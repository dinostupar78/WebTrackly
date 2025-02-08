package hr.javafx.webtrackly.app.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TrafficRecord extends Entity{
    private Optional<Website> website;
    private LocalDateTime timeOfVisit;
    private List<Session> sessions;

    public TrafficRecord(Long id, Optional<Website> website, LocalDateTime timeOfVisit, List<Session> sessions) {
        super(id);
        this.website = website;
        this.timeOfVisit = timeOfVisit;
        this.sessions = sessions;
    }

    public Optional<Website> getWebsite() {
        return website;
    }

    public void setWebsite(Optional<Website> website) {
        this.website = website;
    }

    public LocalDateTime getTimeOfVisit() {
        return timeOfVisit;
    }

    public void setTimeOfVisit(LocalDateTime timeOfVisit) {
        this.timeOfVisit = timeOfVisit;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}
