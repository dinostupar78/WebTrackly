package hr.javafx.webtrackly.app.db;
import hr.javafx.webtrackly.app.enums.BehaviourType;
import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;

/**
 * Klasa koja predstavlja repozitorij za rad s korisničkim akcijama u bazi podataka.
 * Ova klasa omogućuje dohvat najčešće korištene akcije i brojanje akcija po vrsti.
 * Implementira sinkronizaciju kako bi se spriječili problemi s višestrukim pristupom bazi podataka.
 */

public class UserActionDbRepository3 {

    private boolean dbLock = false;

    /**
     * Dohvaća najčešće korištenu akciju iz baze podataka.
     * @return Optional koji sadrži najčešću akciju ako postoji, inače prazan Optional.
     * @throws RepositoryException ako dođe do greške pri radu s bazom podataka.
     */

    public synchronized Optional<BehaviourType> findMostFrequentAction() {
        while (dbLock) {
            try { wait(); }
            catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RepositoryException(ie);
            }
        }
        dbLock = true;

        String sql = "SELECT ACTION, COUNT(*) AS cnt " +
                        "FROM USER_ACTION " +
                        "GROUP BY ACTION " +
                        "ORDER BY cnt DESC " +
                        "LIMIT 1";

        try (Connection conn = DbActiveUtil.connectToDatabase();
             Statement stmt   = conn.createStatement();
             ResultSet rs     = stmt.executeQuery(sql)) {

            if (rs.next()) {
                BehaviourType bt = BehaviourType.valueOf(rs.getString("ACTION"));
                return Optional.of(bt);
            }
            return Optional.empty();

        } catch (SQLException | IOException | DbConnectionException ex) {
            throw new RepositoryException("Error fetching most frequent action", ex);
        } finally {
            dbLock = false;
            notifyAll();
        }
    }

    /**
     * Broji koliko puta je određena akcija izvršena u bazi podataka.
     * @param action Vrsta akcije za koju se broji broj izvršenja.
     * @return Broj izvršenja navedene akcije.
     * @throws RepositoryException ako dođe do greške pri radu s bazom podataka.
     */

    public synchronized Integer countByAction(BehaviourType action) {
        while (dbLock) {
            try { wait(); }
            catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RepositoryException(ie);
            }
        }
        dbLock = true;

        String sql = "SELECT COUNT(*) AS cnt FROM USER_ACTION WHERE ACTION = ?";
        try (Connection conn = DbActiveUtil.connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, action.name());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("cnt") : 0;
            }

        } catch (SQLException|IOException|DbConnectionException ex) {
            throw new RepositoryException("Error counting actions", ex);
        } finally {
            dbLock = false;
            notifyAll();
        }
    }

}
