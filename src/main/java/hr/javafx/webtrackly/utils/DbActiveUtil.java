package hr.javafx.webtrackly.utils;

import hr.javafx.webtrackly.app.exception.DbConnectionException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static hr.javafx.webtrackly.main.HelloApplication.log;

public class DbActiveUtil {
    private DbActiveUtil() {}

    public static boolean isDatabaseOnline() {
        try (Connection connection = connectToDatabase()) {
            log.info("Database is online.");
            return true;
        } catch (DbConnectionException | IOException | SQLException e) {
            log.error("Database is offline.", e);
            return false;
        }
    }

    public static Connection connectToDatabase() throws DbConnectionException, IOException, SQLException {
        Properties props = new Properties();
        try (FileReader reader = new FileReader("C:\\Users\\Dino\\Desktop\\PROJEKT\\WebTrackly\\src\\main\\resources\\database.properties")) {
            props.load(reader);
        } catch (IOException e) {
            log.error("Error reading database properties file.", e);
            throw new DbConnectionException("Error reading database properties file.");
        }
        return DriverManager.getConnection(
                props.getProperty("databaseUrl"),
                props.getProperty("username"),
                props.getProperty("password"));
    }


}
