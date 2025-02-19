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

public class UserActionDbRepository2<T extends UserAction> {
    public void update(T entity){
        String query = "UPDATE USER_ACTION " +
                "SET USER_ID = ?, ACTION  = ?, WEBSITE_ID = ?, ACTION_TIMESTAMP = ?, " +
                "DETAILS  = ? WHERE ID = ?";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, entity.getUser().getId());
            stmt.setString(2, entity.getAction().name());
            stmt.setLong(3, entity.getPage().getId());
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getTimestamp()));
            stmt.setString(5, entity.getDetails());
            stmt.setLong(6, entity.getId());
            stmt.executeUpdate();

        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while updating user action in database: {}", e.getMessage());
            throw new RepositoryException("Error while updating user action in database");
        }
    }

    public void delete(Long id){
        try (Connection connection = DbActiveUtil.connectToDatabase()) {
            connection.setAutoCommit(false);
            performDeleteOperation(connection, id);
        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while deleting user action from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting user action from database");
        }
    }

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
