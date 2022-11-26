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
    Object argValue;
    int argNameIndex = arguments.indexOf("-" + option.value());
    argValue = arguments.get(argNameIndex + 1);
    return argValue;
  }
}
