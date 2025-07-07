package hr.javafx.webtrackly.app.exception;

/**
 * Exception koji se baca kada dođe do greške u vezi s povezivanjem na bazu podataka.
 * Ovaj exception može sadržavati poruku o grešci i/ili uzrok greške.
 * Ovaj exception se koristi za označavanje situacija kada aplikacija ne može uspostaviti vezu s bazom podataka,
 */

public class DbConnectionException extends Exception{
    public DbConnectionException() {
    }

    public DbConnectionException(String message) {
        super(message);
    }

    public DbConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbConnectionException(Throwable cause) {
        super(cause);
    }

    public DbConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
