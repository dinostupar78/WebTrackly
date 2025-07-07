package hr.javafx.webtrackly.app.db;
import hr.javafx.webtrackly.app.enums.BehaviourType;
import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.InvalidDataException;
import hr.javafx.webtrackly.app.exception.EntityNotFoundException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.Session;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.UserAction;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static hr.javafx.webtrackly.main.HelloApplication.log;

/**
 * Klasa koja predstavlja repozitorij za upravljanje korisničkim akcijama u bazi podataka.
 * Nasljeđuje apstraktnu klasu AbstractDbRepository i implementira metode za dohvat i spremanje korisničkih akcija.
 *
 * @param <T> Tip korisničke akcije koja se pohranjuje u repozitorij.
 */

public class UserActionDbRepository1<T extends UserAction> extends AbstractDbRepository<T> {
    private static final String FIND_BY_ID_QUERY =
            "SELECT ID, USER_ID, SESSION_ID, ACTION , WEBSITE_ID, ACTION_TIMESTAMP, DETAILS FROM USER_ACTION WHERE ID = ?";

    private static final String FIND_ALL_QUERY =
            "SELECT ID, USER_ID, SESSION_ID, ACTION, WEBSITE_ID, ACTION_TIMESTAMP, DETAILS FROM USER_ACTION";

    /**
     * Metoda za dohvat korisničke akcije prema ID-u.
     * Ako akcija s danim ID-om ne postoji, baca se EntityNotFoundException.
     * @param id ID korisničke akcije koju tražimo.
     * @return Korisnička akcija s danim ID-om.
     */

    @Override
    public T findById(Long id) {
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            stmt.setLong(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return (T) extractFromUserActionResultSet(resultSet);
                } else {
                    log.error("User Action with id {} not found! ", id);
                    throw new EntityNotFoundException("User Action with id " + id + " not found!");
                }
            }
        } catch (InvalidDataException | DbConnectionException | SQLException | IOException e) {
            log.error("Error while trying to find User Action with id {}! ", id, e);
            throw new RepositoryException("Error while trying to find User Action with id !");
        }
    }

    /**
     * Metoda za dohvat svih korisničkih akcija iz baze podataka.
     * Ako dođe do greške prilikom dohvaćanja, baca se RepositoryException.
     * @return Lista svih korisničkih akcija.
     */

    @Override
    public List<T> findAll(){
        List<T> actions = new ArrayList<>();
        try (Connection connection = DbActiveUtil.connectToDatabase();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(FIND_ALL_QUERY)) {

            while (resultSet.next()) {
                actions.add((T) extractFromUserActionResultSet(resultSet));
            }
        } catch (IOException | SQLException | DbConnectionException | InvalidDataException e) {
            log.error("Error while trying to find all User Actions! ", e);
            throw new RepositoryException("Error while trying to find all User Actions!");
        }
        return actions;
    }

    /**
     * Metoda za spremanje liste korisničkih akcija u bazu podataka.
     * Ako dođe do greške prilikom spremanja, baca se RepositoryException.
     * @param entities Lista korisničkih akcija koje se spremaju.
     */

    @Override
    public void save(List<T> entities){
        String sql = "INSERT INTO USER_ACTION (USER_ID, SESSION_ID, ACTION, WEBSITE_ID, ACTION_TIMESTAMP, DETAILS) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (T entity : entities) {
                stmt.setLong(1, entity.getUser().getId());
                stmt.setLong(2, entity.getSession().getId());
                stmt.setString(3, entity.getAction().name());
                stmt.setLong(4, entity.getPage().getId());
                stmt.setTimestamp(5, Timestamp.valueOf(entity.getTimestamp()));
                stmt.setString(6, entity.getDetails());

                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException | IOException | DbConnectionException e) {
            log.error("Error while trying to save User Actions! ", e);
            throw new RepositoryException("Error while trying to save User Actions!");
        }

    }

    /**
     * Metoda za spremanje pojedinačne korisničke akcije u bazu podataka.
     * Ako dođe do greške prilikom spremanja, baca se RepositoryException.
     * @param entity Korisnička akcija koja se sprema.
     */

    @Override
    public void save(T entity){
        String sql = "INSERT INTO USER_ACTION (USER_ID, SESSION_ID, ACTION, WEBSITE_ID, ACTION_TIMESTAMP, DETAILS) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getUser().getId());
            stmt.setLong(2, entity.getSession().getId());
            stmt.setString(3, entity.getAction().name());
            stmt.setLong(4, entity.getPage().getId());
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getTimestamp()));
            stmt.setString(6, entity.getDetails());
            stmt.executeUpdate();

        } catch (SQLException | IOException | DbConnectionException e) {
            log.error("Error while trying to save User Action! ", e);
            throw new RepositoryException("Error while trying to save User Action!");
        }

    }

    /**
     * Privatna metoda za ekstrakciju korisničke akcije iz ResultSet-a.
     * @param resultSet ResultSet iz kojeg se ekstraktira korisnička akcija.
     * @return Korisnička akcija ekstraktirana iz ResultSet-a.
     * @throws SQLException Ako dođe do greške prilikom čitanja ResultSet-a.
     * @throws InvalidDataException Ako su podaci u ResultSet-u nevažeći.
     */

    private static UserAction extractFromUserActionResultSet(ResultSet resultSet) throws SQLException, InvalidDataException {
        Long id = resultSet.getLong("ID");

        Long userId = resultSet.getLong("USER_ID");
        UserDbRepository1<User> userRepository = new UserDbRepository1<>();
        User user = userRepository.findById(userId);

        Long sessionId = resultSet.getLong("SESSION_ID");
        SessionDbRepository1<Session> sessionRepository = new SessionDbRepository1<>();
        Session session = sessionRepository.findById(sessionId);

        String action = resultSet.getString("ACTION");
        BehaviourType behaviourType;
        try{
            behaviourType = BehaviourType.valueOf(action.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Unknown behaviour type: " + action);
        }

        Long websiteId = resultSet.getLong("WEBSITE_ID");
        WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
        Website website = websiteRepository.findById(websiteId);

        LocalDateTime startTime = resultSet.getTimestamp("ACTION_TIMESTAMP").toLocalDateTime();

        String details = resultSet.getString("DETAILS");

        return new UserAction.Builder()
                .setId(id)
                .setUser(user)
                .setSession(session)
                .setAction(behaviourType)
                .setPage(website)
                .setActionTimestamp(startTime)
                .setDetails(details)
                .build();
    }

}
