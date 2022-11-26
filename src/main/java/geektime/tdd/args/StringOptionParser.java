package geektime.tdd.args;

import geektime.tdd.args.annotation.Option;
import java.util.List;
import java.util.function.Function;

/**
 * 字符串类型命令行参数值解释器
 *
 * @author 李小平
 */
class StringOptionParser implements OptionParser {

  /**
   * 解释输入的参数值的转换器函数
   */
  protected Function<String, Object> parseValue;

  public StringOptionParser() {
    this.parseValue = value -> value;
  }

  @Override
  public Object parse(List<String> arguments, Option option) {
    int argNameIndex = arguments.indexOf("-" + option.value());
    String argValue = arguments.get(argNameIndex + 1);
    return parseValue.apply(argValue);
  }

}
