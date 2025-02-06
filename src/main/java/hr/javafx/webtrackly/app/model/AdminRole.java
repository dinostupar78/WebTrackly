package hr.javafx.webtrackly.app.model;

public final class AdminRole implements Role{

    @Override
    public String getPermission() {
        return "FULL_ACCESS";
    }

    @Override
    public String toString() {
        return "AdminRole";
    }
}
