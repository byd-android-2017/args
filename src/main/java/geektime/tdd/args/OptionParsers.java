package geektime.tdd.args;

import geektime.tdd.args.annotation.Option;
import geektime.tdd.args.exception.InsufficientArgumentsException;
import geektime.tdd.args.exception.TooManyArgumentsException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;

/**
 * 单值或标识类型命令行参数值解释器工厂
 *
 * @author 李小平
 */
class OptionParsers {

  private OptionParsers() {
  }

  /**
   * 创建单值命令行解释器
   *
   * @param <T>           选项值类型
   * @param defaultValue  命令行选项默认值
   * @param parseValueFun 选项值解释器
   * @return 选项解释器
   */
  public static <T> OptionParser<T> unary(T defaultValue,
      @NotNull Function<String, T> parseValueFun) {
    return (arguments, option) -> fetchOptionValue(
        arguments, option, 1, defaultValue, parseValueFun);
  }


  /**
   * 创建flag命令行解释器
   */
  public static OptionParser<Boolean> bool() {
    return (arguments, option) -> fetchOptionValue(
        arguments, option, 0, false, it -> true);
  }

  /**
   * 多值参数项值解释器
   *
   * @param defaultValue  默认值
   * @param generator     数据构建器
   * @param parseValueFun 选项值解释器
   * @param <T>           选项值类型
   * @return 选项值类型数组
   */
  public static <T> OptionParser<T[]> list(T[] defaultValue,
      @NotNull IntFunction<T[]> generator,
      @NotNull Function<String, T> parseValueFun) {
    return (arguments, option) -> {
      final var flagIndex = arguments.indexOf("-" + option.value());

      final var flagValuesOptional = extractFlagValue(
            arguments, flagIndex);
      return flagValuesOptional.map(flagValues -> {
        validListOptionValue(option, flagValues);

        try {
          return flagValues.stream().map(parseValueFun).toArray(generator);
        } catch (Exception e) {
          throw throwIllegalArgumentException(option, flagValues, e);
        }
      }).orElse(defaultValue);

    };
  }

  private static void validListOptionValue(Option option, List<String> flagValues) {
    if (flagValues.isEmpty()) {
      throw new InsufficientArgumentsException(option.value());
    }
  }

  private static <T> T fetchOptionValue(List<String> arguments, Option option,
      int expectedSize, T defaultValue,
      @NotNull Function<String, T> parseValueFun) {
    final var flagIndex = arguments.indexOf("-" + option.value());

    // 命令行参数标识不存在时，返回默认值
    if (flagIndex == -1) {
      return defaultValue;
    }

    final var flagValuesOptional = extractFlagValue(
        arguments, flagIndex);
    return flagValuesOptional.map(flagValues -> {
        validOptionValue(option, flagValues, expectedSize);

        try {
          return parseValueFun.apply(flagValues.isEmpty() ? null : flagValues.get(0));
        } catch (Exception e) {
          throw throwIllegalArgumentException(option, flagValues, e);
        }

    }).orElse(defaultValue);
  }

  @NotNull
  private static IllegalArgumentException throwIllegalArgumentException(Option option,
      List<String> flagValues, Exception e) {
    return new IllegalArgumentException(option.value() + "对应的参数值:"
        + flagValues + "格式不对", e);
  }
  
  private static void validOptionValue(Option option, List<String> flagValues, int expectedSize) {
    int size = flagValues.size();

    if (size < expectedSize) {
      throw new InsufficientArgumentsException(option.value());
    }

    if (size > expectedSize) {
      throw new TooManyArgumentsException(option.value());
    }

  }

  /**
   * 提取命令行指定选项参数值列表，如果选择项不存在，返回:<link>Optional.empty</link>
   * 并对参数值个数进行验证
   *
   * @param arguments         参数值列表
   * @param flagIndex         选项索引号
   * @return 选项参数值列表（原始值）
   */
  @NotNull
  private static Optional<List<String>> extractFlagValue(List<String> arguments, int flagIndex)
      throws InsufficientArgumentsException, TooManyArgumentsException {
    List<String> values;
    if (-1 == flagIndex) {
      values = null;
    } else {
      final var size = arguments.size();

      final var nextFlagIndex = IntStream.range(flagIndex + 1, size)
          .filter(index -> arguments.get(index).startsWith("-"))
          .findFirst()
          .orElse(size);
      values = arguments.subList(flagIndex + 1, nextFlagIndex);
    }

    return Optional.ofNullable(values);
  }

}
