package geektime.tdd.args;

import static org.assertj.core.api.Assertions.assertThat;

import geektime.tdd.args.annotation.Option;
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

  // Single Option:
  // Bool -1
  @Test
  void should_set_boolean_option_to_true_if_flag_present() {
    BooleanOption option = Args.parse(BooleanOption.class, "-l");
    assertThat(option)
        .extracting(BooleanOption::logging)
        .isEqualTo(true);
  }

  @Test
  void should_set_boolean_option_to_false_if_flag_not_present() {
    BooleanOption option = Args.parse(BooleanOption.class);
    assertThat(option)
        .extracting(BooleanOption::logging)
        .isEqualTo(false);
  }

  record BooleanOption(@Option("l")boolean logging) {}

  // Integer -p 8080

  @Test
  void should_parse_int_as_option_value() {
    IntOption option = Args.parse(IntOption.class, "-p", "8080");
    assertThat(option)
        .extracting(IntOption::port)
        .isEqualTo(8080);
  }

  record IntOption(@Option("p")int port) {}

  // String -d /usr/logs

  @Test
  void should_parse_string_as_option_value() {
    StringOption option = Args.parse(StringOption.class, "-d", "/usr/logs");
    assertThat(option)
        .extracting(StringOption::directory)
        .isEqualTo("/usr/logs");
  }

  record StringOption(@Option("d") String directory) {}

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

  // sad path:
  // TODO: -bool -l t / -l t f
  // TODO: - int -p / -p 8088 8081
  // TODO: - string -d/ -d /usr/logs /usr/vars

  // default value
  // TODO: - bool : false
  // TODO: -int :0
  // TODO: - string ""





}
