package hr.javafx.webtrackly.app.model;
import java.io.Serializable;

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
