package hr.javafx.webtrackly.app.model;

public sealed interface Role permits AdminRole, MarketingRole {
    String getPermission();
}
