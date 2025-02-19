package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.utils.DbActiveUtil;

import java.io.IOException;
import java.sql.*;

public class UserDbRepository2<T extends User> {
    public void update(T entity) throws RepositoryAccessException {
        String query = "UPDATE APP_USER " +
                "SET FIRST_NAME = ?, LAST_NAME = ?, DATE_OF_BIRTH = ?, NATIONALITY = ?, " +
                "GENDER_TYPE = ?, USERNAME = ?, HASHED_PASSWORD = ?, ROLE = ?, WEBSITE_ID = ?, CREATED_AT = ? " +
                "WHERE ID = ?";

        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, entity.getFirstName());
            stmt.setString(2, entity.getLastName());
            stmt.setDate(3, Date.valueOf(entity.getPersonalData().dateOfBirth()));
            stmt.setString(4, entity.getPersonalData().nationality());
            stmt.setString(5, entity.getPersonalData().gender().toString());
            stmt.setString(6, entity.getUsername());
            stmt.setString(7, entity.getHashedPassword());
            stmt.setString(8, entity.getRole().toString());
            stmt.setLong(9, entity.getWebsiteId());
            stmt.setTimestamp(10, Timestamp.valueOf(entity.getRegistrationDate()));
            stmt.setLong(11, entity.getId());
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
        String deleteUserActionQuery = "DELETE FROM USER_ACTION WHERE USER_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteUserActionQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }

        String deleteSessionQuery = "DELETE FROM SESSION WHERE USER_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSessionQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }

        String deleteUserQuery = "DELETE FROM APP_USER WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteUserQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

}
