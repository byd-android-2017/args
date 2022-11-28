package geektime.tdd.args;

import static geektime.tdd.args.OptionParsers.bool;
import static geektime.tdd.args.OptionParsers.unary;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * SingleValueOptionParserTest
 * @author 李小平
 * @Date 2022-11-28 10:17
 * @Version V1.0
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OptionParsersTest {

  @Nested
  class FlagOptionParserTest {
    // Happy path:
    // Bool -1
    @Test
    void should_set_boolean_option_to_true_if_flag_present() {
      final OptionParser<Boolean> parser = bool();
      Boolean argValue = parser.parse(List.of("-l"), option("l"));
      assertThat(argValue).isTrue();
    }

    // sad path:
    // -bool -l t / -l t f

    @ParameterizedTest
    @ValueSource(strings = {"-l t", "-l t f"})
    void should_not_accept_extra_argument_for_boolean_option(String cmdLine) {
      final OptionParser<Boolean> parser = bool();
      final List<String> arguments = List.of(cmdLine.split(" "));
      final Option option = option("l");

      TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () ->
          parser.parse(arguments, option));
      assertThat(e.getOption()).isEqualTo("l");
    }


    // default path:
    // - bool : false
    @Test
    void should_set_default_value_to_false_when_flag_option_not_present() {
      final OptionParser<Boolean> parser = bool();
      Boolean argumentValue = parser.parse(Collections.emptyList(), option("l"));
      assertThat(argumentValue).isFalse();
    }
  }


  @Nested
  class SingleValueOptionParserTest {

    // happy path:
    // Integer -p 8080
    @Test
    void should_parse_int_as_option_value() {
      final OptionParser<Integer> parser = unary(0, Integer::parseInt);
      final List<String> arguments = List.of("-p", "8080");
      final Option option = option("p");
      assertThat(parser.parse(arguments, option)).isEqualTo(8080);
    }

    // String -d /usr/logs

    @Test
    void should_parse_string_as_option_value() {
      final OptionParser<String> parser = unary("", identity());
      final List<String> arguments = List.of("-d", "/usr/logs");
      final Option option = option("d");
      assertThat(parser.parse(arguments, option)).isEqualTo("/usr/logs");
    }

    // sad path:
    // - int -p 8080 8081
    @Test
    void should_not_accept_extra_argument_for_int_single_value_option() {
      final OptionParser<Integer> parser = unary(0, Integer::parseInt);
      final List<String> arguments = List.of("-p", "8080", "8081");
      final Option option = option("p");

      TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class,
          () -> parser.parse(arguments, option));
      assertThat(e.getOption()).isEqualTo("p");
    }

    // 错误的数值格式
    // - int -p 8080L
    @Test
    void should_throw_illegal_argument_exception_for_int_single_value_option() {
      final OptionParser<Integer> parser = unary(0, Integer::parseInt);
      final List<String> arguments = List.of("-p", "8080L");
      final Option option = option("p");

      IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
          () -> parser.parse(arguments, option));
      assertThat(e.getMessage()).startsWith("p");
    }

    // - int -p/ -p -l

    @ParameterizedTest(name = "{index}) cmdLine:{0}")
    @ValueSource(strings = {"-p", "-p -l"})
    void should_not_accept_insufficient_argument_for_int_single_value_option(String cmdLine) {
      final OptionParser<Integer> parser = unary(0, Integer::parseInt);
      final List<String> arguments = List.of(cmdLine.split(" "));
      final Option option = option("p");

      InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class,
          () -> parser.parse(arguments, option));
      assertThat(e.getOption()).isEqualTo("p");
    }

    // - string -d /usr/logs /usr/vars
    @Test
    void should_not_accept_extra_argument_for_string_single_value_option() {
      final OptionParser<String> parser = unary("", identity());
      final List<String> arguments = List.of("-d", "/usr/logs", "/usr/vars");
      final Option option = option("d");

      TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class,
          () -> parser.parse(arguments, option));
      assertThat(e.getOption()).isEqualTo("d");
    }

    // sad path:
    // - string -d / -d -l
    @ParameterizedTest(name = "{index}) cmdLine:{0}")
    @ValueSource(strings = {"-d", "-d -l"})
    void should_not_accept_insufficient_argument_for_string_single_value_option(String cmdLine) {
      final OptionParser<String> parser = unary("", identity());
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
      final OptionParser<Integer> parser = unary(0, Integer::parseInt);
      final List<String> arguments = Collections.emptyList();
      final Option option = option("p");

      Integer argVaule = parser.parse(arguments, option);
      assertThat(argVaule).isZero();
    }


    //  - string ""
    @Test
    void should_set_default_value_to_0_not_present_str_value_option() {
      final OptionParser<String> parser = unary("", identity());
      final List<String> arguments = Collections.emptyList();
      final Option option = option("s");

      var argVaule = parser.parse(arguments, option);
      assertThat(argVaule).isEmpty();
    }

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
