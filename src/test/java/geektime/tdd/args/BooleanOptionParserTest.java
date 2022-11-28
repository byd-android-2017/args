package geektime.tdd.args;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import geektime.tdd.args.annotation.Option;
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
 *
 * @author 李小平
 * @Date 2022-11-28 08:51
 * @Version V1.0
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BooleanOptionParserTest {

  // Happy path:
  // Bool -1
  @Test
  void should_set_boolean_option_to_true_if_flag_present() {
    final OptionParser<Boolean> parser = new BooleanOptionParser();
    Boolean argValue = parser.parse(List.of("-l"), option());
    assertThat(argValue).isTrue();
  }

  // sad path:
  // -bool -l t / -l t f

  @ParameterizedTest
  @ValueSource(strings = {"-l t", "-l t f"})
  void should_not_accept_extra_argument_for_boolean_option(String cmdLine) {
    final OptionParser<Boolean> parser = new BooleanOptionParser();
    final List<String> arguments = List.of(cmdLine.split(" "));
    final Option option = option();

    TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () ->
        parser.parse(arguments, option));
    assertThat(e.getOption()).isEqualTo("l");
  }


   // default path:
   // - bool : false
   @Test
   void should_set_default_value_to_false_when_flag_option_not_present() {
     final OptionParser<Boolean> parser = new BooleanOptionParser();
     Boolean argumentValue = parser.parse(Collections.emptyList(), option());
     assertThat(argumentValue).isFalse();
   }

   Option option() {
       return new Option() {

         @Override
         public Class<? extends Annotation> annotationType() {
           return Option.class;
         }

         @Override
         public String value() {
           return "l";
         }
       };
   }

}
