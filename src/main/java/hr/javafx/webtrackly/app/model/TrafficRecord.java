package hr.javafx.webtrackly.app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TrafficRecord extends Entity{
    private Website website;
    private LocalDateTime timeOfVisit;
    private Integer userCount;
    private Integer pageViews;
    private BigDecimal bounceRate;
    private List<Session> sessions;

    public TrafficRecord(Long id, Website website, LocalDateTime timeOfVisit, Integer userCount, Integer pageViews,
                         BigDecimal bounceRate, List<Session> sessions) {
        super(id);
        this.website = website;
        this.timeOfVisit = timeOfVisit;
        this.userCount = userCount;
        this.pageViews = pageViews;
        this.bounceRate = bounceRate;
        this.sessions = sessions;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public LocalDateTime getTimeOfVisit() {
        return timeOfVisit;
    }

    public void setTimeOfVisit(LocalDateTime timeOfVisit) {
        this.timeOfVisit = timeOfVisit;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getPageViews() {
        return pageViews;
    }

    public void setPageViews(Integer pageViews) {
        this.pageViews = pageViews;
    }

    public BigDecimal getBounceRate() {
        return bounceRate;
    }

    public void setBounceRate(BigDecimal bounceRate) {
        this.bounceRate = bounceRate;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}
