package hr.javafx.webtrackly.app.model;
import java.io.Serializable;

/**
 * Sučelje koje predstavlja ulogu korisnika u aplikaciji.
 * Sve uloge moraju implementirati ovo sučelje i pružiti svoju specifičnu logiku za dobivanje dozvole.
 */

public sealed interface Role extends Serializable permits AdminRole, MarketingRole, UserRole {
    String getPermission();
}
