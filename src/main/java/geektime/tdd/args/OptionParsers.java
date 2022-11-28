package geektime.tdd.args;

import geektime.tdd.args.annotation.Option;
import geektime.tdd.args.exception.InsufficientArgumentsException;
import geektime.tdd.args.exception.TooManyArgumentsException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;

/**
 * 单值或标识类型命令行参数值解释器工厂
 *
 * @author 李小平
 */
class OptionParsers {

  private OptionParsers() {}

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
        arguments, option, true, defaultValue, parseValueFun);
  }

  /**
   * 创建flag命令行解释器
   *
   */
  public static OptionParser<Boolean> bool() {
    return (arguments, option) -> fetchOptionValue(
        arguments, option, false, false, it -> true);
  }

  private static <T> T fetchOptionValue(List<String> arguments, Option option,
      boolean requiredFlagValue, T defaultValue,
      @NotNull Function<String, T> parseValueFun) {
    final var flagIndex = arguments.indexOf("-" + option.value());

    // 命令行参数标识不存在时，返回默认值
    if (flagIndex == -1) {
      return defaultValue;
    }

    final var flagValues = extractFlagValue(arguments, flagIndex);

    if (requiredFlagValue) {
      if (flagValues.isEmpty()) {
        throw new InsufficientArgumentsException(option.value());
      } else if (flagValues.size() > 1) {
        throw new TooManyArgumentsException(option.value());
      }

      try {
        return parseValueFun.apply(flagValues.get(0));
      } catch (Exception e) {
        throw new IllegalArgumentException(option.value() + "对应的参数值:"
            + flagValues.get(0)  + "格式不对", e);
      }
    } else {
       if (!flagValues.isEmpty()) {
         throw new TooManyArgumentsException(option.value());
       }

       try {
         return parseValueFun.apply(null);
      } catch (Exception e) {
        throw new IllegalArgumentException(option.value() + "对应的参数值:"
            + flagValues.get(0)  + "格式不对", e);
      }
    }
  }

  /**
   * 提取命令行指定选项参数值列表
   *
   * @param arguments 参数值列表
   * @param flagIndex 选项索引号
   * @return 选项参数值列表（原始值）
   */
  @NotNull
  private static List<String> extractFlagValue(List<String> arguments, int flagIndex) {
    final var size = arguments.size();
    final var nextFlagIndex = IntStream.range(flagIndex + 1, size)
        .filter(index -> arguments.get(index).startsWith("-"))
        .findFirst()
        .orElse(size);
    return arguments.subList(flagIndex + 1, nextFlagIndex);
  }

}
