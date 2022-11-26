package geektime.tdd.args.exception;


/**
 * 命令行参数解释异常
 * @author LiXiaoPing
 */
public class ArgumentParseException extends RuntimeException {

  public ArgumentParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
