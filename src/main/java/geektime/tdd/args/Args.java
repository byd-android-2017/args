package geektime.tdd.args;

import static geektime.tdd.args.SingleValueOptionParser.createSingleValueOptionParser;

import geektime.tdd.args.annotation.Option;
import geektime.tdd.args.exception.ArgumentParseException;
import geektime.tdd.args.exception.LackOptionException;
import geektime.tdd.args.exception.LackParserException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
   * @param args         命令行参数
   * @param <T>          目标对象类
   * @return 目标对象实例
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
   * 解释构造器参数对应的值
   *
   * @param arguments 命令行参数列表
   * @param parameter 构造器方法参数
   * @return 构造器方法参数值
   */
  private static Object parseOptionArgument(List<String> arguments, Parameter parameter) {
    Class<?> parameterType = parameter.getType();
    Optional<OptionParser<?>> parserOptional = buildOptionParser(parameterType);
    return parserOptional.map(parser -> {
      var optionOptional = Optional.ofNullable(parameter.getAnnotation(Option.class));
      return optionOptional.map(option -> parser.parse(arguments, option))
          .orElseThrow(() -> new LackOptionException(parameter.getName() + "缺少@Option."));
    }).orElseThrow(() -> new LackParserException("类型：" + parameterType.getCanonicalName()
        + "的" + parameter.getName() + "没有注册解释器"));

  }

  /**
   * 目标对象构造器参数类型构造对应参数值解释器注册器
   */
  private static final Map<Class<?>, OptionParser<?>> OPTION_PARSER_REGISTER = Map.of(
      boolean.class, new BooleanOptionParser(),
      int.class, createSingleValueOptionParser(0, Integer::valueOf),
      String.class, createSingleValueOptionParser("", argValue -> argValue)
  );

  /**
   * 目标对象构造器参数类型构造相应的参数值解释器
   *
   * @param parameterType 目标对象构造器参数类型
   * @return 参数值解释器
   */
  private static  Optional<OptionParser<?>> buildOptionParser(Class<?> parameterType) {
    return Optional.ofNullable(OPTION_PARSER_REGISTER.get(parameterType));
  }

}
