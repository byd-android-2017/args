package geektime.tdd.args;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import geektime.tdd.args.annotation.Option;
import geektime.tdd.args.exception.LackOptionException;
import geektime.tdd.args.exception.LackParserException;
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
  void should_throw_illegal_exception_if_option_annotation_not_present() {
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
  void should_throw_insufficient_argument_for_long_single_value_option() {
    LackParserException exception = assertThrows(LackParserException.class,
        () -> Args.parse(MultiOptionsLackParser.class,
            "-l", "-p", "8088", "-d", "/usr/logs"));
    assertThat(exception.getOption()).contains("port");
  }

  public record MultiOptionsLackParser(
      @Option("l")boolean logging,
      @Option("p") Long port,
      @Option("d") String directory){}

}
