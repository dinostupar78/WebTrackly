package hr.javafx.webtrackly.app.db;
import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.UserAction;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import static hr.javafx.webtrackly.main.HelloApplication.log;

/**
 * Klasa koja predstavlja repozitorij za upravljanje korisničkim akcijama u bazi podataka.
 * Ova klasa omogućuje ažuriranje i brisanje korisničkih akcija.
 *
 * @param <T> Tip korisničke akcije koja se koristi u repozitoriju.
 */

public class UserActionDbRepository2<T extends UserAction> {
    /**
     * Ažurira korisničku akciju u bazi podataka.
     * Ova metoda koristi SQL upit za ažuriranje podataka o korisničkoj akciji u bazi podataka.
     * @param entity Korisnička akcija koja se ažurira.
     * @param entity
     */
    public void update(T entity){
        String query = "UPDATE USER_ACTION " +
                "SET USER_ID = ?, SESSION_ID = ?, ACTION  = ?, WEBSITE_ID = ?, ACTION_TIMESTAMP = ?, " +
                "DETAILS  = ? WHERE ID = ?";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, entity.getUser().getId());
            stmt.setLong(2, entity.getSession().getId());
            stmt.setString(3, entity.getAction().name());
            stmt.setLong(4, entity.getPage().getId());
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getTimestamp()));
            stmt.setString(6, entity.getDetails());
            stmt.setLong(7, entity.getId());
            stmt.executeUpdate();

        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while updating user action in database: {}", e.getMessage());
            throw new RepositoryException("Error while updating user action in database");
        }
    }

    /**
     * Briše korisničku akciju iz baze podataka.
     * Ova metoda koristi SQL upit za brisanje korisničke akcije na temelju ID-a.
     * @param id ID korisničke akcije koja se briše.
     */

    public void delete(Long id){
        try (Connection connection = DbActiveUtil.connectToDatabase()) {
            connection.setAutoCommit(false);
            performDeleteOperation(connection, id);
        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while deleting user action from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting user action from database");
        }
    }

    /**
     * Izvršava brisanje korisničke akcije u okviru transakcije.
     * Ova metoda koristi SQL upit za brisanje korisničke akcije i upravlja transakcijom.
     * @param connection Veza s bazom podataka.
     * @param id ID korisničke akcije koja se briše.
     */

    private void performDeleteOperation(Connection connection, Long id){
        try {
            executeDeleteUserQuery(connection, id);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                log.error("Rollback failed: {}", rollbackEx.getMessage());
                throw new RepositoryException("Rollback failed");
            }
            log.error("Error while deleting user action from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting user action from database");
        }
    }

    /**
     * Izvršava SQL upit za brisanje korisničke akcije.
     * Ova metoda koristi pripremljeni upit za brisanje korisničke akcije na temelju ID-a.
     * @param connection Veza s bazom podataka.
     * @param id ID korisničke akcije koja se briše.
     * @throws SQLException Ako dođe do greške pri izvršavanju SQL upita.
     */

    private void executeDeleteUserQuery(Connection connection, Long id) throws SQLException {
        String deleteUserQuery = "DELETE FROM USER_ACTION WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteUserQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while deleting user action from database: {}", e.getMessage());
        }
    }
}
