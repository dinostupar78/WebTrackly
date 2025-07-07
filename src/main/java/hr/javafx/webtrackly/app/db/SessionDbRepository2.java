package hr.javafx.webtrackly.app.db;
import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.Session;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import static hr.javafx.webtrackly.main.App.log;

/**
 * Klasa koja predstavlja repozitorij za upravljanje sesijama u bazi podataka.
 * Ova klasa omogućuje ažuriranje i brisanje sesija.
 *
 * @param <T> Tip entiteta koji nasljeđuje klasu Session.
 */

public class SessionDbRepository2<T extends Session> {
    /**
     * Ažurira postojeću sesiju u bazi podataka.
     *
     * @param entity Entitet sesije koji se ažurira.
     * @throws RepositoryException Ako dođe do greške prilikom ažuriranja sesije u bazi podataka.
     */

    public void update(T entity){
        String query = "UPDATE SESSION " +
                "SET WEBSITE_ID = ?, USER_ID = ?, DEVICE_TYPE = ?, " +
                "START_TIME = ?, END_TIME = ?, IS_ACTIVE = ? WHERE ID = ?";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, entity.getWebsite().getId());
            stmt.setLong(2, entity.getUser().getId());
            stmt.setString(3, entity.getDeviceType().name());
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getStartTime()));
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getEndTime()));
            stmt.setBoolean(6, entity.getActive());
            stmt.setLong(7, entity.getId());

            stmt.executeUpdate();
        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while updating session in database: {}", e.getMessage());
            throw new RepositoryException("Error while updating session in database");
        }
    }

    /**
     * Briše sesiju iz baze podataka prema ID-u.
     *
     * @param id ID sesije koja se briše.
     * @throws RepositoryException Ako dođe do greške prilikom brisanja sesije iz baze podataka.
     */

    public void delete(Long id){
        try (Connection connection = DbActiveUtil.connectToDatabase()) {
            connection.setAutoCommit(false);
            performDeleteOperation(connection, id);
        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while deleting session from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting session from database");
        }
    }

    /**
     * Izvršava brisanje sesije iz baze podataka.
     *
     * @param connection Veza na bazu podataka.
     * @param id ID sesije koja se briše.
     * @throws RepositoryException Ako dođe do greške prilikom brisanja sesije iz baze podataka.
     */

    private void performDeleteOperation(Connection connection, Long id){
        try {
            executeDeleteSessionQuery(connection, id);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException f) {
                log.error("Rollback failed: {}", f.getMessage());
                throw new RepositoryException("Rollback failed");
            }
            log.error("Error while deleting session from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting session from database");
        }
    }

    /**
     * Izvršava SQL upit za brisanje sesije iz baze podataka.
     *
     * @param connection Veza na bazu podataka.
     * @param id ID sesije koja se briše.
     * @throws SQLException Ako dođe do greške prilikom izvršavanja SQL upita.
     */

    private void executeDeleteSessionQuery(Connection connection, Long id) throws SQLException {
        String deleteUserQuery = "DELETE FROM SESSION WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteUserQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while deleting session from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting session from database");
        }
    }
}
