package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.enums.GenderType;
import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserDbRepository<T extends User> extends AbstractDbRepository<T> {
    private final Connection connection;

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_MARKETING = "MARKETING";

    public UserDbRepository(Connection connection) {
        this.connection = connection;
    }


    @Override
    public List<T> findAll() throws RepositoryAccessException {
        List<T> users = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT id, first_name, last_name, date_of_birth, nationality," +
                    " gender_type, username, hashedPassword, role FROM USER");
            while (resultSet.next()) {
                User user = extractUserFromResultSet(resultSet);
                users.add((T) user);
            }

        } catch (Exception e) {
            throw new RepositoryAccessException(e);
        }
        return users;
    }

    private static User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
        String nationality = resultSet.getString("nationality");
        String genderTypeString = resultSet.getString("gender_type");
        GenderType gender = GenderType.valueOf(genderTypeString.toUpperCase());
        String username = resultSet.getString("username");
        String hashedPassword = resultSet.getString("hashedPassword");

        String roleString = resultSet.getString("role");
        Role role;
        if (roleString.equalsIgnoreCase(ROLE_ADMIN)) {
            role = new AdminRole();
        } else if (roleString.equalsIgnoreCase(ROLE_MARKETING)) {
            role = new MarketingRole();
        } else {
            throw new SQLException("Unknown role type: " + roleString);
        }

        PersonalData personalData = new PersonalData(dateOfBirth, nationality, gender);

        return new User.Builder()
                .setId(id)
                .setName(firstName)
                .setSurname(lastName)
                .setPersonalData(personalData)
                .setUsername(username)
                .setHashedPassword(hashedPassword)
                .setRole(role)
                .build();
    }

    @Override
    public void save(Set<T> entities) throws RepositoryAccessException {
        String sql = "INSERT INTO USER(FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, GENDER_TYPE, USERNAME, HASHED_PASSWORD, ROLE)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
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

                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public void save(T entity) throws RepositoryAccessException {
        String sql = "INSERT INTO USER(FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, GENDER_TYPE, USERNAME, HASHED_PASSWORD, ROLE)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
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

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }
}
