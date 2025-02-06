package hr.javafx.webtrackly.app.model;

import java.time.LocalDateTime;

public class TrafficRecord {
    private LocalDateTime timestamp;
    private int userCount;
    private String source;

    public TrafficRecord(LocalDateTime timestamp, int userCount, String source) {
        this.timestamp = timestamp;
        this.userCount = userCount;
        this.source = source;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
