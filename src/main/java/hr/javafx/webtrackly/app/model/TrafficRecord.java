package hr.javafx.webtrackly.app.model;

import hr.javafx.webtrackly.utils.DateFormatterUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class TrafficRecord extends Entity{
    private Website website;
    private LocalDateTime timeOfVisit;
    private Integer pageViews;
    private BigDecimal bounceRate;

    public TrafficRecord(Long id, Website website, LocalDateTime timeOfVisit, Integer pageViews,
                         BigDecimal bounceRate) {
        super(id);
        this.website = website;
        this.timeOfVisit = timeOfVisit;
        this.pageViews = pageViews;
        this.bounceRate = bounceRate;
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

    public static class Builder{
        private Long id;
        private Website website;
        private LocalDateTime timeOfVisit;
        private Integer pageViews;
        private BigDecimal bounceRate;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setWebsite(Website website) {
            this.website = website;
            return this;
        }

        public Builder setTimeOfVisit(LocalDateTime timeOfVisit) {
            this.timeOfVisit = timeOfVisit;
            return this;
        }

        public Builder setPageViews(Integer pageViews) {
            this.pageViews = pageViews;
            return this;
        }

        public Builder setBounceRate(BigDecimal bounceRate) {
            this.bounceRate = bounceRate;
            return this;
        }

        public TrafficRecord build(){
            return new TrafficRecord(id, website, timeOfVisit,pageViews, bounceRate);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Id: %d | Website: %s | When: %s | Views: %d | Bounce: %.2f%%",
                getId(),
                Optional.ofNullable(website)
                        .map(Website::getWebsiteName)
                        .orElse("N/A"),
                Optional.ofNullable(timeOfVisit)
                        .map(DateFormatterUtil::formatLocalDateTime)
                        .orElse("N/A"),
                pageViews,
                Optional.ofNullable(bounceRate)
                        .map(BigDecimal::doubleValue)
                        .orElse(0.0) * 100
        );
    }
}
