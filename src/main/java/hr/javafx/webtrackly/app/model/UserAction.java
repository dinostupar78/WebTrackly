package hr.javafx.webtrackly.app.model;

import hr.javafx.webtrackly.app.enums.BehaviorType;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserAction extends Entity{
    private User user;
    private BehaviorType action;
    private Optional<Website> page;
    private LocalDateTime actionTimestamp;
    private Optional<String> details;

    public UserAction(Long id, User user, BehaviorType action, Optional<Website> page, LocalDateTime actionTimestamp, Optional<String> details) {
        super(id);
        this.user = user;
        this.action = action;
        this.page = page;
        this.actionTimestamp = actionTimestamp;
        this.details = details;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BehaviorType getAction() {
        return action;
    }

    public void setAction(BehaviorType action) {
        this.action = action;
    }

    public Optional<Website> getPage() {
        return page;
    }

    public void setPage(Optional<Website> page) {
        this.page = page;
    }

    public LocalDateTime getTimestamp() {
        return actionTimestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.actionTimestamp = timestamp;
    }

    public Optional<String> getDetails() {
        return details;
    }

    public void setDetails(Optional<String> details) {
        this.details = details;
    }
}
