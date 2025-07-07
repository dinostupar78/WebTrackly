package hr.javafx.webtrackly.utils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Util klasa za formatiranje datuma i vremena.
 * Ova klasa pruža statičku metodu za formatiranje objekata LocalDateTime u string formatu "yyyy-MM-dd HH:mm:ss".
 */

public class DateFormatterUtil {
    private DateFormatterUtil(){}
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return FORMATTER.format(dateTime);
    }
}
