package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.enums.BehaviorType;
import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.UserAction;
import hr.javafx.webtrackly.app.model.Website;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserActionDbRepository<T extends UserAction> extends AbstractDbRepository<T> {
    private static final String FIND_BY_ID_QUERY =
            "SELECT ID, USER_ID, ACTION , WEBSITE_ID, ACTION_TIMESTAMP, DETAILS FROM USER_ACTION WHERE ID = ?";

    private static final String FIND_ALL_QUERY =
            "SELECT ID, USER_ID, ACTION , WEBSITE_ID, ACTION_TIMESTAMP, DETAILS FROM USER_ACTION";

    private static Connection connectToDatabase() throws IOException, SQLException {
        Properties props = new Properties();
        try (FileReader reader = new FileReader("C:\\Users\\Dino\\Desktop\\PROJEKT\\WebTrackly\\src\\main\\resources\\database.properties")) {
            props.load(reader);
        }
        return DriverManager.getConnection(
                props.getProperty("databaseUrl"),
                props.getProperty("username"),
                props.getProperty("password"));
    }


    @Override
    public T findById(Long id) throws RepositoryAccessException {
        try (Connection connection = connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            stmt.setLong(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return (T) extractFromUserActionResultSet(resultSet);
                } else {
                    throw new RepositoryAccessException("User Action with id " + id + " not found!");
                }
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public List<T> findAll() throws RepositoryAccessException {
        List<T> actions = new ArrayList<>();
        try (Connection connection = connectToDatabase();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(FIND_ALL_QUERY)) {

            while (resultSet.next()) {
                actions.add((T) extractFromUserActionResultSet(resultSet));
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
        return actions;
    }

    @Override
    public void save(List<T> entities) throws RepositoryAccessException {
        String sql = "INSERT INTO USER_ACTION (USER_ID, ACTION , WEBSITE_ID, ACTION_TIMESTAMP, DETAILS) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (T entity : entities) {
                stmt.setLong(1, entity.getUser().getId());
                stmt.setString(2, entity.getAction().name());
                stmt.setString(3, entity.getPage().getWebsiteName());
                stmt.setTimestamp(4, Timestamp.valueOf(entity.getTimestamp()));
                stmt.setString(5, entity.getDetails());

                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException | IOException e) {
            throw new RepositoryAccessException(e);
        }

    }

    @Override
    public void save(T entity) throws RepositoryAccessException {
        String sql = "INSERT INTO USER_ACTION (USER_ID, ACTION , WEBSITE_ID, ACTION_TIMESTAMP, DETAILS) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getUser().getId());
            stmt.setString(2, entity.getAction().name());
            stmt.setString(3, entity.getPage().getWebsiteName());
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getTimestamp()));
            stmt.setString(5, entity.getDetails());
            stmt.executeUpdate();

        } catch (SQLException | IOException e) {
            throw new RepositoryAccessException(e);
        }

    }

    private static UserAction extractFromUserActionResultSet(ResultSet resultSet){
        try {
            Long id = resultSet.getLong("ID");

            Long userId = resultSet.getLong("USER_ID");
            UserDbRepository<User> userRepository = new UserDbRepository<>();
            User user = userRepository.findById(userId);

            String action = resultSet.getString("ACTION");
            BehaviorType behaviorType = BehaviorType.valueOf(action.toUpperCase());

            Long websiteId = resultSet.getLong("WEBSITE_ID");
            WebsiteDbRepository<Website> websiteRepository = new WebsiteDbRepository<>();
            Website website = websiteRepository.findById(websiteId);

            LocalDateTime startTime = resultSet.getTimestamp("ACTION_TIMESTAMP").toLocalDateTime();

            String details = resultSet.getString("DETAILS");

            return new UserAction(id, user, behaviorType, website, startTime, details);

        } catch (SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }
}
