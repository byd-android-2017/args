package geektime.tdd.args.exception;


/**
 * @author 李小平
 * @Date 2022-11-28 08:54
 * @Version V1.0
 */
public class TooManyArgumentsException extends RuntimeException {

   public TooManyArgumentsException(String option) {
      super(option);
   }

   public TooManyArgumentsException(String option, Throwable cause) {
      super(option, cause);
   }

   public String getOption() {
      return super.getMessage();
   }
}
