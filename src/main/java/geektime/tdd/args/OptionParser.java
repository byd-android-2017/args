package geektime.tdd.args;

import geektime.tdd.args.annotation.Option;
import java.util.List;

/**
 * 目标对象构造器参数类型对应的参数值解释器
 * @param <T> 命令行参数值解释后的类型
 */
interface OptionParser<T> {

  /**
   * 解释目标对象构造器参数对应的命令行参数值
   *
   * @param arguments 命令行参数列表
   * @param option    目标对象属性标注
   * @return 目标对象属性对应的命令行参数值
   */
  T parse(List<String> arguments, Option option);
}
