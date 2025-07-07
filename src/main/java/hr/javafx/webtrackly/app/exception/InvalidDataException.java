package hr.javafx.webtrackly.app.exception;

/**
 * Exception koji se baca kada dođe do greške u obradi podataka.
 * Ovaj exception se koristi za označavanje situacija kada su podaci neispravni,
 * nedostaju ili nisu u očekivanom formatu.
 * Primjerice, može se koristiti kada se pokuša obraditi podatke koji su prazni,
 * neispravnog tipa ili ne zadovoljavaju određene kriterije validacije.
 */

public class InvalidDataException extends Exception{
    public InvalidDataException() {
    }

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDataException(Throwable cause) {
        super(cause);
    }

    public InvalidDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
