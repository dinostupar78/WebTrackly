package hr.javafx.webtrackly.app.model;

public class UserSession {
    private static UserSession instance;
    private User currentUser;

    private UserSession(User user) {
        this.currentUser = user;
    }

    public static void createSession(User user) {
        instance = new UserSession(user);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }


}
