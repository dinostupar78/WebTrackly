package hr.javafx.webtrackly.app.model;

public sealed interface Role permits AdminRole, MarketingRole, UserRole {
    String getPermission();
}
