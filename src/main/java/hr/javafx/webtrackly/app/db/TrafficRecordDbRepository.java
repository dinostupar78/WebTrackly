package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.Session;
import hr.javafx.webtrackly.app.model.TrafficRecord;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.DbActiveUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrafficRecordDbRepository<T extends TrafficRecord> extends AbstractDbRepository<T> {
    private static final String FIND_BY_ID_QUERY = "SELECT ID, WEBSITE_ID, TIME_OF_VISIT , USER_COUNT, PAGE_VIEWS, BOUNCE_RATE FROM TRAFFIC_RECORD WHERE ID = ?";

    private static final String FIND_ALL_QUERY = "SELECT ID, WEBSITE_ID, TIME_OF_VISIT , USER_COUNT, PAGE_VIEWS, BOUNCE_RATE FROM TRAFFIC_RECORD";

    @Override
    public T findById(Long id) throws RepositoryAccessException {
        if (!DbActiveUtil.isDatabaseOnline()) {
            throw new RepositoryAccessException("Database is inactive. Please check your connection.");
        }
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            stmt.setLong(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return (T) extractTrafficRecordFromResultSet(resultSet);
                } else {
                    throw new RepositoryAccessException("Traffic record with id " + id + " not found!");
                }
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public List<T> findAll() throws RepositoryAccessException {
        if (!(DbActiveUtil.isDatabaseOnline())) {
            return List.of();
        }
        List<T> trafficRecords = new ArrayList<>();
        try (Connection connection = DbActiveUtil.connectToDatabase();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(FIND_ALL_QUERY)) {

            while (resultSet.next()) {
                trafficRecords.add((T) extractTrafficRecordFromResultSet(resultSet));
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
        return trafficRecords;
    }

    @Override
    public void save(List<T> entities) throws RepositoryAccessException {
        String sql = "INSERT INTO TRAFFIC_RECORD (WEBSITE_ID, TIME_OF_VISIT , USER_COUNT, PAGE_VIEWS, BOUNCE_RATE) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (T entity : entities) {
                stmt.setLong(1, entity.getWebsite().getId());
                stmt.setTimestamp(2, Timestamp.valueOf(entity.getTimeOfVisit()));
                stmt.setInt(3, entity.getUserCount());
                stmt.setInt(4, entity.getPageViews());
                stmt.setBigDecimal(5, entity.getBounceRate());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException | IOException e) {
            throw new RepositoryAccessException(e);
        }

    }

    @Override
    public void save(T entity) throws RepositoryAccessException {
        String sql = "INSERT INTO TRAFFIC_RECORD (WEBSITE_ID, TIME_OF_VISIT , USER_COUNT, PAGE_VIEWS, BOUNCE_RATE) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getWebsite().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(entity.getTimeOfVisit()));
            stmt.setInt(3, entity.getUserCount());
            stmt.setInt(4, entity.getPageViews());
            stmt.setBigDecimal(5, entity.getBounceRate());
            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RepositoryAccessException(e);
        }

    }

    private static TrafficRecord extractTrafficRecordFromResultSet(ResultSet resultSet){
        try{
            Long id = resultSet.getLong("ID");

            Long websiteId = resultSet.getLong("WEBSITE_ID");
            WebsiteDbRepository<Website> websiteRepository = new WebsiteDbRepository<>();
            Website website = websiteRepository.findById(websiteId);

            LocalDateTime timeOfVisit = resultSet.getTimestamp("TIME_OF_VISIT").toLocalDateTime();
            Integer userCount = resultSet.getInt("USER_COUNT");
            Integer pageViews = resultSet.getInt("PAGE_VIEWS");
            BigDecimal bounceRate = resultSet.getBigDecimal("BOUNCE_RATE");

            List<Session> sessions = fetchSessionsForTrafficRecord(id);

            return new TrafficRecord(id, website, timeOfVisit, userCount, pageViews, bounceRate, sessions);


        } catch (SQLException e){
            throw new RepositoryAccessException(e);
        }

    }

    private static List<Session> fetchSessionsForTrafficRecord(Long trafficRecordId){
        List<Session> sessions = new ArrayList<>();
        String query = "SELECT * FROM SESSION WHERE TRAFFIC_RECORD_ID = ?";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, trafficRecordId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    Session session = SessionDbRepository.extractFromSessionResultSet(resultSet);
                    sessions.add(session);
                }
            }
        } catch (SQLException | IOException e) {
            throw new RepositoryAccessException("Error fetching users for website ID: " + trafficRecordId, e);
        }
        return sessions;
    }

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
