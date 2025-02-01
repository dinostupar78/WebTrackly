package hr.javafx.webtrackly.app.model;

public class User extends Person{
    private String username;
    private String password;

    public User(Long id, String name, String surname, PersonalData personalData, String username, String password) {
        super(id, name, surname, personalData);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
