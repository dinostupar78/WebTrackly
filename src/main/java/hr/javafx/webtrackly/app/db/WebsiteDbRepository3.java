package hr.javafx.webtrackly.app.db;
import hr.javafx.webtrackly.app.enums.WebsiteType;
import hr.javafx.webtrackly.app.exception.DbConnectionException;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;

/**
 * Repozitorij za rad s bazom podataka za entitet Website.
 * Ova klasa sadrži metode za dohvat najčešće kategorije,
 * brojanje broja web stranica po kategoriji,
 * dohvat najčešće URL adrese i brojanje broja web stranica po URL adresi.
 * Ova klasa koristi zaključavanje kako bi se spriječile
 * istovremene promjene baze podataka i osigurala konsistentnost podataka.
 */

public class WebsiteDbRepository3 {

    private static boolean dbLock = false;

    /**
     * Dohvaća najčešću kategoriju web stranica iz baze podataka.
     * Ova metoda koristi SQL upit za grupiranje i brojanje kategorija,
     * te vraća opcionalnu vrijednost najčešće kategorije.
     *
     * @return Opcionalna vrijednost najčešće kategorije web stranica.
     */

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

    /**
     * Broji broj web stranica po kategoriji.
     * Ova metoda koristi SQL upit za brojanje web stranica
     * koje pripadaju određenoj kategoriji.
     *
     * @param category Kategorija web stranica za koju se broji broj stranica.
     * @return Broj web stranica u zadanoj kategoriji.
     */

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

    /**
     * Dohvaća najčešću URL adresu web stranica iz baze podataka.
     * Ova metoda koristi SQL upit za grupiranje i brojanje URL adresa,
     * te vraća opcionalnu vrijednost najčešće URL adrese.
     *
     * @return Opcionalna vrijednost najčešće URL adrese web stranica.
     */


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

    /**
     * Broji broj web stranica po URL adresi.
     * Ova metoda koristi SQL upit za brojanje web stranica
     * koje imaju određenu URL adresu.
     *
     * @param url URL adresa web stranica za koju se broji broj stranica.
     * @return Broj web stranica s zadanim URL-om.
     */

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
