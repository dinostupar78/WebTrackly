package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.UserAction;
import hr.javafx.webtrackly.utils.DbActiveUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserActionDbRepository2<T extends UserAction> {
    public void update(T entity) throws RepositoryAccessException {
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

        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    public void delete(Long id) throws RepositoryAccessException {
        try (Connection connection = DbActiveUtil.connectToDatabase()) {
            connection.setAutoCommit(false);
            performDeleteOperation(connection, id);
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private void performDeleteOperation(Connection connection, Long id) throws RepositoryAccessException {
        try {
            executeDeleteUserQuery(connection, id);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RepositoryAccessException("Rollback failed: " + rollbackEx.getMessage(), rollbackEx);
            }
            throw new RepositoryAccessException(e);
        }
    }

    private void executeDeleteUserQuery(Connection connection, Long id) throws SQLException {
        String deleteUserQuery = "DELETE FROM USER_ACTION WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteUserQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
