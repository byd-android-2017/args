package geektime.tdd.args;

import static geektime.tdd.args.OptionParsers.bool;
import static geektime.tdd.args.OptionParsers.unary;

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
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/**
 * 命令行参数解析
 *
 * @author LiXiaoPing
 */
public class Args {

  private final Map<Class<?>, OptionParser<?>> register;
  private final List<String> arguments;

  public Args(Map<Class<?>, OptionParser<?>> register, String... cmdLines) {
    this.register = register;
    this.arguments = List.of(cmdLines);
  }

  /**
   * 解释命令行参数
   *
   * @param optionsClass 目标对象类
   * @param cmdLines     命令行参数
   * @param <T>          目标对象类
   * @return 目标对象实例
   */
  public static <T> T parse(Class<T> optionsClass, String... cmdLines) {
    final var args = new Args(OPTION_PARSER_REGISTER, cmdLines);
    return args.parse(optionsClass);
  }

  @NotNull
  @SuppressWarnings("unchecked")
  public <T> T parse(Class<T> optionsClass) {
    try {
      Constructor<?> constructor = optionsClass.getDeclaredConstructors()[0];

      Object[] argsValues = Arrays.stream(constructor.getParameters()).map(
          this::parseOptionArgument
      ).toArray();

      return (T) constructor.newInstance(argsValues);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new ArgumentParseException("命令行参数解释失败.", e);
    }
  }

  /**
   * 解释构造器参数对应的值
   *
   * @param parameter 构造器方法参数
   * @return 构造器方法参数值
   */
  private Object parseOptionArgument(Parameter parameter) {
    Class<?> parameterType = parameter.getType();
    Optional<OptionParser<?>> parserOptional = Optional.ofNullable(
        register.get(parameterType));
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
      boolean.class, bool(),
      int.class, unary(0, Integer::valueOf),
      String.class, unary("", Function.identity()),
      Integer[].class, OptionParsers.list(new Integer[0], Integer[]::new, Integer::parseInt),
      String[].class, OptionParsers.list(new String[0], String[]::new, Function.identity())
  );

}
