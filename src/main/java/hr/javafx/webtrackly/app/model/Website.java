package hr.javafx.webtrackly.app.model;

import java.math.BigDecimal;
import java.util.Set;

public class Website extends Entity {
    private String websiteName;
    private Integer websiteClicks;
    private String websiteUrl;
    private Integer websiteUserCount;
    private BigDecimal bounceRate;
    private Set<User> users;

    public Website(Long id, String websiteName, Integer websiteClicks, String websiteUrl, Integer websiteUserCount,
                   BigDecimal bounceRate, Set<User> users) {
        super(id);
        this.websiteName = websiteName;
        this.websiteClicks = websiteClicks;
        this.websiteUrl = websiteUrl;
        this.websiteUserCount = websiteUserCount;
        this.bounceRate = bounceRate;
        this.users = users;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public Integer getWebsiteClicks() {
        return websiteClicks;
    }

    public void setWebsiteClicks(Integer websiteClicks) {
        this.websiteClicks = websiteClicks;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Integer getWebsiteUserCount() {
        return websiteUserCount;
    }

    public void setWebsiteUserCount(Integer websiteUserCount) {
        this.websiteUserCount = websiteUserCount;
    }

    public BigDecimal getBounceRate() {
        return bounceRate;
    }

    public void setBounceRate(BigDecimal bounceRate) {
        this.bounceRate = bounceRate;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public static class Builder{
        private Long id;
        private String websiteName;
        private Integer websiteClicks;
        private String websiteUrl;
        private Integer websiteUserCount;
        private BigDecimal bounceRate;
        private Set<User> users;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setWebsiteName(String websiteName) {
            this.websiteName = websiteName;
            return this;
        }

        public Builder setWebsiteClicks(Integer websiteClicks) {
            this.websiteClicks = websiteClicks;
            return this;
        }

        public Builder setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
            return this;
        }

        public Builder setWebsiteUserCount(Integer websiteUserCount) {
            this.websiteUserCount = websiteUserCount;
            return this;
        }

        public Builder setBounceRate(BigDecimal bounceRate) {
            this.bounceRate = bounceRate;
            return this;
        }

        public Builder setUsers(Set<User> users) {
            this.users = users;
            return this;
        }

        public Website build(){
            return new Website(id, websiteName, websiteClicks, websiteUrl, websiteUserCount, bounceRate, users);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Website[id=%d, Name='%s', Clicks=%d, URL='%s', Users=%d, Bounce Rate=%.2f%%, Registered Users=%d]",
                getId(), websiteName, websiteClicks, websiteUrl, websiteUserCount,
                bounceRate, users.size()
        );
    }
}
