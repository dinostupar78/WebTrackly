package hr.javafx.webtrackly.utils;

/**
 * Util klasa za ekstrakciju domene iz URL-a.
 * Ova klasa pruža statičku metodu za izdvajanje domene iz URL-a.
 * Primjer: "https://www.example.com/path" će vratiti "example.com".
 * Ako URL ne sadrži domenu, vraća se cijeli URL.
 */

public class ExtractDomainUtil {

    private ExtractDomainUtil() {}

    /**
     * Ekstrahira domenu iz danog URL-a.
     * Ako URL ne sadrži domenu, vraća se cijeli URL.
     *
     * @param url URL iz kojeg se želi izdvojiti domena
     * @return String - domena ili cijeli URL ako domena nije pronađena
     */

    public static String extractDomain(String url) {
        String[] parts = url.split("/");
        if (parts.length < 3) return url;
        String domain = parts[2];
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
