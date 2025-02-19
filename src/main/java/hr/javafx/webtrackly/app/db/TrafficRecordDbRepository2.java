package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.TrafficRecord;
import hr.javafx.webtrackly.utils.DbActiveUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TrafficRecordDbRepository2<T extends TrafficRecord> {
    public void update(T entity) throws RepositoryAccessException {
        String query = "UPDATE TRAFFIC_RECORD " +
                "SET WEBSITE_ID = ?, TIME_OF_VISIT = ?, USER_COUNT = ?, PAGE_VIEWS = ?, " +
                "BOUNCE_RATE = ? WHERE ID = ?";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, entity.getWebsite().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(entity.getTimeOfVisit()));
            stmt.setInt(3, entity.getUserCount());
            stmt.setInt(4, entity.getPageViews());
            stmt.setBigDecimal(5, entity.getBounceRate());
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
        String deleteSessionsQuery = "DELETE FROM SESSION WHERE TRAFFIC_RECORD_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSessionsQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }

        String deleteTrafficRecordQuery = "DELETE FROM TRAFFIC_RECORD WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteTrafficRecordQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
