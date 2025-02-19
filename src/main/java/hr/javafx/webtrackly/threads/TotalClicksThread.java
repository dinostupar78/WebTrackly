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

public class TotalClicksThread implements Runnable{
    @FXML
    private Label totalClicksLabel;

    private WebsiteDbRepository1<Website> websiteRepository;

    public TotalClicksThread(WebsiteDbRepository1<Website> websiteRepository, Label totalClicksLabel) {
        this.websiteRepository = websiteRepository;
        this.totalClicksLabel = totalClicksLabel;
    }

    @Override
    public void run() {
        Timeline clicksTimeLine = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    try {
                        List<Website> websites = websiteRepository.findAll();

                        int totalClicks = websites.stream()
                                .mapToInt(Website::getWebsiteClicks)
                                .sum();

                        Platform.runLater(() ->
                            totalClicksLabel.setText(String.valueOf(totalClicks)));

                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }),
                new KeyFrame(Duration.seconds(1))
        );

        clicksTimeLine.setCycleCount(Animation.INDEFINITE);
        clicksTimeLine.play();

    }
}
