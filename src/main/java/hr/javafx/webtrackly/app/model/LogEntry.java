package hr.javafx.webtrackly.app.model;

/**
 * Klasa koja predstavlja zapis u dnevniku aplikacije WebTrackly.
 * Sadrži informacije o vremenu, razini i poruci zapisa.
 */

public class LogEntry {
    private final String timestamp;
    private final String level;
    private final String message;

    public LogEntry(String timestamp, String level, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }
}
