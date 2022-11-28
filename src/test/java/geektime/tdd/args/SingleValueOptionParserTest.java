package geektime.tdd.args;

import static geektime.tdd.args.SingleValueOptionParser.createSingleValueOptionParser;
import static java.util.function.Function.identity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import geektime.tdd.args.annotation.Option;
import geektime.tdd.args.exception.InsufficientArgumentsException;
import geektime.tdd.args.exception.TooManyArgumentsException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author 李小平
 * @Date 2022-11-28 10:17
 * @Version V1.0
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SingleValueOptionParserTest {

  // sad path:
  // - int -p 8080 8081
  @Test
  void should_not_accept_extra_argument_for_int_single_value_option() {
    final OptionParser<Integer> parser = createSingleValueOptionParser(0, Integer::parseInt);
    final List<String> arguments = List.of("-p", "8080", "8081");
    final Option option = option("p");

    TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class,
        () -> parser.parse(arguments, option));
    assertThat(e.getOption()).isEqualTo("p");
  }

  // - int -p/ -p -l

  @ParameterizedTest(name = "{index}) cmdLine:{0}")
  @ValueSource(strings = {"-p", "-p -l"})
  void should_not_accept_insufficient_argument_for_int_single_value_option(String cmdLine) {
    final OptionParser<Integer> parser = createSingleValueOptionParser(0, Integer::parseInt);
    final List<String> arguments = List.of(cmdLine.split(" "));
    final Option option = option("p");

    InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class,
        () -> parser.parse(arguments, option));
    assertThat(e.getOption()).isEqualTo("p");
  }

  // - string-d /usr/logs /usr/vars
  @Test
  void should_not_accept_extra_argument_for_string_single_value_option() {
    final OptionParser<String> parser = createSingleValueOptionParser("", identity());
    final List<String> arguments = List.of("-d", "/usr/logs", "/usr/vars");
    final Option option = option("d");

    TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class,
        () -> parser.parse(arguments, option));
    assertThat(e.getOption()).isEqualTo("d");
  }

  // - string -d / -d -l
  @ParameterizedTest(name = "{index}) cmdLine:{0}")
  @ValueSource(strings = {"-d", "-d -l"})
  void should_not_accept_insufficient_argument_for_string_single_value_option(String cmdLine) {
    final OptionParser<String> parser = createSingleValueOptionParser("", identity());
    final List<String> arguments = List.of(cmdLine.split(" "));
    final Option option = option("d");

    InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class,
        () -> parser.parse(arguments, option));
    assertThat(e.getOption()).isEqualTo("d");
  }

  // default value:
  // -int :0
  @Test
  void should_set_default_value_to_0_not_present_int_value_option() {
    final OptionParser<Integer> parser = createSingleValueOptionParser(0, Integer::parseInt);
    final List<String> arguments = Collections.emptyList();
    final Option option = option("p");

    Integer argVaule = parser.parse(arguments, option);
    assertThat(argVaule).isZero();
  }


  //  - string ""
  @Test
  void should_set_default_value_to_0_not_present_str_value_option() {
    final OptionParser<String> parser = createSingleValueOptionParser("", identity());
    final List<String> arguments = Collections.emptyList();
    final Option option = option("s");

    var argVaule = parser.parse(arguments, option);
    assertThat(argVaule).isEmpty();
  }

  Option option(String value) {
    return new Option() {

      @Override
      public Class<? extends Annotation> annotationType() {
        return Option.class;
      }

      @Override
      public String value() {
        return value;
      }
    };
  }

}
