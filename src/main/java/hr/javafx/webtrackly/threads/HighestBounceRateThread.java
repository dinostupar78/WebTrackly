package hr.javafx.webtrackly.threads;

import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.model.Website;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HighestBounceRateThread implements Runnable{
    @FXML
    private Label highestBounceRateLabel;

    private WebsiteDbRepository1<Website> websiteRepository;

    public HighestBounceRateThread(WebsiteDbRepository1<Website> websiteRepository, Label highestBounceRateLabel) {
        this.websiteRepository = websiteRepository;
        this.highestBounceRateLabel = highestBounceRateLabel;
    }

    @Override
    public void run() {
//        Timeline clicksTimeLine = new Timeline(
//                new KeyFrame(Duration.ZERO, e -> {
//                    try {
//                        List<Website> websites = websiteRepository.findAll();
//
//                        Optional<Website> highestBounceRate = websites.stream()
//                                .max(Comparator.comparing(Website::getBounceRate));
//
//                        Platform.runLater(() ->
//                                highestBounceRateLabel.setText("Highest Bounce Rate: " + highestBounceRate.get().getBounceRate()));
//
//                    } catch (Exception f) {
//                        f.printStackTrace();
//                    }
//                }),
//                new KeyFrame(Duration.seconds(1))
//        );
//
//        clicksTimeLine.setCycleCount(Animation.INDEFINITE);
//        clicksTimeLine.play();

    }
}
