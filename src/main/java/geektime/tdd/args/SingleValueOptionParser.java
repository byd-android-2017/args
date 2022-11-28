package geektime.tdd.args;

import geektime.tdd.args.annotation.Option;
import geektime.tdd.args.exception.InsufficientArgumentsException;
import geektime.tdd.args.exception.TooManyArgumentsException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;

/**
 * 字符串类型命令行参数值解释器
 *
 * @param <T> 命令行参数值目标类型
 *
 * @author 李小平
 */
class SingleValueOptionParser<T> implements OptionParser<T> {

  private final T defaultValue;

  /**
   * 解释输入的参数值的转换器函数
   */
  private final Function<String, T> parseValueFun;

  private SingleValueOptionParser(T defaultValue, @NotNull Function<String, T> parseValueFun) {
    this.defaultValue = defaultValue;
    this.parseValueFun = parseValueFun;
  }

  public static <T> SingleValueOptionParser<T> createSingleValueOptionParser(T defaultValue,
      @NotNull Function<String, T> parseValueFun) {
    return new SingleValueOptionParser<>(defaultValue, parseValueFun);
  }

  @Override
  public T parse(List<String> arguments, Option option) {
    int flagIndex = arguments.indexOf("-" + option.value());

    // 命令行参数标识不存在时，返回默认值
    if (flagIndex == -1) {
      return defaultValue;
    }

    int nextFlagIndex = IntStream.range(flagIndex + 1, arguments.size())
        .filter(index -> arguments.get(index).startsWith("-"))
        .findFirst()
        .orElse(arguments.size());
    List<String> flagValues = arguments.subList(flagIndex + 1, nextFlagIndex);

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
  }

}
