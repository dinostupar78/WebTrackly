package hr.javafx.webtrackly.app.model;
import java.io.Serializable;

/**
 * Klasa koja predstavlja ulogu marketinga u aplikaciji WebTrackly.
 * Implementira sučelje Role i sadrži metodu za dobivanje dozvole.
 * Ova uloga omogućuje pristup analitici aplikacije.
 */

public final class MarketingRole implements Role, Serializable
{
    @Override
    public String getPermission() {
        return "VIEW_ANALYTICS";
    }

    @Override
    public String toString() {
        return "Marketing";
    }
}
