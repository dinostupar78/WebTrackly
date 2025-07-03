package hr.javafx.webtrackly.utils;

import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.model.Website;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebsiteLabelUtil {
    private final WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();

    private final Label mostFrequentDomainLabel;
    private final Label mostFrequentDomainCountLabel;

    public WebsiteLabelUtil(Label mostFrequentDomainLabel, Label mostFrequentDomainCountLabel) {
        this.mostFrequentDomainLabel = mostFrequentDomainLabel;
        this.mostFrequentDomainCountLabel = mostFrequentDomainCountLabel;
    }

    public List<Website> fetchAllWebsites() {
        return websiteRepository.findAll();
    }

    private String extractDomain(String url) {
        String[] parts = url.split("/");
        if (parts.length < 3) {
            return url;
        }
        String domain = parts[2];
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public void frequentDomainLabel(){
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

        Integer frequency = domainCount.get(mostFrequentDomain);
        String displayText = "" + mostFrequentDomain;
        String displayCount = "Number of Domains: " + frequency;

        Platform.runLater(() -> {
            mostFrequentDomainLabel.setText(displayCount);
            mostFrequentDomainCountLabel.setText(displayText);
        });


    }




}
