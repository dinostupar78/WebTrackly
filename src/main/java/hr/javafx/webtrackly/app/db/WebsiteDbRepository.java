package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.Website;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class WebsiteDbRepository<T extends Website> extends AbstractDbRepository<T> {
    private static final String FIND_BY_ID_QUERY =
            "SELECT ID, WEBSITE_NAME, WEBSITE_CLICKS, WEBSITE_URL, WEBSITE_USER_COUNT, BOUNCE_RATE FROM WEBSITE WHERE ID = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT ID, WEBSITE_NAME, WEBSITE_CLICKS, WEBSITE_URL, WEBSITE_USER_COUNT, BOUNCE_RATE FROM WEBSITE";

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
                    return (T) extractWebsiteFromResultSet(resultSet);
                } else {
                    throw new RepositoryAccessException("Website with id " + id + " not found!");
                }
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }


    @Override
    public List<T> findAll() throws RepositoryAccessException {
        List<T> websites = new ArrayList<>();
        try (Connection connection = connectToDatabase();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(FIND_ALL_QUERY)) {

            while (resultSet.next()) {
                websites.add((T) extractWebsiteFromResultSet(resultSet));
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
        return websites;
    }

    @Override
    public void save(List<T> entities) throws RepositoryAccessException {
        String sql = "INSERT INTO WEBSITE (WEBSITE_NAME, WEBSITE_CLICKS, WEBSITE_URL, WEBSITE_USER_COUNT, BOUNCE_RATE) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (T entity : entities) {
                stmt.setString(1, entity.getWebsiteName());
                stmt.setInt(2, entity.getWebsiteClicks());
                stmt.setString(3, entity.getWebsiteUrl());
                stmt.setInt(4, entity.getWebsiteUserCount());
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
        String sql = "INSERT INTO WEBSITE (WEBSITE_NAME, WEBSITE_CLICKS, WEBSITE_URL, WEBSITE_USER_COUNT, BOUNCE_RATE) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, entity.getWebsiteName());
            stmt.setInt(2, entity.getWebsiteClicks());
            stmt.setString(3, entity.getWebsiteUrl());
            stmt.setInt(4, entity.getWebsiteUserCount());
            stmt.setBigDecimal(5, entity.getBounceRate());
            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RepositoryAccessException(e);
        }
    }


    private static Website extractWebsiteFromResultSet(ResultSet resultSet) {
        try {
            Long id = resultSet.getLong("ID");
            String websiteName = resultSet.getString("WEBSITE_NAME");
            Integer websiteClicks = resultSet.getInt("WEBSITE_CLICKS");
            String websiteUrl = resultSet.getString("WEBSITE_URL");
            Integer websiteUserCount = resultSet.getInt("WEBSITE_USER_COUNT");
            BigDecimal bounceRate = resultSet.getBigDecimal("BOUNCE_RATE");

            // Retrieve the users for this website from APP_USER using the WEBSITE_ID column.
            Set<User> users = fetchUsersForWebsite(id);

            return new Website(id, websiteName, websiteClicks, websiteUrl, websiteUserCount, bounceRate, users);
        } catch (SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }


    private static Set<User> fetchUsersForWebsite(Long websiteId) {
        Set<User> users = new HashSet<>();
        String query = "SELECT * FROM APP_USER WHERE WEBSITE_ID = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, websiteId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    User user = UserDbRepository.extractUserFromResultSet(resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException | IOException e) {
            throw new RepositoryAccessException("Error fetching users for website ID: " + websiteId, e);
        }
        return users;
    }
}
