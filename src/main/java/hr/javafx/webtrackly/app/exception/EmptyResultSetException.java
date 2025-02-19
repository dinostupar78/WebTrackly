package hr.javafx.webtrackly.app.exception;

public class EmptyResultSetException extends RuntimeException{
    public EmptyResultSetException() {
    }

    public EmptyResultSetException(String message) {
        super(message);
    }

    public EmptyResultSetException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyResultSetException(Throwable cause) {
        super(cause);
    }

    public EmptyResultSetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
