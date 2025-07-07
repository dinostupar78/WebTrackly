package hr.javafx.webtrackly.app.db;
import hr.javafx.webtrackly.app.enums.DeviceType;
import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;

/**
 * Klasa koja predstavlja repozitorij za upravljanje sesijama u bazi podataka.
 * Ova klasa omogućuje dohvat najčešće korištenog tipa uređaja i broj sesija po tipu uređaja.
 */

public class SessionDbRepository3 {

    private boolean dbLock = false;

    /**
     * Dohvaća najčešće korišteni tip uređaja iz baze podataka.
     * Ova metoda izvršava SQL upit koji grupira sesije po tipu uređaja,
     * broji ih i vraća tip uređaja koji se najčešće pojavljuje.
     * Ako nema sesija, vraća prazan Optional.
     *
     * @return Optional koji sadrži najčešće korišteni tip uređaja, ili prazan Optional ako nema sesija.
     * @throws RepositoryException ako dođe do greške pri dohvaćanju podataka iz baze.
     */

    public synchronized Optional<DeviceType> findMostFrequentDeviceType() {
        while (dbLock) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RepositoryException(e);
            }
        }
        dbLock = true;

        String sql =
                "SELECT DEVICE_TYPE " +
                        "FROM SESSION " +
                        "GROUP BY DEVICE_TYPE " +
                        "ORDER BY COUNT(*) DESC " +
                        "LIMIT 1";

        try (Connection conn = DbActiveUtil.connectToDatabase();
             Statement stmt   = conn.createStatement();
             ResultSet rs     = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return Optional.of(DeviceType.valueOf(rs.getString("DEVICE_TYPE")));
            }
            return Optional.empty();

        } catch (SQLException | IOException | DbConnectionException ex) {
            throw new RepositoryException("Error fetching most frequent device type", ex);
        } finally {
            dbLock = false;
            notifyAll();
        }
    }

    /**
     * Broji broj sesija po tipu uređaja.
     * Ova metoda izvršava SQL upit koji broji sve sesije za određeni tip uređaja.
     * Ako nema sesija za navedeni tip uređaja, vraća 0.
     *
     * @param device Tip uređaja za koji se broje sesije.
     * @return Broj sesija za navedeni tip uređaja.
     * @throws RepositoryException ako dođe do greške pri dohvaćanju podataka iz baze.
     */

    public synchronized int countByDeviceType(DeviceType device) {
        while (dbLock) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RepositoryException(e);
            }
        }
        dbLock = true;

        String sql = "SELECT COUNT(*) AS cnt FROM SESSION WHERE DEVICE_TYPE = ?";
        try (Connection conn = DbActiveUtil.connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, device.name());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("cnt") : 0;
            }

        } catch (SQLException|IOException|DbConnectionException ex) {
            throw new RepositoryException("Error counting sessions by device", ex);
        } finally {
            dbLock = false;
            notifyAll();
        }
    }
}
