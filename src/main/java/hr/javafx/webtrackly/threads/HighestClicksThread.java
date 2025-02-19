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
import java.util.Optional;

public class HighestClicksThread implements Runnable{
    @FXML
    private Label highestClicksLabel;

    private WebsiteDbRepository1<Website> websiteRepository;

    public HighestClicksThread(WebsiteDbRepository1<Website> websiteRepository, Label highestClicksLabel) {
        this.websiteRepository = websiteRepository;
        this.highestClicksLabel = highestClicksLabel;
    }

    @Override
    public void run() {
        Timeline clicksTimeLine = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    try {
                        List<Website> websites = websiteRepository.findAll();

                        Optional<Website> highestClicks = websites.stream()
                                .max((w1, w2) -> Integer.compare(w1.getWebsiteClicks(), w2.getWebsiteClicks()));

                        Platform.runLater(() ->
                                highestClicksLabel.setText("Highest Clicks: " + highestClicks.get().getWebsiteClicks()));

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
