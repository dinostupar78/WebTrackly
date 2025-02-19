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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequentDomainsThread implements Runnable {
    @FXML
    private Label mostFrequentDomainLabel;

    @FXML
    private Label mostFrequentDomainCountLabel;


    private WebsiteDbRepository1<Website> websiteRepository;

    public FrequentDomainsThread(WebsiteDbRepository1<Website> websiteRepository, Label mostFrequentDomainLabel, Label mostFrequentDomainCountLabel) {
        this.websiteRepository = websiteRepository;
        this.mostFrequentDomainLabel = mostFrequentDomainLabel;
        this.mostFrequentDomainCountLabel = mostFrequentDomainCountLabel;
    }

    @Override
    public void run() {
        Timeline frequentDomainsTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    try {
                        List<Website> websites = websiteRepository.findAll();
                        Map<String, Integer> domainCount = new HashMap<>();

                        for (Website website : websites) {
                            String domain = extractDomain(website.getWebsiteUrl());
                            domainCount.put(domain, domainCount.getOrDefault(domain, 0) + 1);
                        }

                        String mostFrequentDomain = domainCount.entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse("No Data");

                        int frequency = domainCount.get(mostFrequentDomain);
                        String displayText = "" + mostFrequentDomain;
                        String displayCount = "Number of Domains: " + frequency;

                        Platform.runLater(() -> {
                            mostFrequentDomainLabel.setText(displayCount);
                            mostFrequentDomainCountLabel.setText(displayText);
                        });


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }),
                new KeyFrame(Duration.seconds(1))
        );

        frequentDomainsTimeline.setCycleCount(Animation.INDEFINITE);
        frequentDomainsTimeline.play();
    }

    private String extractDomain(String url) {
        String[] parts = url.split("/");
        if (parts.length < 3) {
            return url;
        }
        String domain = parts[2];
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

}


