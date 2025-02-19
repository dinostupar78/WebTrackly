package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.Session;
import hr.javafx.webtrackly.utils.DbActiveUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SessionDbRepository2<T extends Session> {
    public void update(T entity) throws RepositoryAccessException {
        String query = "UPDATE SESSION " +
                "SET WEBSITE_ID = ?, USER_ID = ?, DEVICE_TYPE = ?, SESSION_DURATION = ?, " +
                "START_TIME = ?, END_TIME = ?, IS_ACTIVE = ?, TRAFFIC_RECORD_ID = ? " +
                "WHERE ID = ?";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, entity.getWebsite().getId());
            stmt.setLong(2, entity.getUser().getId());
            stmt.setString(3, entity.getDeviceType().name());
            stmt.setBigDecimal(4, entity.getSessionDuration());
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getStartTime()));
            stmt.setTimestamp(6, Timestamp.valueOf(entity.getEndTime()));
            stmt.setBoolean(7, entity.getActive());
            stmt.setLong(8, entity.getTrafficRecordId());
            stmt.setLong(9, entity.getId());

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
            executeDeleteSessionQuery(connection, id);
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

    private void executeDeleteSessionQuery(Connection connection, Long id) throws SQLException {
        String deleteUserQuery = "DELETE FROM SESSION WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteUserQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
