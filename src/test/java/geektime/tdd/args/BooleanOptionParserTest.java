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

/**
 *
 * @author 李小平
 * @Date 2022-11-28 08:51
 * @Version V1.0
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BooleanOptionParserTest {

  // sad path:
  // -bool -l t / -l t f

  @Test
  void should_not_accept_extra_argument_for_boolean_option() {
    final OptionParser<Boolean> parser = new BooleanOptionParser();
    final List<String> arguments = List.of("-l", "t");
    final Option option = option();

    TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () ->
        parser.parse(arguments, option));
    assertThat(e.getOption()).isEqualTo("l");
  }

   @Test
   void should_not_accept_extra_arguments_for_boolean_option() {
     final OptionParser<Boolean> parser = new BooleanOptionParser();
     final List<String> arguments = List.of("-l", "t", "f");
     final Option option = option();

     TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () ->
         parser.parse(arguments, option));
     assertThat(e.getOption()).isEqualTo("l");
   }                                                                                     // TODO: - int -p / -p 8088 8081

   // default:
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
