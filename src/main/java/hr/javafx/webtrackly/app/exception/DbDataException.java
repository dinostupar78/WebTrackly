package hr.javafx.webtrackly.app.exception;

public class DbDataException extends Exception{
    public DbDataException() {
    }

    public DbDataException(String message) {
        super(message);
    }

    public DbDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbDataException(Throwable cause) {
        super(cause);
    }

    public DbDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
