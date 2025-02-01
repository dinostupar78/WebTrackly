package hr.javafx.webtrackly.app.model;

import hr.javafx.webtrackly.app.enums.GenderType;

import java.time.LocalDate;

public record PersonalData(LocalDate dateOfBirth, String nationality, GenderType gender) {
}
