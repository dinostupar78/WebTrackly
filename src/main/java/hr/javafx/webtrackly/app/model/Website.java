package hr.javafx.webtrackly.app.model;

import hr.javafx.webtrackly.app.enums.WebsiteType;

import java.time.LocalDateTime;
import java.util.Set;

public class Website extends Entity {
    private String websiteName;
    private String websiteUrl;
    private WebsiteType websiteCategory;
    private String websiteDescription;
    private Set<User> users;
    private LocalDateTime createdAt;

    public Website(Long id, String websiteName, String websiteUrl, WebsiteType websiteCategory,
                   String websiteDescription, Set<User> users, LocalDateTime createdAt) {
        super(id);
        this.websiteName = websiteName;
        this.websiteUrl = websiteUrl;
        this.websiteCategory = websiteCategory;
        this.websiteDescription = websiteDescription;
        this.users = users;
        this.createdAt = createdAt;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public WebsiteType getWebsiteCategory() {
        return websiteCategory;
    }

    public void setWebsiteCategory(WebsiteType websiteCategory) {
        this.websiteCategory = websiteCategory;
    }

    public String getWebsiteDescription() {
        return websiteDescription;
    }

    public void setWebsiteDescription(String websiteDescription) {
        this.websiteDescription = websiteDescription;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static class Builder{
        private Long id;
        private String websiteName;
        private String websiteUrl;
        private WebsiteType websiteCategory;
        private String websiteDescription;
        private Set<User> users;
        private LocalDateTime createdAt;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setWebsiteName(String websiteName) {
            this.websiteName = websiteName;
            return this;
        }

        public Builder setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
            return this;
        }

        public Builder setWebsiteCategory(WebsiteType websiteCategory) {
            this.websiteCategory = websiteCategory;
            return this;
        }

        public Builder setWebsiteDescription(String websiteDescription) {
            this.websiteDescription = websiteDescription;
            return this;
        }

        public Builder setUsers(Set<User> users) {
            this.users = users;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Website build(){
            return new Website(id, websiteName, websiteUrl, websiteCategory, websiteDescription, users, createdAt);
        }
    }


    @Override
    public String toString() {
        return "Website{" +
                "websiteName='" + websiteName + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", websiteCategory=" + websiteCategory +
                ", websiteDescription='" + websiteDescription + '\'' +
                ", users=" + users +
                ", createdAt=" + createdAt +
                '}';
    }
}
