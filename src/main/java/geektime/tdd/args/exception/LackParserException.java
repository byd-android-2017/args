package geektime.tdd.args.exception;


/**
 * @author 李小平
 * @Date 2022-11-28 08:54
 * @Version V1.0
 */
public class LackParserException extends RuntimeException {

   public LackParserException(String option) {
      super(option);
   }

   public LackParserException(String option, Throwable cause) {
      super(option, cause);
   }

   public String getOption() {
      return super.getMessage();
   }
}
