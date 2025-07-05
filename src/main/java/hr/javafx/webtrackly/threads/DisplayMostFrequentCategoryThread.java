package hr.javafx.webtrackly.threads;

import hr.javafx.webtrackly.app.db.WebsiteDbRepository3;
import hr.javafx.webtrackly.app.enums.WebsiteType;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Optional;

public class DisplayMostFrequentCategoryThread implements Runnable{

    private final WebsiteDbRepository3 wbRepository;
    private final Label categoryLabel;
    private final Label countLabel;

    public DisplayMostFrequentCategoryThread(WebsiteDbRepository3 wbRepository, Label categoryLabel,
                                             Label countLabel) {
        this.wbRepository = wbRepository;
        this.categoryLabel = categoryLabel;
        this.countLabel = countLabel;
    }

    @Override
    public void run() {
        Optional<WebsiteType> topCategoryOptional = wbRepository.findMostFrequentCategory();

        if (topCategoryOptional.isPresent()) {
            WebsiteType topCat = topCategoryOptional.get();
            Integer count = wbRepository.countByCategory(topCat);

            Platform.runLater(() -> {
                categoryLabel.setText("Number of Sites: " + count);
                countLabel.setText(topCat.name());
            });

        } else {
            Platform.runLater(() -> {
                categoryLabel.setText("N/A");
                countLabel   .setText("Number of Sites: N/A");
            });
        }

    }
}
