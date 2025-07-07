package hr.javafx.webtrackly.app.model;
import java.io.Serializable;

/**
 * Klasa koja predstavlja ulogu administratora u aplikaciji WebTrackly.
 * Implementira sučelje Role i sadrži metode za dobivanje dozvola i string reprezentaciju uloge.
 */

public final class AdminRole implements Role, Serializable
{

    @Override
    public String getPermission() {
        return "FULL_ACCESS";
    }

    @Override
    public String toString() {
        return "Admin";
    }
}
