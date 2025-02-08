package hr.javafx.webtrackly.app.model;

import hr.javafx.webtrackly.app.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.Optional;

public class Notification extends Entity{
    private Optional<Website> website;
    private String message;
    private LocalDateTime notificationTimestamp;
    private NotificationType notificationType;

    public Notification(Long id, Optional<Website> website, String message, LocalDateTime notificationTimestamp, NotificationType notificationType) {
        super(id);
        this.website = website;
        this.message = message;
        this.notificationTimestamp = notificationTimestamp;
        this.notificationType = notificationType;
    }

    public Optional<Website> getWebsite() {
        return website;
    }

    public void setWebsite(Optional<Website> website) {
        this.website = website;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return notificationTimestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.notificationTimestamp = timestamp;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
