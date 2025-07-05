package hr.javafx.webtrackly.app.db;

import hr.javafx.webtrackly.app.enums.WebsiteType;
import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.utils.DbActiveUtil;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class WebsiteDbRepository3 {

    private static Boolean dbLock = false;

    public synchronized Optional<WebsiteType> findMostFrequentCategory() {
        while (dbLock) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RepositoryException(e);
            }
        }
        dbLock = true;
        String sql = "SELECT WEBSITE_CATEGORY FROM WEBSITE "
                + "GROUP BY WEBSITE_CATEGORY "
                + "ORDER BY COUNT(*) DESC "
                + "LIMIT 1";

        try (Connection connection = DbActiveUtil.connectToDatabase();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                WebsiteType topCat = WebsiteType.valueOf(rs.getString("WEBSITE_CATEGORY"));
                return Optional.of(topCat);
            }
            return Optional.empty();

        } catch (SQLException | IOException | DbConnectionException ex) {
            throw new RepositoryException("Error fetching most frequent category", ex);
        } finally {
            dbLock = false;
            notifyAll();
        }
    }

    public synchronized Integer countByCategory(WebsiteType category) {
        while (dbLock) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RepositoryException(e);
            }
        }
        dbLock = true;
        String sql = "SELECT COUNT(*) as cnt FROM WEBSITE WHERE WEBSITE_CATEGORY = ?";
        try (Connection conn = DbActiveUtil.connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
                return 0;
            }
        } catch (Exception ex) {
            throw new RepositoryException("Error counting category", ex);
        } finally {
            dbLock = false;
            notifyAll();
        }
    }


    public synchronized Optional<String> findMostFrequentUrl() {
        while (dbLock) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RepositoryException(e);
            }
        }
        dbLock = true;

        String sql = ""
                + "SELECT WEBSITE_URL "
                + "FROM WEBSITE "
                + "GROUP BY WEBSITE_URL "
                + "ORDER BY COUNT(*) DESC "
                + "LIMIT 1";

        try (Connection conn = DbActiveUtil.connectToDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return Optional.of(rs.getString("WEBSITE_URL"));
            }
            return Optional.empty();

        } catch (SQLException | IOException | DbConnectionException ex) {
            throw new RepositoryException("Error fetching most frequent URL", ex);
        } finally {
            dbLock = false;
            notifyAll();
        }
    }

    public synchronized Integer countByUrl(String url) {
        while (dbLock) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RepositoryException(e);
            }
        }
        dbLock = true;

        String sql = "SELECT COUNT(*) AS cnt FROM WEBSITE WHERE WEBSITE_URL = ?";
        try (Connection conn = DbActiveUtil.connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, url);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
                return 0;
            }

        } catch (IOException | SQLException | DbConnectionException ex) {
            throw new RepositoryException("Error counting URL occurrences", ex);
        } finally {
            dbLock = false;
            notifyAll();
        }
    }

}
