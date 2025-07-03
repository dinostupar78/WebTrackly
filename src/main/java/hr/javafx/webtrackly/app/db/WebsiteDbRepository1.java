package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.enums.WebsiteType;
import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.DbDataException;
import hr.javafx.webtrackly.app.exception.EmptyResultSetException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.DbActiveUtil;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static hr.javafx.webtrackly.main.HelloApplication.log;

public class WebsiteDbRepository1<T extends Website> extends AbstractDbRepository<T> {
    private static final String FIND_BY_ID_QUERY =
            "SELECT ID, WEBSITE_NAME, WEBSITE_URL, WEBSITE_CATEGORY, WEBSITE_DESCRIPTION FROM WEBSITE WHERE ID = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT ID, WEBSITE_NAME, WEBSITE_URL, WEBSITE_CATEGORY, WEBSITE_DESCRIPTION FROM WEBSITE";

    private static boolean dbLock = false;

    @Override
    public synchronized T findById(Long id){
        while (dbLock) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RepositoryException(e);
            }
        }
        dbLock = true;

        if (!DbActiveUtil.isDatabaseOnline()) {
            log.error("Database is inactive. Please check your connection.");
            throw new RepositoryException("Database is inactive. Please check your connection.");
        }
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            stmt.setLong(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return (T) extractWebsiteFromResultSet(resultSet);
                } else {
                    throw new EmptyResultSetException("Website with id " + id + " not found!");
                }
            }
        } catch (IOException | SQLException | DbConnectionException | DbDataException e) {
            log.error("Error while fetching website from database: {}", e.getMessage());
            throw new RepositoryException("Error while fetching website from database");
        } finally {
            dbLock = false;
            notifyAll();
        }
    }

    @Override
    public synchronized List<T> findAll(){
        while (dbLock) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Error while fetching websites from database: {}", e.getMessage());
                throw new RepositoryException("Error while fetching websites from database");
            }
        }
        dbLock = true;

        if (!(DbActiveUtil.isDatabaseOnline())) {
            throw new RepositoryException("Database is inactive. Please check your connection.");
        }
        List<T> websites = new ArrayList<>();
        try (Connection connection = DbActiveUtil.connectToDatabase();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(FIND_ALL_QUERY)) {

            while (resultSet.next()) {
                websites.add((T) extractWebsiteFromResultSet(resultSet));
            }
        } catch (IOException | SQLException | DbConnectionException | DbDataException e) {
            log.error("Error while fetching websites from database: {}", e.getMessage());
            throw new RepositoryException("Error while fetching websites from database");
        } finally {
            dbLock = false;
            notifyAll();
        }
        return websites;
    }

    @Override
    public synchronized void save(List<T> entities){
        String sql = "INSERT INTO WEBSITE (WEBSITE_NAME, WEBSITE_URL, WEBSITE_CATEGORY, WEBSITE_DESCRIPTION) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (T entity : entities) {
                stmt.setString(1, entity.getWebsiteName());
                stmt.setString(2, entity.getWebsiteUrl());
                stmt.setString(3, entity.getWebsiteCategory().name());
                stmt.setString(4, entity.getWebsiteDescription());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException | IOException | DbConnectionException e) {
            log.error("Error while saving websites to database: {}", e.getMessage());
            throw new RepositoryException("Error while saving websites to database");
        }
    }

    @Override
    public synchronized void save(T entity){
        String sql = "INSERT INTO WEBSITE (WEBSITE_NAME, WEBSITE_URL, WEBSITE_CATEGORY, WEBSITE_DESCRIPTION) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, entity.getWebsiteName());
            stmt.setString(2, entity.getWebsiteUrl());
            stmt.setString(3, entity.getWebsiteCategory().name());
            stmt.setString(4, entity.getWebsiteDescription());
            stmt.executeUpdate();
        } catch (SQLException | IOException | DbConnectionException e) {
            log.error("Error while saving website to database: {}", e.getMessage());
            throw new RepositoryException("Error while saving website to database");
        }
    }


    private static Website extractWebsiteFromResultSet(ResultSet resultSet) throws SQLException, DbDataException {
        Long id = resultSet.getLong("ID");
        String websiteName = resultSet.getString("WEBSITE_NAME");
        String websiteUrl = resultSet.getString("WEBSITE_URL");

        String websiteCat = resultSet.getString("WEBSITE_CATEGORY");
        WebsiteType websiteType;

        try{
            websiteType = WebsiteType.valueOf(websiteCat.toUpperCase());
        } catch (IllegalArgumentException e){
            log.error("Device type not found! {}", websiteCat);
            throw new DbDataException("Device type not found!" + websiteCat);
        }
        String websiteDescription = resultSet.getString("WEBSITE_DESCRIPTION");
        Set<User> users = fetchUsersForWebsite(id);

        return new Website.Builder()
                .setId(id)
                .setWebsiteName(websiteName)
                .setWebsiteUrl(websiteUrl)
                .setWebsiteCategory(websiteType)
                .setWebsiteDescription(websiteDescription)
                .setUsers(users)
                .build();

    }

    private static Set<User> fetchUsersForWebsite(Long websiteId) {
        Set<User> users = new HashSet<>();
        String query = "SELECT ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, " +
                "GENDER_TYPE, USERNAME, EMAIL, PASSWORD, ROLE, WEBSITE_ID " +
                "FROM APP_USER WHERE WEBSITE_ID = ?";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, websiteId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    User user = UserDbRepository1.extractUserFromResultSet(resultSet);
                    users.add(user);
                }
            }
        } catch (DbDataException | SQLException | IOException | DbConnectionException e) {
            log.error("Error fetching users for website ID: {}", websiteId);
            throw new RepositoryException("Error fetching users for website ID: " + websiteId, e);
        }
        return users;
    }

}
