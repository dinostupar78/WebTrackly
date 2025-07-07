package hr.javafx.webtrackly.utils;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Util klasa za hashiranje lozinki.
 * Ova klasa pruža statičku metodu za hashiranje lozinki koristeći SHA-256 algoritam.
 * Ako dođe do greške prilikom hashiranja, baca RepositoryException.
 */

public class PasswordUtil {
    private PasswordUtil() {}

    /**
     * Hashira lozinku koristeći SHA-256 algoritam.
     * Vraća heksadecimalni string koji predstavlja hash lozinke.
     *
     * @param password Lozinka koja se hashira.
     * @return Heksadecimalni string hashirane lozinke.
     * @throws RepositoryException Ako dođe do greške prilikom hashiranja.
     */

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RepositoryException("SHA-256 algorithm not available", e);
        }
    }
}
