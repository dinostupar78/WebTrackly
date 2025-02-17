package hr.javafx.webtrackly.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatterUtil {
    private DateFormatterUtil(){}
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return FORMATTER.format(dateTime);
    }
}
