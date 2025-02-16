package hr.javafx.webtrackly.app.db;
import hr.javafx.webtrackly.app.enums.GenderType;
import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static hr.javafx.webtrackly.main.HelloApplication.log;

public class UserDbRepository<T extends User> extends AbstractDbRepository<T> {
    private static final String FIND_BY_ID_QUERY =
            "SELECT ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, GENDER_TYPE, USERNAME, HASHED_PASSWORD, ROLE, WEBSITE_ID, CREATED_AT FROM APP_USER WHERE ID = ?";

    private static final String FIND_ALL_QUERY =
            "SELECT ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, GENDER_TYPE, USERNAME, HASHED_PASSWORD, ROLE, WEBSITE_ID, CREATED_AT FROM APP_USER";

    private static final String ROLE_ADMIN = "AdminRole";
    private static final String ROLE_MARKETING = "MarketingRole";
    private static final String ROLE_USER = "UserRole";

    @Override
    public T findById(Long id) throws RepositoryAccessException {
        if (!DbActiveUtil.isDatabaseOnline()) {
            log.error("Database is inactive. Please check your connection.");
            throw new RepositoryAccessException("Database is inactive. Please check your connection.");
        }
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            stmt.setLong(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return (T) extractUserFromResultSet(resultSet);
                } else {
                    throw new RepositoryAccessException("User with id " + id + " not found!");
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
        List<T> users = new ArrayList<>();
        try (Connection connection = DbActiveUtil.connectToDatabase();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(FIND_ALL_QUERY)) {

            while (resultSet.next()) {
                users.add((T) extractUserFromResultSet(resultSet));
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
        return users;
    }

    public static User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String firstName = resultSet.getString("FIRST_NAME");
        String lastName = resultSet.getString("LAST_NAME");

        LocalDate dateOfBirth = resultSet.getTimestamp("DATE_OF_BIRTH").toLocalDateTime().toLocalDate();

        String nationality = resultSet.getString("NATIONALITY");
        String genderTypeString = resultSet.getString("GENDER_TYPE");
        GenderType gender = GenderType.valueOf(genderTypeString.toUpperCase());

        String username = resultSet.getString("USERNAME");
        String hashedPassword = resultSet.getString("HASHED_PASSWORD");

        String roleString = resultSet.getString("ROLE");
        Role role;
        if (roleString.equalsIgnoreCase(ROLE_ADMIN)) {
            role = new AdminRole();
        } else if (roleString.equalsIgnoreCase(ROLE_MARKETING)) {
            role = new MarketingRole();
        } else if (roleString.equalsIgnoreCase(ROLE_USER)) {
            role = new UserRole();
        } else {
            throw new SQLException("Unknown role type: " + roleString);
        }

        Long websiteId = resultSet.getLong("WEBSITE_ID");
        LocalDateTime registrationDate = resultSet.getTimestamp("CREATED_AT").toLocalDateTime();

        PersonalData personalData = new PersonalData(dateOfBirth, nationality, gender);

        return new User.Builder()
                .setId(id)
                .setName(firstName)
                .setSurname(lastName)
                .setPersonalData(personalData)
                .setUsername(username)
                .setHashedPassword(hashedPassword)
                .setRole(role)
                .setWebsiteId(websiteId)
                .setRegistrationDate(registrationDate)
                .build();
    }

    @Override
    public void save(List<T> entities) throws RepositoryAccessException {
        String sql = "INSERT INTO APP_USER (FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, GENDER_TYPE, USERNAME, HASHED_PASSWORD, ROLE, WEBSITE_ID, CREATED_AT) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (T entity : entities) {
                stmt.setString(1, entity.getFirstName());
                stmt.setString(2, entity.getLastName());
                stmt.setDate(3, Date.valueOf(entity.getPersonalData().dateOfBirth()));
                stmt.setString(4, entity.getPersonalData().nationality());
                stmt.setString(5, entity.getPersonalData().gender().toString());
                stmt.setString(6, entity.getUsername());
                stmt.setString(7, entity.getHashedPassword());

                String roleString;
                if (entity.getRole() instanceof AdminRole) {
                    roleString = ROLE_ADMIN;
                } else if (entity.getRole() instanceof MarketingRole) {
                    roleString = ROLE_MARKETING;
                } else if (entity.getRole() instanceof UserRole) {
                    roleString = ROLE_USER;
                } else {
                    roleString = entity.getRole().getClass().getSimpleName().toUpperCase();
                }
                stmt.setString(8, roleString);
                stmt.setLong(9, entity.getWebsiteId());
                stmt.setTimestamp(10, Timestamp.valueOf(entity.getRegistrationDate()));

                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException | IOException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public void save(T entity) throws RepositoryAccessException {
        String sql = "INSERT INTO APP_USER (FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, GENDER_TYPE, USERNAME, HASHED_PASSWORD, ROLE, WEBSITE_ID, CREATED_AT) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getFirstName());
            stmt.setString(2, entity.getLastName());
            stmt.setDate(3, Date.valueOf(entity.getPersonalData().dateOfBirth()));
            stmt.setString(4, entity.getPersonalData().nationality());
            stmt.setString(5, entity.getPersonalData().gender().toString());
            stmt.setString(6, entity.getUsername());
            stmt.setString(7, entity.getHashedPassword());

            String roleString;
            if (entity.getRole() instanceof AdminRole) {
                roleString = ROLE_ADMIN;
            } else if (entity.getRole() instanceof MarketingRole) {
                roleString = ROLE_MARKETING;
            } else if (entity.getRole() instanceof UserRole) {
                roleString = ROLE_USER;
            } else {
                roleString = entity.getRole().getClass().getSimpleName().toUpperCase();
            }
            stmt.setString(8, roleString);
            stmt.setLong(9, entity.getWebsiteId());
            stmt.setTimestamp(10, Timestamp.valueOf(entity.getRegistrationDate()));
            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
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
            executeDeleteUserQuery(connection, id);
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

    private void executeDeleteUserQuery(Connection connection, Long id) throws SQLException {
        String deleteUserQuery = "DELETE FROM APP_USER WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteUserQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
