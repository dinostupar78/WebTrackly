package hr.javafx.webtrackly.utils;

import hr.javafx.webtrackly.app.db.UserActionDbRepository1;
import hr.javafx.webtrackly.app.db.UserDbRepository1;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.enums.WebsiteType;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.UserAction;
import hr.javafx.webtrackly.app.model.Website;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WebsiteLabelUtil {
    private final WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
    private final UserDbRepository1<User> userRepository = new UserDbRepository1<>();
    private final UserActionDbRepository1<UserAction> userActionRepository = new UserActionDbRepository1<>();


    private final Label mostFrequentCategoryLabel;
    private final Label mostFrequentCategoryCountLabel;
    private final Label mostFrequentDomainLabel;
    private final Label mostFrequentDomainCountLabel;
    private final Label totalUsersLabel;
    private final Label mostUsersLabel;

    public WebsiteLabelUtil(Label mostFrequentDomainLabel, Label mostFrequentDomainCountLabel,
                            Label mostFrequentCategoryLabel, Label mostFrequentCategoryCountLabel,
                            Label totalUsersLabel, Label mostUsersLabel) {
        this.mostFrequentDomainLabel = mostFrequentDomainLabel;
        this.mostFrequentDomainCountLabel = mostFrequentDomainCountLabel;
        this.mostFrequentCategoryLabel = mostFrequentCategoryLabel;
        this.mostFrequentCategoryCountLabel = mostFrequentCategoryCountLabel;
        this.totalUsersLabel = totalUsersLabel;
        this.mostUsersLabel = mostUsersLabel;
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

    public void totalUsersLabel() {
        List<Website> sites = websiteRepository.findAll();
        // sum up the size of each site's user set
        int totalUsers = sites.stream()
                .mapToInt(w -> w.getUsers().size())
                .sum();

        Platform.runLater(() ->
                totalUsersLabel.setText(String.valueOf(totalUsers))
        );
    }

    public void mostUsersLabel() {
        List<Website> sites = websiteRepository.findAll();

        Map<String,Integer> usersBySite = sites.stream()
                .collect(Collectors.toMap(
                        Website::getWebsiteName,
                        w -> w.getUsers().size()
                ));

        Map.Entry<String,Integer> max = usersBySite.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(Map.entry("N/A", 0));

        Platform.runLater(() ->
                mostUsersLabel.setText("Users: " + max.getKey())
        );

    }

    public void frequentCategoryLabel() {
        List<Website> sites = websiteRepository.findAll();

        Map<WebsiteType, Long> countByCat = sites.stream()
                .collect(Collectors.groupingBy(
                        Website::getWebsiteCategory,
                        Collectors.counting()
                ));

        Map.Entry<WebsiteType, Long> best = countByCat.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(Map.entry(WebsiteType.OTHER, 0L));

        String catName = best.getKey().name();
        Long count = best.getValue();

        Platform.runLater(() -> {
            mostFrequentCategoryLabel.setText(catName);
            mostFrequentCategoryCountLabel.setText("Number of Sites: " + count);
        });
    }




}







