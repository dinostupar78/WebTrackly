package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.WebsiteDbRepository;
import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.app.model.Website;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static hr.javafx.webtrackly.main.HelloApplication.log;

public class WebsiteController {
    @FXML
    private TextField websiteSearchTextField;

    @FXML
    private LineChart<String, Number> newUsersLineChart;
    @FXML
    private PieChart pieChartSources;
    @FXML
    private AreaChart<String, Number> clicksAreaChart;
    @FXML
    private BarChart<String, Number> ageBarChart;

    private WebsiteDbRepository<Website> websiteRepository = new WebsiteDbRepository<>();

    private Website currentWebsite;

    public void showWebsiteData() {
        String searchQuery = websiteSearchTextField.getText();

        try{
            List<Website> websites = websiteRepository.findAll();
            currentWebsite = websites.stream()
                    .filter(w -> w.getWebsiteName().equalsIgnoreCase(searchQuery))
                    .findFirst()
                    .orElse(null);

            populateNewUsersLineChart();
            populateUsersByNationalityPieChart();
            populateClicksAreaChart();
            populateUsersByAgeBarChart();


        } catch (RepositoryAccessException e) {
            log.info("Error while fetching website data: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void populateNewUsersLineChart() {
        newUsersLineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("New Users");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoff = now.minusHours(48);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:00");

        Map<String, Long> usersByHour = currentWebsite.getUsers().stream()
                .filter(user -> user.getRegistrationDate().isAfter(cutoff))
                .collect(Collectors.groupingBy(
                        user -> user.getRegistrationDate().format(formatter),
                        Collectors.counting()
                ));

        usersByHour.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                });
        newUsersLineChart.getData().add(series);
    }

    private void populateUsersByNationalityPieChart() {
        pieChartSources.getData().clear();
        Map<String, Long> countByNationality = currentWebsite.getUsers().stream()
                .collect(Collectors.groupingBy(
                        user -> user.getPersonalData().nationality(),
                        Collectors.counting()
                ));
        countByNationality.forEach((nationality, count) -> {
            pieChartSources.getData().add(new PieChart.Data(nationality, count));
        });
    }

    private void populateClicksAreaChart() {
        clicksAreaChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Clicks");

        // For demonstration, we simulate hourly click counts by evenly dividing total clicks.
        // In a real app, you would query a CLICK_LOG table.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoff = now.minusHours(48);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:00");
        int totalClicks = currentWebsite.getWebsiteClicks();
        int hours = 48;
        int clicksPerHour = totalClicks / hours;
        for (int i = 0; i < hours; i++) {
            LocalDateTime time = cutoff.plusHours(i);
            String timeLabel = time.format(formatter);
            series.getData().add(new XYChart.Data<>(timeLabel, clicksPerHour));
        }
        clicksAreaChart.getData().add(series);
    }

    private void populateUsersByAgeBarChart() {
        ageBarChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Users by Age");

        // Group users into age bins: <18, 18-24, 25-34, 35-44, 45-54, 55+
        Map<String, Long> ageGroups = currentWebsite.getUsers().stream()
                .collect(Collectors.groupingBy(user -> {
                    // Compute age from dateOfBirth:
                    int age = Period.between(user.getPersonalData().dateOfBirth(), LocalDate.now()).getYears();
                    if (age < 18) return "<18";
                    else if (age <= 24) return "18-24";
                    else if (age <= 34) return "25-34";
                    else if (age <= 44) return "35-44";
                    else if (age <= 54) return "45-54";
                    else return "55+";
                }, Collectors.counting()));

        // Define the order of groups
        String[] ageOrder = {"<18", "18-24", "25-34", "35-44", "45-54", "55+"};
        for (String group : ageOrder) {
            Long count = ageGroups.getOrDefault(group, 0L);
            series.getData().add(new XYChart.Data<>(group, count));
        }
        ageBarChart.getData().add(series);
    }













}
