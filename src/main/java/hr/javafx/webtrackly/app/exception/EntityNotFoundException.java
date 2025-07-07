package hr.javafx.webtrackly.app.exception;

/**
 * Exception koji se baca kada se pokuša pristupiti entitetu koji ne postoji u sustavu.
 * Ovaj exception se koristi za označavanje situacija kada se pokuša dohvatiti entitet
 * koji nije pronađen u bazi podataka ili drugom skladištu podataka.
 * Primjerice, može se koristiti kada se pokuša dohvatiti korisnik s određenim ID-em
 * koji ne postoji u sustavu.
 */

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

    public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
