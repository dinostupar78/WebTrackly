package hr.javafx.webtrackly.app.model;

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
