package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.enums.BehaviourType;
import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.utils.DbActiveUtil;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class UserActionDbRepository3 {

    private Boolean dbLock = false;

    public synchronized Optional<BehaviourType> findMostFrequentAction() {
        while (dbLock) {
            try { wait(); }
            catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RepositoryException(ie);
            }
        }
        dbLock = true;

        String sql = "SELECT ACTION, COUNT(*) AS cnt " +
                        "FROM USER_ACTION " +
                        "GROUP BY ACTION " +
                        "ORDER BY cnt DESC " +
                        "LIMIT 1";

        try (Connection conn = DbActiveUtil.connectToDatabase();
             Statement stmt   = conn.createStatement();
             ResultSet rs     = stmt.executeQuery(sql)) {

            if (rs.next()) {
                BehaviourType bt = BehaviourType.valueOf(rs.getString("ACTION"));
                return Optional.of(bt);
            }
            return Optional.empty();

        } catch (SQLException | IOException | DbConnectionException ex) {
            throw new RepositoryException("Error fetching most frequent action", ex);
        } finally {
            dbLock = false;
            notifyAll();
        }
    }

    public synchronized Integer countByAction(BehaviourType action) {
        while (dbLock) {
            try { wait(); }
            catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RepositoryException(ie);
            }
        }
        dbLock = true;

        String sql = "SELECT COUNT(*) AS cnt FROM USER_ACTION WHERE ACTION = ?";
        try (Connection conn = DbActiveUtil.connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, action.name());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("cnt") : 0;
            }

        } catch (SQLException|IOException|DbConnectionException ex) {
            throw new RepositoryException("Error counting actions", ex);
        } finally {
            dbLock = false;
            notifyAll();
        }
    }

}
