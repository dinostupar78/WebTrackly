package hr.javafx.webtrackly.app.model;

public class User extends Person{
    private String username;
    private String hashedPassword;
    private Role role;

    public User(Long id, String name, String surname, PersonalData personalData, String username, String hashedPassword, Role role) {
        super(id, name, surname, personalData);
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
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

    public static class Builder{
        private Long id;
        private String name;
        private String surname;
        private PersonalData personalData;
        private String username;
        private String hashedPassword;
        private Role role;

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

            public User build(){
                return new User(id, name, surname, personalData, username, hashedPassword, role);
            }
    }

}
