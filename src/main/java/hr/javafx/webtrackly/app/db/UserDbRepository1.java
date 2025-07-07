package hr.javafx.webtrackly.app.db;
import hr.javafx.webtrackly.app.enums.GenderType;
import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.InvalidDataException;
import hr.javafx.webtrackly.app.exception.EntityNotFoundException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static hr.javafx.webtrackly.main.HelloApplication.log;

/**
 * Klasa koja predstavlja repozitorij za korisnike u bazi podataka.
 * Nasljeđuje apstraktnu klasu AbstractDbRepository i implementira metode za dohvat i spremanje korisnika.
 *
 * @param <T> Tip korisnika koji se koristi u repozitoriju.
 */

public class UserDbRepository1<T extends User> extends AbstractDbRepository<T> {
    private static final String FIND_BY_ID_QUERY = "SELECT ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, GENDER_TYPE, USERNAME, EMAIL, PASSWORD, ROLE, WEBSITE_ID FROM APP_USER WHERE ID = ?";

    private static final String FIND_ALL_QUERY = "SELECT ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, GENDER_TYPE, USERNAME, EMAIL, PASSWORD, ROLE, WEBSITE_ID FROM APP_USER";

    private static final String ROLE_ADMIN = "Admin";

    private static final String ROLE_MARKETING = "Marketing";

    private static final String ROLE_USER = "User";

    /**
     * Konstruktor koji inicijalizira repozitorij korisnika.
     */

    @Override
    public T findById(Long id){
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            stmt.setLong(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return (T) extractUserFromResultSet(resultSet);
                } else {
                    log.error("User with id {} not found! ", id);
                    throw new EntityNotFoundException("User with id " + id + " not found!");
                }
            }
        } catch (IOException | SQLException | InvalidDataException | DbConnectionException e) {
            log.error("Error while fetching user from database: {}", e.getMessage());
            throw new RepositoryException("Error while fetching user from database");
        }
    }

    /**
     * Metoda koja dohvaća sve korisnike iz baze podataka.
     *
     * @return Lista svih korisnika.
     * @throws RepositoryException Ako dođe do greške prilikom dohvaćanja korisnika.
     */

    @Override
    public List<T> findAll(){
        List<T> users = new ArrayList<>();
        try (Connection connection = DbActiveUtil.connectToDatabase();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(FIND_ALL_QUERY)) {

            while (resultSet.next()) {
                users.add((T) extractUserFromResultSet(resultSet));
            }
        } catch (IOException | SQLException | InvalidDataException | DbConnectionException e) {
            log.error("Error while fetching users from database: {}", e.getMessage());
            throw new RepositoryException("Error while fetching users from database");
        }
        return users;
    }

    /**
     * Metoda koja sprema listu korisnika u bazu podataka.
     *
     * @param entities Lista korisnika koji se spremaju.
     * @throws RepositoryException Ako dođe do greške prilikom spremanja korisnika.
     */

    @Override
    public void save(List<T> entities) {
        String sql = "INSERT INTO APP_USER (FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, GENDER_TYPE, USERNAME, EMAIL, PASSWORD, ROLE, WEBSITE_ID) " +
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
                stmt.setString(7, entity.getEmail());
                stmt.setString(8, entity.getPassword());
                stmt.setString(9, entity.getRole().toString());
                stmt.setLong(10, entity.getWebsiteId());

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


                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException | IOException | DbConnectionException e) {
            log.error("Error while saving users to database: {}", e.getMessage());
            throw new RepositoryException("Error while saving users to database");
        }
    }

    /**
     * Metoda koja sprema pojedinačnog korisnika u bazu podataka.
     *
     * @param entity Korisnik koji se sprema.
     * @throws RepositoryException Ako dođe do greške prilikom spremanja korisnika.
     */

    @Override
    public void save(T entity) {
        String sql = "INSERT INTO APP_USER (FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, NATIONALITY, GENDER_TYPE, USERNAME, EMAIL, PASSWORD, ROLE, WEBSITE_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getFirstName());
            stmt.setString(2, entity.getLastName());
            stmt.setDate(3, Date.valueOf(entity.getPersonalData().dateOfBirth()));
            stmt.setString(4, entity.getPersonalData().nationality());
            stmt.setString(5, entity.getPersonalData().gender().toString());
            stmt.setString(6, entity.getUsername());
            stmt.setString(7, entity.getEmail());
            stmt.setString(8, entity.getPassword());

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
            stmt.setString(9, roleString);
            stmt.setLong(10, entity.getWebsiteId());
            stmt.executeUpdate();
        } catch (SQLException | IOException | DbConnectionException e) {
            log.error("Error while saving user to database: {}", e.getMessage());
            throw new RepositoryException("Error while saving user to database");
        }
    }

    /**
     * Metoda koja dohvaća korisnika iz ResultSet-a.
     *
     * @param resultSet ResultSet iz kojeg se dohvaća korisnik.
     * @return Korisnik koji je dohvaćen iz ResultSet-a.
     * @throws InvalidDataException Ako su podaci u ResultSet-u neispravni.
     * @throws SQLException Ako dođe do greške prilikom dohvaćanja podataka iz ResultSet-a.
     */

    public static User extractUserFromResultSet(ResultSet resultSet) throws InvalidDataException, SQLException {
        Long id = resultSet.getLong("ID");
        String firstName = resultSet.getString("FIRST_NAME");
        String lastName = resultSet.getString("LAST_NAME");

        LocalDate dateOfBirth = resultSet.getTimestamp("DATE_OF_BIRTH").toLocalDateTime().toLocalDate();

        String nationality = resultSet.getString("NATIONALITY");
        String genderTypeString = resultSet.getString("GENDER_TYPE");
        GenderType gender;
        try{
            gender = GenderType.valueOf(genderTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Unknown gender type: " + genderTypeString);
        }

        String username = resultSet.getString("USERNAME");
        String email = resultSet.getString("EMAIL");
        String password = resultSet.getString("PASSWORD");

        String roleString = resultSet.getString("ROLE");
        Role role;
        if (roleString.equalsIgnoreCase(ROLE_ADMIN)) {
            role = new AdminRole();
        } else if (roleString.equalsIgnoreCase(ROLE_MARKETING)) {
            role = new MarketingRole();
        } else if (roleString.equalsIgnoreCase(ROLE_USER)) {
            role = new UserRole();
        } else {
            log.error("Unknown role type: {}", roleString);
            throw new InvalidDataException("Unknown role type: " + roleString);
        }

        Long websiteId = resultSet.getLong("WEBSITE_ID");

        PersonalData personalData = new PersonalData(dateOfBirth, nationality, gender);

        return new User.Builder()
                .setId(id)
                .setName(firstName)
                .setSurname(lastName)
                .setPersonalData(personalData)
                .setUsername(username)
                .setEmail(email)
                .setPassword(password)
                .setRole(role)
                .setWebsiteId(websiteId)
                .build();
    }

}
