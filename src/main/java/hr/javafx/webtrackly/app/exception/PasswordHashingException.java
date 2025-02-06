package hr.javafx.webtrackly.app.exception;

public class PasswordHashingException extends RuntimeException{
    public PasswordHashingException() {
    }

    public PasswordHashingException(String message) {
        super(message);
    }

    public PasswordHashingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordHashingException(Throwable cause) {
        super(cause);
    }

    public PasswordHashingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
