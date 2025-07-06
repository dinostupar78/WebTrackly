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

import static hr.javafx.webtrackly.main.HelloApplication.log;

public class SessionDbRepository2<T extends Session> {
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

    public void delete(Long id){
        try (Connection connection = DbActiveUtil.connectToDatabase()) {
            connection.setAutoCommit(false);
            performDeleteOperation(connection, id);
        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while deleting session from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting session from database");
        }
    }

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
