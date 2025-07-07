package hr.javafx.webtrackly.utils;

import hr.javafx.webtrackly.app.exception.DbConnectionException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

import static hr.javafx.webtrackly.main.App.log;

public class DbActiveUtil {
    private DbActiveUtil() {}

    /**
     * Provjerava je li baza podataka dostupna.
     * Pokušava se povezati na bazu podataka koristeći konfiguraciju iz datoteke database.properties.
     * Ako je veza uspješna, vraća true, inače vraća false i ispisuje grešku u log.
     *
     * @return boolean - true ako je baza online, false inače
     */

    public static boolean isDatabaseOnline() {
        try (Connection connection = connectToDatabase()) {
            log.info("Database is online.");
            return true;
        } catch (DbConnectionException | IOException | SQLException e) {
            log.error("Database is offline.", e);
            return false;
        }
    }

    /**
     * Povezuje se na bazu podataka koristeći konfiguraciju iz datoteke database.properties.
     * Ako dođe do greške prilikom čitanja datoteke ili uspostavljanja veze, baca DbConnectionException.
     *
     * @return Connection - objekt koji predstavlja vezu s bazom podataka
     * @throws DbConnectionException ako dođe do greške prilikom povezivanja na bazu
     * @throws IOException ako dođe do greške prilikom čitanja datoteke
     * @throws SQLException ako dođe do SQL greške prilikom uspostavljanja veze
     */

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

    /**
     * Zatvara vezu s bazom podataka ako je otvorena.
     * Ako dođe do greške prilikom zatvaranja veze, ispisuje grešku u log.
     *
     * @param connection - objekt koji predstavlja vezu s bazom podataka
     */

    public static void disconnectFromDatabase(Connection connection) {
        Optional.ofNullable(connection).ifPresent(conn -> {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    log.info("Disconnected from database.");
                }
            } catch (SQLException e) {
                log.error("Error while disconnecting from database.", e);
            }
        });
    }
}
