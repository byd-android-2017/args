package geektime.tdd.args;

import geektime.tdd.args.annotation.Option;
import java.util.List;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/**
 * 字符串类型命令行参数值解释器
 *
 * @param <T> 命令行参数值目标类型
 *
 * @author 李小平
 */
class SingleValueOptionParser<T> implements OptionParser<T> {

  /**
   * 解释输入的参数值的转换器函数
   */
  private final Function<String, T> parseValueFun;

  public SingleValueOptionParser(@NotNull Function<String, T> parseValueFun) {
    this.parseValueFun = parseValueFun;
  }

  @Override
  public T parse(List<String> arguments, Option option) {
    int argNameIndex = arguments.indexOf("-" + option.value());
    String argValue = arguments.get(argNameIndex + 1);
    return parseValueFun.apply(argValue);
  }

}
