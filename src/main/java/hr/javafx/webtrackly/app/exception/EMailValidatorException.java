package hr.javafx.webtrackly.app.exception;

public class EMailValidatorException extends Exception {
  public EMailValidatorException() {
  }

  public EMailValidatorException(String message) {
    super(message);
  }

  public EMailValidatorException(String message, Throwable cause) {
    super(message, cause);
  }

  public EMailValidatorException(Throwable cause) {
    super(cause);
  }

  public EMailValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
