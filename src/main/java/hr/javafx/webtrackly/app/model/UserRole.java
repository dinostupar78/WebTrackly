package hr.javafx.webtrackly.app.model;

/**
 * Klasa koja predstavlja ulogu korisnika u aplikaciji.
 * Implementira sučelje Role i definira osnovne karakteristike uloge korisnika.
 */

public final class UserRole implements Role {
    @Override
    public String getPermission() {
        return "NO_ACCESS";
    }

    @Override
    public String toString() {
        return "User";
    }
}
