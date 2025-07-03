package hr.javafx.webtrackly.threads;

import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.model.Website;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
//        Timeline clicksTimeLine = new Timeline(
//                new KeyFrame(Duration.ZERO, e -> {
//                    try {
//                        List<Website> websites = websiteRepository.findAll();
//
//                        Optional<Website> highestClicks =
//
//                        Platform.runLater(() ->
//                                highestClicksLabel.setText("Highest Clicks: " + highestClicks.get().getWebsiteClicks()));
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
