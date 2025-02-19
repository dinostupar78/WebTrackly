package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.DbActiveUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static hr.javafx.webtrackly.main.HelloApplication.log;

public class WebsiteDbRepository2<T extends Website> {

    public  void delete(Long id){
        try (Connection connection = DbActiveUtil.connectToDatabase()) {
            connection.setAutoCommit(false);
            performDeleteOperation(connection, id);
        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while deleting website from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting website from database");
        }
    }

    private void performDeleteOperation(Connection connection, Long id) {
        try {
            executeDeleteWebsiteQuery(connection, id);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                log.error("Rollback failed: {}", rollbackEx.getMessage());
                throw new RepositoryException("Rollback failed");
            }
            log.error("Error while deleting website from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting website from database");
        }
    }

    private void executeDeleteWebsiteQuery(Connection connection, Long id) throws SQLException {
        try (PreparedStatement deleteUserActionStmt = connection.prepareStatement("DELETE FROM USER_ACTION WHERE WEBSITE_ID = ?");
             PreparedStatement deleteSessionStmt = connection.prepareStatement("DELETE FROM SESSION WHERE TRAFFIC_RECORD_ID IN (SELECT ID FROM TRAFFIC_RECORD WHERE WEBSITE_ID = ?)");
             PreparedStatement deleteTrafficRecordStmt = connection.prepareStatement("DELETE FROM TRAFFIC_RECORD WHERE WEBSITE_ID = ?");
             PreparedStatement deleteAppUserStmt = connection.prepareStatement("DELETE FROM APP_USER WHERE WEBSITE_ID = ?");
             PreparedStatement deleteWebsiteStmt = connection.prepareStatement("DELETE FROM WEBSITE WHERE ID = ?")) {

            deleteUserActionStmt.setLong(1, id);
            deleteUserActionStmt.executeUpdate();

            deleteSessionStmt.setLong(1, id);
            deleteSessionStmt.executeUpdate();

            deleteTrafficRecordStmt.setLong(1, id);
            deleteTrafficRecordStmt.executeUpdate();

            deleteAppUserStmt.setLong(1, id);
            deleteAppUserStmt.executeUpdate();

            deleteWebsiteStmt.setLong(1, id);
            deleteWebsiteStmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Error while deleting website from database: {}", e.getMessage());
            throw new RepositoryException("Error while deleting website from database");
        }
    }

    public void update(T entity) throws RepositoryException {
        String query = "UPDATE WEBSITE SET WEBSITE_NAME = ?, WEBSITE_CLICKS = ?, WEBSITE_URL = ?, WEBSITE_USER_COUNT = ?, " +
                "BOUNCE_RATE = ? WHERE ID = ?";

        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, entity.getWebsiteName());
            stmt.setInt(2, entity.getWebsiteClicks());
            stmt.setString(3, entity.getWebsiteUrl());
            stmt.setInt(4, entity.getWebsiteUserCount());
            stmt.setBigDecimal(5, entity.getBounceRate());
            stmt.setLong(6, entity.getId());
            stmt.executeUpdate();

        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error while updating website in database: {}", e.getMessage());
            throw new RepositoryException("Error while updating website in database");
        }
    }
}
