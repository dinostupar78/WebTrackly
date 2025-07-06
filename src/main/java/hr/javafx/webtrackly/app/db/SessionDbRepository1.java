package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.enums.DeviceType;
import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.DbDataException;
import hr.javafx.webtrackly.app.exception.EmptyResultSetException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.Session;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static hr.javafx.webtrackly.main.HelloApplication.log;
import static hr.javafx.webtrackly.utils.ShowAlertUtil.showAlert;

public class SessionDbRepository1<T extends Session> extends AbstractDbRepository<T> {
    private static final String FIND_BY_ID_QUERY =
            "SELECT ID, WEBSITE_ID, USER_ID, DEVICE_TYPE, START_TIME, END_TIME, IS_ACTIVE FROM SESSION WHERE ID = ?";

    private static final String FIND_ALL_QUERY =
            "SELECT ID, WEBSITE_ID, USER_ID, DEVICE_TYPE, START_TIME, END_TIME, IS_ACTIVE FROM SESSION";


    @Override
    public T findById(Long id) {
        if (!DbActiveUtil.isDatabaseOnline()) {
            log.error("Database is inactive. Please check your connection.");
            showAlert("Database error", "Database is inactive. Please check your connection.", Alert.AlertType.ERROR);
        }

        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            stmt.setLong(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return (T) extractFromSessionResultSet(resultSet);
                } else {
                    log.error("Session with id {} not found! ", id);
                    throw new EmptyResultSetException("Session with id not found!");
                }
            }
        } catch (IOException | SQLException | DbConnectionException | DbDataException e) {
            log.error("Error while fetching session from database: {}", e.getMessage());
            throw new RepositoryException("Error while fetching session from database");
        }
    }

    @Override
    public List<T> findAll()  {
        if (!DbActiveUtil.isDatabaseOnline()) {
            log.error("Database is inactive. Please check your connection.");
            showAlert("Database error", "Database is inactive. Please check your connection.", Alert.AlertType.ERROR);
        }

        List<T> sessions = new ArrayList<>();
        try (Connection connection = DbActiveUtil.connectToDatabase();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(FIND_ALL_QUERY)) {

            while (resultSet.next()) {
                sessions.add((T) extractFromSessionResultSet(resultSet));
            }
        } catch (IOException | SQLException | DbConnectionException | DbDataException e) {
            log.error("Error while fetching sessions from database: {}", e.getMessage());
            throw new RepositoryException("Error while fetching sessions from database");
        }
        return sessions;
    }

    @Override
    public void save(List<T> entities) {
        String sql = "INSERT INTO SESSION (WEBSITE_ID, USER_ID, DEVICE_TYPE, START_TIME, END_TIME, IS_ACTIVE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (T entity : entities) {
                stmt.setLong(1, entity.getWebsite().getId());
                stmt.setLong(2, entity.getUser().getId());
                stmt.setString(3, entity.getDeviceType().name());
                stmt.setTimestamp(4, Timestamp.valueOf(entity.getStartTime()));
                stmt.setTimestamp(5, Timestamp.valueOf(entity.getEndTime()));
                stmt.setBoolean(6, entity.getActive());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException | IOException | DbConnectionException e) {
            log.error("Error while saving sessions to database: {}", e.getMessage());
            throw new RepositoryException("Error while saving sessions to database");
        }

    }

    @Override
    public void save(T entity) {
        String sql = "INSERT INTO SESSION (WEBSITE_ID, USER_ID, DEVICE_TYPE, START_TIME, END_TIME, IS_ACTIVE) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getWebsite().getId());
            stmt.setLong(2, entity.getUser().getId());
            stmt.setString(3, entity.getDeviceType().name());
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getStartTime()));
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getEndTime()));
            stmt.setBoolean(6, entity.getActive());
            stmt.executeUpdate();

        } catch (SQLException | IOException | DbConnectionException e) {
            log.error("Error while saving session to database: {}", e.getMessage());
            throw new RepositoryException("Error while saving session to database");
        }

    }

    public static Session extractFromSessionResultSet(ResultSet resultSet) throws SQLException, DbDataException {
        Long id = resultSet.getLong("ID");

        Long userId = resultSet.getLong("USER_ID");
        UserDbRepository1<User> userRepository = new UserDbRepository1<>();
        User user = userRepository.findById(userId);

        Long websiteId = resultSet.getLong("WEBSITE_ID");
        WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
        Website website = websiteRepository.findById(websiteId);

        String deviceTypeStr = resultSet.getString("DEVICE_TYPE");
        DeviceType deviceType;

        try{
            deviceType = DeviceType.valueOf(deviceTypeStr.toUpperCase());
        } catch (IllegalArgumentException e){
            log.error("Device type not found! {}", deviceTypeStr);
            throw new DbDataException("Device type not found!" + deviceTypeStr);
        }

        LocalDateTime startTime = resultSet.getTimestamp("START_TIME").toLocalDateTime();
        LocalDateTime endTime = resultSet.getTimestamp("END_TIME").toLocalDateTime();
        Boolean active = resultSet.getObject("IS_ACTIVE") != null ? resultSet.getBoolean("IS_ACTIVE") : null;

        return new Session.Builder()
                .setId(id)
                .setUser(user)
                .setWebsite(website)
                .setDeviceType(deviceType)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setActive(active)
                .build();

    }

}
