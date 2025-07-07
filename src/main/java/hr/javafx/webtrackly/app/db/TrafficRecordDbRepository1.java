package hr.javafx.webtrackly.app.db;
import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.EntityNotFoundException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.TrafficRecord;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static hr.javafx.webtrackly.main.HelloApplication.log;

/**
 * Klasa koja predstavlja repozitorij za upravljanje prometnim zapisima u bazi podataka.
 * Nasljeđuje apstraktnu klasu AbstractDbRepository i implementira metode za dohvat i spremanje prometnih zapisa.
 *
 * @param <T> Tip prometnog zapisa koji se koristi u repozitoriju.
 */

public class TrafficRecordDbRepository1<T extends TrafficRecord> extends AbstractDbRepository<T> {
    private static final String FIND_BY_ID_QUERY = "SELECT ID, WEBSITE_ID, TIME_OF_VISIT FROM TRAFFIC_RECORD WHERE ID = ?";

    private static final String FIND_ALL_QUERY = "SELECT ID, WEBSITE_ID, TIME_OF_VISIT FROM TRAFFIC_RECORD";

    /**
     * Konstruktor koji inicijalizira repozitorij s tipom prometnog zapisa.
     */

    @Override
    public T findById(Long id){
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            stmt.setLong(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return (T) extractTrafficRecordFromResultSet(resultSet);
                } else {
                    log.error("Traffic record with id {} not found!", id);
                    throw new EntityNotFoundException("Traffic record with id " + id + " not found!");
                }
            }
        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error fetching traffic record with id {}", id, e);
            throw new RepositoryException("Error fetching traffic record with id ");
        }
    }

    /**
     * Metoda koja dohvaća sve prometne zapise iz baze podataka.
     * Pretražuje tablicu TRAFFIC_RECORD i vraća listu svih prometnih zapisa.
     *
     *
     * @return Lista svih prometnih zapisa.
     */

    @Override
    public List<T> findAll(){
        List<T> trafficRecords = new ArrayList<>();
        try (Connection connection = DbActiveUtil.connectToDatabase();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(FIND_ALL_QUERY)) {

            while (resultSet.next()) {
                trafficRecords.add((T) extractTrafficRecordFromResultSet(resultSet));
            }
        } catch (IOException | SQLException | DbConnectionException e) {
            log.error("Error fetching traffic records ", e);
            throw new RepositoryException("Error fetching traffic records");
        }
        return trafficRecords;
    }

    /**
     * Metoda koja sprema listu prometnih zapisa u bazu podataka.
     * Koristi SQL INSERT naredbu za dodavanje svakog prometnog zapisa u tablicu TRAFFIC_RECORD.
     *
     * @param entities Lista prometnih zapisa koji se spremaju.
     */

    @Override
    public void save(List<T> entities){
        String sql = "INSERT INTO TRAFFIC_RECORD (WEBSITE_ID, TIME_OF_VISIT) " +
                "VALUES (?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (T entity : entities) {
                stmt.setLong(1, entity.getWebsite().getId());
                stmt.setTimestamp(2, Timestamp.valueOf(entity.getTimeOfVisit()));
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException | IOException | DbConnectionException e) {
            log.error("Error saving traffic records ", e);
            throw new RepositoryException("Error saving traffic records");
        }

    }

    /**
     * Metoda koja sprema pojedinačni prometni zapis u bazu podataka.
     * Koristi SQL INSERT naredbu za dodavanje prometnog zapisa u tablicu TRAFFIC_RECORD.
     *
     * @param entity Prometni zapis koji se sprema.
     */

    @Override
    public void save(T entity){
        String sql = "INSERT INTO TRAFFIC_RECORD (WEBSITE_ID, TIME_OF_VISIT) " +
                "VALUES (?, ?)";
        try (Connection connection = DbActiveUtil.connectToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getWebsite().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(entity.getTimeOfVisit()));
            stmt.executeUpdate();
        } catch (SQLException | IOException | DbConnectionException e) {
            log.error("Error saving traffic records ", e);
            throw new RepositoryException("Error saving traffic records");
        }

    }

    /**
     * Privatna metoda koja izvlači prometni zapis iz ResultSet objekta.
     * Koristi se za pretvaranje rezultata upita u objekt prometnog zapisa.
     *
     * @param resultSet ResultSet iz kojeg se izvlače podaci.
     * @return Prometni zapis koji sadrži podatke iz ResultSet-a.
     * @throws SQLException Ako dođe do greške pri dohvaćanju podataka iz ResultSet-a.
     */

    private static TrafficRecord extractTrafficRecordFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");

        Long websiteId = resultSet.getLong("WEBSITE_ID");
        WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
        Website website = websiteRepository.findById(websiteId);

        LocalDateTime timeOfVisit = resultSet.getTimestamp("TIME_OF_VISIT").toLocalDateTime();


        return new TrafficRecord.Builder()
                .setId(id)
                .setWebsite(website)
                .setTimeOfVisit(timeOfVisit)
                .build();

    }

}
