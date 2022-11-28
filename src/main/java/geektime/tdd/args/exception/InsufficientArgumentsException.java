package geektime.tdd.args.exception;


/**
 * @author 李小平
 * @Date 2022-11-28 08:54
 * @Version V1.0
 */
public class InsufficientArgumentsException extends RuntimeException {

   public InsufficientArgumentsException(String option) {
      super(option);
   }

   public InsufficientArgumentsException(String option, Throwable cause) {
      super(option, cause);
   }

   public String getOption() {
      return super.getMessage();
   }
}
