package geektime.tdd.args;

import geektime.tdd.args.annotation.Option;
import geektime.tdd.args.exception.TooManyArgumentsException;
import java.util.List;

/**
 * 布尔类型命令行参数值解释器
 *
 * @author 李小平
 */
class BooleanOptionParser implements OptionParser<Boolean> {

  @Override
  public Boolean parse(List<String> arguments, Option option) throws TooManyArgumentsException {
    // -bool -l t / -l t f
    int flagIndex = arguments.indexOf("-" + option.value());

    int nextElementIndex = flagIndex + 1;
    boolean isNotEndOfList = nextElementIndex < arguments.size();
    boolean isFollowingArgument =
        isNotEndOfList && !arguments.get(nextElementIndex).startsWith("-");
    if (isFollowingArgument) {
      throw new TooManyArgumentsException(option.value());
    }
    return flagIndex > -1;
  }
}
