package geektime.tdd.args;

import geektime.tdd.args.annotation.Option;
import java.util.List;

/**
 * 字符串类型命令行参数值解释器
 *
 * @author 李小平
 */
class StringOptionParser implements OptionParser {

  @Override
  public Object parse(List<String> arguments, Option option) {
    int argNameIndex = arguments.indexOf("-" + option.value());
    String argValue = arguments.get(argNameIndex + 1);
    return parseValue(argValue);
  }

  /**
   * 解释输入的参数值
   *
   * @param argValue 输入的参数值
   * @return 返回解释器要求的类型
   */
  protected Object parseValue(String argValue) {
    return argValue;
  }
}
