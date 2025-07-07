package hr.javafx.webtrackly.app.exception;

/**
 * Exception koji se baca kada se pokuša dodati entitet koji već postoji u sustavu.
 * Ovaj exception se koristi za označavanje situacija kada se pokušava dodati duplikat entiteta,
 * što može biti rezultat pokušaja dodavanja entiteta s istim identifikatorom ili drugim jedinstvenim atributima.
 * Primjerice, može se koristiti kada se pokuša dodati korisnik s istim usernameom ili e-mail adresom
 */

public class DuplicateEntityException extends RuntimeException{
    public DuplicateEntityException() {
    }

    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateEntityException(Throwable cause) {
        super(cause);
    }

    public DuplicateEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
