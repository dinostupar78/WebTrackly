package hr.javafx.webtrackly.app.model;

import hr.javafx.webtrackly.app.enums.DeviceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class Session extends Entity{
    private User user;
    private DeviceType deviceType;
    private BigDecimal sessionDuration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Optional<Boolean> isActive;

    public Session(Long id, User user, DeviceType deviceType, BigDecimal sessionDuration, LocalDateTime startTime, LocalDateTime endTime, Optional<Boolean> isActive) {
        super(id);
        this.user = user;
        this.deviceType = deviceType;
        this.sessionDuration = sessionDuration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public BigDecimal getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(BigDecimal sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Optional<Boolean> getActive() {
        return isActive;
    }

    public void setActive(Optional<Boolean> active) {
        isActive = active;
    }
}
