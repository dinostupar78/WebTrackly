package hr.javafx.webtrackly.threads;

import hr.javafx.webtrackly.app.db.UserDbRepository1;
import hr.javafx.webtrackly.app.model.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.util.List;

public class NewUsersThread implements Runnable{
    @FXML
    private Label newUsersLabel;

    private final UserDbRepository1<User> userRepository;

    public NewUsersThread(UserDbRepository1<User> userRepository, Label newUsersLabel) {
        this.userRepository = userRepository;
        this.newUsersLabel = newUsersLabel;
    }

    @Override
    public void run() {
        Timeline newUsersTimeLine = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    try {
                        List<User> users = userRepository.findAll();

                        LocalDateTime last24Hours = LocalDateTime.now().minusDays(1);

                        Long newUsersCount = users.stream()
                                .filter(website -> website.getRegistrationDate().isAfter(last24Hours))
                                .count();

                        Platform.runLater(() ->
                                newUsersLabel.setText("New Users Today: " + newUsersCount));

                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }),
                new KeyFrame(Duration.seconds(1))
        );

        newUsersTimeLine.setCycleCount(Animation.INDEFINITE);
        newUsersTimeLine.play();

    }
}
