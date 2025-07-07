package hr.javafx.webtrackly.app.exception;

/**
 * Exception koji se baca kada dođe do greške u radu s repozitorijem podataka.
 * Ovaj exception se koristi za označavanje situacija kada operacije nad repozitorijem
 * ne uspiju, poput grešaka pri dohvaćanju, spremanju ili brisanju podataka.
 * Primjerice, može se koristiti kada se pokuša dohvatiti podatak koji ne postoji
 * ili kada dođe do greške pri povezivanju s bazom podataka.
 */

public class RepositoryException extends RuntimeException{
    public RepositoryException() {
    }

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(Throwable cause) {
        super(cause);
    }

    public RepositoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
