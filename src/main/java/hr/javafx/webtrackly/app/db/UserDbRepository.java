package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.enums.GenderType;
import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDbRepository<T extends User> extends AbstractDbRepository<T> {
    private final Connection connection;

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_MARKETING = "MARKETING";

    public UserDbRepository(Connection connection) {
        this.connection = connection;
    }


    @Override
    public T findById(Long id) throws RepositoryAccessException {
        return null;
    }

    @Override
    public List<T> findAll() throws RepositoryAccessException {
        List<T> users = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            // Include WEBSITE_ID in the select
            ResultSet resultSet = stmt.executeQuery(
                    "SELECT ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, " +
                            "GENDER_TYPE, USERNAME, HASHED_PASSWORD, ROLE, WEBSITE_ID FROM APP_USER"
            );
            while (resultSet.next()) {
                User user = extractUserFromResultSet(resultSet);
                users.add((T) user);
            }
        } catch (Exception e) {
            throw new RepositoryAccessException(e);
        }
        return users;
    }

    public static User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String firstName = resultSet.getString("FIRST_NAME");
        String lastName = resultSet.getString("LAST_NAME");
        LocalDate dateOfBirth = resultSet.getDate("DATE_OF_BIRTH").toLocalDate();
        String nationality = resultSet.getString("NATIONALITY");
        String genderTypeString = resultSet.getString("GENDER_TYPE");
        GenderType gender = GenderType.valueOf(genderTypeString.toUpperCase());
        String username = resultSet.getString("USERNAME");
        String hashedPassword = resultSet.getString("HASHED_PASSWORD");
        String roleString = resultSet.getString("ROLE");
        Role role;
        if (roleString.equalsIgnoreCase("ADMIN")) {
            role = new AdminRole();
        } else if (roleString.equalsIgnoreCase("MARKETING")) {
            role = new MarketingRole();
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
        String sql = "INSERT INTO APP_USER (FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, GENDER_TYPE, USERNAME, HASHED_PASSWORD, ROLE, WEBSITE_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
                } else {
                    roleString = entity.getRole().getClass().getSimpleName().toUpperCase();
                }
                stmt.setString(8, roleString);
                stmt.setLong(9, entity.getWebsiteId());  // Set the website id

                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public void save(T entity) throws RepositoryAccessException {
        String sql = "INSERT INTO APP_USER (FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, GENDER_TYPE, USERNAME, HASHED_PASSWORD, ROLE, WEBSITE_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
            } else {
                roleString = entity.getRole().getClass().getSimpleName().toUpperCase();
            }
            stmt.setString(8, roleString);
            stmt.setLong(9, entity.getWebsiteId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }
}
