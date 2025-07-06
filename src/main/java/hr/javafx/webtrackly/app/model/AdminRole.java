package hr.javafx.webtrackly.app.model;

import java.io.Serializable;

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
