package geektime.tdd.args;

import static geektime.tdd.args.OptionParsers.bool;
import static geektime.tdd.args.OptionParsers.unary;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import geektime.tdd.args.annotation.Option;
import geektime.tdd.args.exception.LackOptionException;
import geektime.tdd.args.exception.LackParserException;
import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

/**
 * 命令行参数解析
 *
 * @author 李小平
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ArgsTest {

  // -l -p 8080 -d /usr/logs
  // [-l], [-p, 8080], [-d, /usr/logs]
  // {-l:[]}, {-p: [8080]}, {-d: [/usr/logs]}

  // multi options: -l -p 8080 -d /usr/log

  @Test
  void should_parse_multi_options() {
    MultiOptions options = Args.parse(MultiOptions.class,
        "-l", "-p", "8088", "-d", "/usr/logs");
    assertThat(options).isEqualTo(new MultiOptions(true, 8088, "/usr/logs"));
  }

  public record MultiOptions(
      @Option("l")boolean logging,
      @Option("p")int port,
      @Option("d") String directory){}

  @Test
  @SuppressWarnings("unchecked")
  void should_parse_multi_options_behavior() {
    Function<String, Integer> mockParse1 = mock(Function.class);
    Function<String, String> mockParse2 = mock(Function.class);
    final Map<Class<?>, OptionParser<?>> register = Map.of(
        boolean.class, bool(),
        int.class, unary(0, mockParse1),
        String.class, unary("", mockParse2));

    Args args = new Args(register, "-l", "-p", "8088", "-d", "/usr/logs");
    args.parse(MultiOptions.class);
    verify(mockParse1).apply("8088");
    verify(mockParse2).apply("/usr/logs");
  }

  @Test
  void should_throw_lack_option_exception_if_option_annotation_not_present() {
    LackOptionException exception = assertThrows(LackOptionException.class,
        () -> Args.parse(MultiOptionsLackOption.class,
            "-l", "-p", "8088", "-d", "/usr/logs"));
    assertThat(exception.getOption()).contains("port");
  }

  public record MultiOptionsLackOption(
      @Option("l")boolean logging,
      int port,
      @Option("d") String directory){}


  // ：不支持的类型
  @Test
  void should_throw_lack_parser_exception_for_long_single_value_option() {
    LackParserException exception = assertThrows(LackParserException.class,
        () -> Args.parse(MultiOptionsLackParser.class,
            "-l", "-p", "8088", "-d", "/usr/logs"));
    assertThat(exception.getOption()).contains("port");
  }


  public record MultiOptionsLackParser(
      @Option("l")boolean logging,
      @Option("p") Long port,
      @Option("d") String directory){}

  // -g this is a list -d 1 2 -3 5
  @Test
  void should_parse_arrays_options() {
    ArrayOptions options = Args.parse(ArrayOptions.class,
        "-g", "this", "is","a", "list", "-d", "1", "2", "-3", "5");
    assertThat(options.group).isEqualTo(new String[]{"this", "is","a", "list"});
    assertThat(options.decimals).isEqualTo(new Integer[]{1, 2, -3, 5});
  }

  record ArrayOptions(
      @Option("g") String[] group,
      @Option("d") Integer[] decimals
  ) {}

}
