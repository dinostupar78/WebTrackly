package hr.javafx.webtrackly.utils;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbActiveUtil {
    private DbActiveUtil() {}

    public static boolean isDatabaseOnline() {
        try (Connection connection = connectToDatabase()) {
            return true;
        } catch (IOException | SQLException e) {
            ShowAlertUtil.showAlert("Database Error", "The database is inactive. Please check your connection.", javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }
    }

    public static Connection connectToDatabase() throws IOException, SQLException {
        Properties props = new Properties();
        try (FileReader reader = new FileReader("C:\\Users\\Dino\\Desktop\\PROJEKT\\WebTrackly\\src\\main\\resources\\database.properties")) {
            props.load(reader);
        }
        return DriverManager.getConnection(
                props.getProperty("databaseUrl"),
                props.getProperty("username"),
                props.getProperty("password"));
    }



}
