package hr.javafx.webtrackly.app.model;
import hr.javafx.webtrackly.app.enums.DeviceType;
import hr.javafx.webtrackly.utils.DateFormatterUtil;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Klasa koja predstavlja sesiju korisnika na određenoj web stranici.
 * Sadrži informacije o web stranici, korisniku, vrsti uređaja, vremenu početka i završetka sesije te statusu aktivnosti.
 */

public class Session extends Entity{
    private Website website;
    private User user;
    private DeviceType deviceType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isActive;

    public Session(Long id, Website website, User user, DeviceType deviceType, LocalDateTime startTime, LocalDateTime endTime, boolean isActive) {
        super(id);
        this.website = website;
        this.user = user;
        this.deviceType = deviceType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Optional<Long> getSessionDurationMinutes(){
        return Optional.ofNullable(startTime)
                .flatMap(start -> Optional.ofNullable(endTime)
                        .map(end -> Duration.between(start, end).toMinutes()));
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Builder klasa za izgradnju objekta Session.
     */

    public static class Builder{
        private Long id;
        private Website website;
        private User user;
        private DeviceType deviceType;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private boolean isActive;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setWebsite(Website website) {
            this.website = website;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setDeviceType(DeviceType deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public Builder setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setActive(boolean active) {
            isActive = active;
            return this;
        }

        public Session build(){
            return new Session(id, website, user, deviceType, startTime, endTime, isActive);
        }
    }

    /**
     * Vraća string reprezentaciju objekta Session.
     * @return String koji sadrži informacije o sesiji.
     */

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Site: %s | User: %s | Device: %s | Start: %s | End: %s | Active: %s",
                getId(),
                Optional.ofNullable(website)
                        .map(Website::getWebsiteName)
                        .orElse("N/A"),
                Optional.ofNullable(user)
                        .map(User::getUsername)
                        .orElse("N/A"),
                Optional.ofNullable(deviceType)
                        .map(DeviceType::name)
                        .orElse("N/A"),
                Optional.ofNullable(startTime)
                        .map(DateFormatterUtil::formatLocalDateTime)
                        .orElse("N/A"),
                Optional.ofNullable(endTime)
                        .map(DateFormatterUtil::formatLocalDateTime)
                        .orElse("N/A"),
                isActive ? "Yes" : "No"

        );
    }
}
