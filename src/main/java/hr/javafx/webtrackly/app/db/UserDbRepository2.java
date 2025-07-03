package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.utils.DbActiveUtil;

import java.io.IOException;
import java.sql.*;

import static hr.javafx.webtrackly.main.HelloApplication.log;

public class UserDbRepository2<T extends User> {
    public void update(T entity){
        String query = "UPDATE APP_USER " +
                "SET FIRST_NAME = ?, LAST_NAME = ?, DATE_OF_BIRTH = ?, NATIONALITY = ?, " +
                "GENDER_TYPE = ?, USERNAME = ?, EMAIL = ? ,PASSWORD = ?, ROLE = ?, WEBSITE_ID = ? " +
                "WHERE ID = ?";

        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, entity.getFirstName());
            stmt.setString(2, entity.getLastName());
            stmt.setDate(3, Date.valueOf(entity.getPersonalData().dateOfBirth()));
            stmt.setString(4, entity.getPersonalData().nationality());
            stmt.setString(5, entity.getPersonalData().gender().toString());
            stmt.setString(6, entity.getUsername());
            stmt.setString(7, entity.getEmail());
            stmt.setString(8, entity.getPassword());
            stmt.setString(9, entity.getRole().toString());
            stmt.setLong(10, entity.getWebsiteId());
            stmt.setLong(11, entity.getId());
            stmt.executeUpdate();

        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while updating user in database: {}", e.getMessage());
            throw new RepositoryException("Error while updating user in database");
        }
    }

    public void delete(Long id) {
        try (Connection connection = DbActiveUtil.connectToDatabase()) {
            connection.setAutoCommit(false);
            performDeleteOperation(connection, id);
        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while deleting user from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting user from database");
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
            log.error("Error while deleting user from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting user from database");
        }
    }

    private void executeDeleteUserQuery(Connection connection, Long id) throws SQLException {
        String deleteUserActionQuery = "DELETE FROM USER_ACTION WHERE USER_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteUserActionQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while deleting user actions from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting user actions from database");
        }

        String deleteSessionQuery = "DELETE FROM SESSION WHERE USER_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSessionQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while deleting user sessions from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting user sessions from database");
        }

        String deleteUserQuery = "DELETE FROM APP_USER WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteUserQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while deleting user from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting user from database");
        }
    }

}
