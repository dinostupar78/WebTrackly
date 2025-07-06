package hr.javafx.webtrackly.app.model;

import java.io.Serializable;

public sealed interface Role extends Serializable permits AdminRole, MarketingRole, UserRole {
    String getPermission();
}
