package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.DbActiveUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WebsiteDbRepository2<T extends Website> {
    private static boolean dbLock = false;

    public  void delete(Long id) throws RepositoryAccessException {
        try (Connection connection = DbActiveUtil.connectToDatabase()) {
            connection.setAutoCommit(false);
            performDeleteOperation(connection, id);
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private void performDeleteOperation(Connection connection, Long id) throws RepositoryAccessException {
        try {
            executeDeleteWebsiteQuery(connection, id);
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
        }
    }

    public void update(T entity) throws RepositoryAccessException {
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

        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }
}
