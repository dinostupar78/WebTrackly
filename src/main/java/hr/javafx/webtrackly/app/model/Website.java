package hr.javafx.webtrackly.app.model;
import hr.javafx.webtrackly.app.enums.WebsiteType;
import java.util.Optional;
import java.util.Set;

public class Website extends Entity {
    private String websiteName;
    private String websiteUrl;
    private WebsiteType websiteCategory;
    private String websiteDescription;
    private Set<User> users;

    public Website(Long id, String websiteName, String websiteUrl, WebsiteType websiteCategory,
                   String websiteDescription, Set<User> users) {
        super(id);
        this.websiteName = websiteName;
        this.websiteUrl = websiteUrl;
        this.websiteCategory = websiteCategory;
        this.websiteDescription = websiteDescription;
        this.users = users;
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

    public static class Builder{
        private Long id;
        private String websiteName;
        private String websiteUrl;
        private WebsiteType websiteCategory;
        private String websiteDescription;
        private Set<User> users;

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

        public Website build(){
            return new Website(id, websiteName, websiteUrl, websiteCategory, websiteDescription, users);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Name: %s | URL: %s | Cat: %s | Desc: %s | Users: %d",
                Optional.ofNullable(websiteName)
                        .orElse("N/A"),
                Optional.ofNullable(websiteUrl)
                        .orElse("N/A"),
                Optional.ofNullable(websiteCategory)
                        .map(Enum::name)
                        .orElse("N/A"),
                websiteDescription.length() > 20 ? websiteDescription.substring(0, 20) + "…" : websiteDescription,
                Optional.ofNullable(users)
                        .map(Set::size)
                        .orElse(0));
    }
}
