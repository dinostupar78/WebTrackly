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

import java.math.BigDecimal;
import java.util.List;
import java.util.OptionalDouble;

public class AvgBounceRateThread implements Runnable{
    @FXML
    private Label avgBounceRateLabel;

    private WebsiteDbRepository1<Website> websiteRepository;

    public AvgBounceRateThread(WebsiteDbRepository1<Website> websiteRepository, Label avgBounceRateLabel) {
        this.websiteRepository = websiteRepository;
        this.avgBounceRateLabel = avgBounceRateLabel;
    }

    @Override
    public void run() {
        Timeline avgBounceRateTimeLine = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    try {
                        List<Website> websites = websiteRepository.findAll();

                        OptionalDouble  avgBounceRate = websites.stream()
                                .map(Website::getBounceRate)
                                .mapToDouble(BigDecimal::doubleValue)
                                .average();

                        Double formattedAvg = avgBounceRate.orElse(0.0);

                        Platform.runLater(() ->
                                avgBounceRateLabel.setText(String.format("%.2f%%", formattedAvg)));

                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }),
                new KeyFrame(Duration.seconds(1))
        );

        avgBounceRateTimeLine.setCycleCount(Animation.INDEFINITE);
        avgBounceRateTimeLine.play();

    }
}
