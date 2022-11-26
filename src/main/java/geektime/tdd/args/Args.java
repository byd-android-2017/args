package geektime.tdd.args;

import geektime.tdd.args.annotation.Option;
import geektime.tdd.args.exception.ArgumentParseException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

/**
 * 命令行参数解析
 *
 * @author LiXiaoPing
 */
public class Args {

  private Args() {
  }

  /**
   * 解释命令行参数
   *
   * @param optionsClass 目标对象类
   * @param args 命令行参数
   * @return  目标对象实例
   * @param <T> 目标对象类
   */
  @SuppressWarnings("unchecked")
  public static <T> T parse(Class<T> optionsClass, String... args) {
    try {
      final List<String> arguments = List.of(args);
      Constructor<?> constructor = optionsClass.getDeclaredConstructors()[0];
      Object[] argsValues = Arrays.stream(constructor.getParameters()).map(
          parameter -> parseOptionArgument(arguments, parameter)
      ).toArray();

      return (T) constructor.newInstance(argsValues);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new ArgumentParseException("命令行参数解释失败.", e);
    }
  }

  /**
   *  解释构造器参数对应的值
   *
   * @param arguments 命令行参数列表
   * @param parameter 构造器方法参数
   * @return 构造器方法参数值
   */
  private static Object parseOptionArgument(List<String> arguments, Parameter parameter) {
    Option option = parameter.getAnnotation(Option.class);
    Object argValue = null;
    if (parameter.getType() == boolean.class) {
      argValue = arguments.contains("-" + option.value());
    } else if (parameter.getType() == int.class) {
      int argNameIndex = arguments.indexOf("-" + option.value());
      argValue = Integer.valueOf(arguments.get(argNameIndex + 1));
    }  else if (parameter.getType() == String.class) {
      int argNameIndex = arguments.indexOf("-" + option.value());
      argValue = arguments.get(argNameIndex + 1);
    }
    return argValue;
  }


}
