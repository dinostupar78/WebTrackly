package hr.javafx.webtrackly.threads;

import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.model.Website;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.List;

public class TotalUsersThread implements Runnable{
    @FXML
    private Label totalUsersLabel;

    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();

    public TotalUsersThread(WebsiteDbRepository1<Website> websiteRepository, Label totalUsersLabel) {
        this.websiteRepository = websiteRepository;
        this.totalUsersLabel = totalUsersLabel;
    }


    @Override
    public void run() {
        Timeline usersTimeLine = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    try {
                        List<Website> websites = websiteRepository.findAll();

                        int totalUsers = websites.stream()
                                .mapToInt(Website::getWebsiteUserCount)
                                .sum();

                        Platform.runLater(() ->
                            totalUsersLabel.setText(String.valueOf(totalUsers)));

                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }),
                new KeyFrame(Duration.seconds(1))
        );

        usersTimeLine.setCycleCount(Animation.INDEFINITE);
        usersTimeLine.play();

    }
}
