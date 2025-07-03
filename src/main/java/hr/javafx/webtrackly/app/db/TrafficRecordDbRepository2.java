package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.TrafficRecord;
import hr.javafx.webtrackly.utils.DbActiveUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import static hr.javafx.webtrackly.main.HelloApplication.log;

public class TrafficRecordDbRepository2<T extends TrafficRecord> {
    public void update(T entity){
        String query = "UPDATE TRAFFIC_RECORD " +
                "SET WEBSITE_ID = ?, TIME_OF_VISIT = ? WHERE ID = ?";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, entity.getWebsite().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(entity.getTimeOfVisit()));
            stmt.setLong(3, entity.getId());

            stmt.executeUpdate();
        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while updating traffic record: {} ", e.getMessage());
            throw new RepositoryException("Error while updating traffic record");
        }
    }

    public void delete(Long id){
        try (Connection connection = DbActiveUtil.connectToDatabase()) {
            connection.setAutoCommit(false);
            performDeleteOperation(connection, id);
        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while deleting traffic record: {} ", e.getMessage());
            throw new RepositoryException("Error while deleting traffic record");
        }
    }

    private void performDeleteOperation(Connection connection, Long id){
        try {
            executeDeleteSessionQuery(connection, id);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                log.error("Error while rolling back transaction: {}", rollbackEx.getMessage());
                throw new RepositoryException("Error while rolling back transaction");
            }
            throw new RepositoryException(e);
        }
    }

    private void executeDeleteSessionQuery(Connection connection, Long id) throws SQLException {
        String deleteSessionsQuery = "DELETE FROM SESSION WHERE TRAFFIC_RECORD_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSessionsQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while deleting sessions: {}", e.getMessage());
        }

        String deleteTrafficRecordQuery = "DELETE FROM TRAFFIC_RECORD WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteTrafficRecordQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while deleting traffic record: {}", e.getMessage());
        }
    }
}
