package hr.javafx.webtrackly.utils;

import hr.javafx.webtrackly.app.exception.PasswordHashingException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
    private PasswordUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8)); // Ensure UTF-8 encoding
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new PasswordHashingException("SHA-256 algorithm not available", e);
        }
    }
}
