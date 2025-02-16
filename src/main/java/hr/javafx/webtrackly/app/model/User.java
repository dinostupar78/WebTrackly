package hr.javafx.webtrackly.app.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User extends Person implements Serializable {
    private String username;
    private String hashedPassword;
    private Role role;
    private Long websiteId;
    private LocalDateTime registrationDate;

    public User(Long id, String name, String surname, PersonalData personalData,
                String username, String hashedPassword, Role role, Long websiteId, LocalDateTime registrationDate) {
        super(id, name, surname, personalData);
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
        this.websiteId = websiteId;
        this.registrationDate = registrationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Long websiteId) {
        this.websiteId = websiteId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", role=" + role +
                ", websiteId=" + websiteId +
                ", registrationDate=" + registrationDate +
                '}';
    }

    public static class Builder {
        private Long id;
        private String name;
        private String surname;
        private PersonalData personalData;
        private String username;
        private String hashedPassword;
        private Role role;
        private Long websiteId;
        private LocalDateTime registrationDate;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setSurname(String surname) {
            this.surname = surname;
            return this;
        }
        public Builder setPersonalData(PersonalData personalData) {
            this.personalData = personalData;
            return this;
        }
        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }
        public Builder setHashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
            return this;
        }
        public Builder setRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder setWebsiteId(Long websiteId) {
            this.websiteId = websiteId;
            return this;
        }

        public Builder setRegistrationDate(LocalDateTime registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public User build() {
            return new User(id, name, surname, personalData, username, hashedPassword, role, websiteId, registrationDate);
        }
    }
}
