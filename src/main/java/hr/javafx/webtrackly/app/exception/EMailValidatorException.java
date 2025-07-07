package hr.javafx.webtrackly.app.exception;

/**
 * Exception koji se baca kada dođe do greške u validaciji e-mail adrese.
 * Ovaj exception se koristi za označavanje situacija kada e-mail adresa nije ispravnog formata ili
 * ne zadovoljava određene kriterije validacije.
 * Primjerice, može se koristiti kada se pokuša unijeti e-mail adresa koja ne sadrži
 * ispravne znakove, nije u ispravnom formatu ili je prazna.
 *
 */

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
