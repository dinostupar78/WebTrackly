package hr.javafx.webtrackly.utils;

public class ExtractDomainUtil {

    private ExtractDomainUtil() {}

    public static String extractDomain(String url) {
        String[] parts = url.split("/");
        if (parts.length < 3) return url;
        String domain = parts[2];
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
