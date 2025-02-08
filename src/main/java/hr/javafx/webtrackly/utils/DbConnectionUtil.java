package hr.javafx.webtrackly.utils;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnectionUtil {
    private static Connection connection;

    private DbConnectionUtil() {}

    public static Connection getInstance() throws SQLException, IOException {
        Properties props = new Properties();

        try (FileReader fileReader = new FileReader("C:\\Users\\Dino\\Desktop\\PROJEKT\\WebTrackly\\src\\main\\resources\\database.properties")) {
            props.load(fileReader);
        }
        connection = DriverManager.getConnection(
                props.getProperty("databaseUrl"),
                props.getProperty("username"),
                props.getProperty("password"));


        return connection;
    }


    public void disconnectFromDatabase(Connection connection) throws SQLException {
        connection.close();
    }



}
