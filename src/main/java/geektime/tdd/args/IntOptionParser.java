package geektime.tdd.args;

import geektime.tdd.args.annotation.Option;
import java.util.List;

/**
 * 整数类型命令行参数值解释器
 *
 * @author 李小平
 */
class IntOptionParser implements OptionParser {

  @Override
  public Object parse(List<String> arguments, Option option) {
    Object argValue;
    int argNameIndex = arguments.indexOf("-" + option.value());
    argValue = Integer.valueOf(arguments.get(argNameIndex + 1));
    return argValue;
  }
}
