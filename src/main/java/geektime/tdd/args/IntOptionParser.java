package geektime.tdd.args;

/**
 * 整数类型命令行参数值解释器
 *
 * @author 李小平
 */
class IntOptionParser extends StringOptionParser {

  public IntOptionParser() {
    super.parseValue = Integer::valueOf;
  }

}
