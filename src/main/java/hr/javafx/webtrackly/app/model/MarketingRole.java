package hr.javafx.webtrackly.app.model;

public final class MarketingRole implements Role {
    @Override
    public String getPermission() {
        return "VIEW_ANALYTICS";
    }

    @Override
    public String toString() {
        return "Marketing";
    }
}
