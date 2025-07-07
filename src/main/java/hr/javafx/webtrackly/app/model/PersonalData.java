package hr.javafx.webtrackly.app.model;
import hr.javafx.webtrackly.app.enums.GenderType;
import java.time.LocalDate;

/**
 * Reprezentira osobne podatke korisnika aplikacije.
 * Sadrži datum rođenja, nacionalnost i spol korisnika.
 */

public record PersonalData(LocalDate dateOfBirth, String nationality, GenderType gender) {
}
