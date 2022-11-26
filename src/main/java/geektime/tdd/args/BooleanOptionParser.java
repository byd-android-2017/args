package geektime.tdd.args;

import geektime.tdd.args.annotation.Option;
import java.util.List;

/**
 * 布尔类型命令行参数值解释器
 *
 * @author 李小平
 */
class BooleanOptionParser implements OptionParser {

  @Override
  public Object parse(List<String> arguments, Option option) {
    Object argValue;
    argValue = arguments.contains("-" + option.value());
    return argValue;
  }
}