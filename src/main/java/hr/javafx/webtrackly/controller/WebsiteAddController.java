package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.model.AdminRole;
import hr.javafx.webtrackly.app.model.DataSerialization;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

public class WebsiteAddController {
    @FXML
    private TextField websiteTextFieldName;

    @FXML
    private TextField websiteTextFieldClicks;

    @FXML
    private TextField websiteTextFieldUrl;

    @FXML
    private TextField websiteTextFieldUsers;

    @FXML
    private TextField websiteTextFieldBounceRate;

    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();

    private User getCurrentUser() {
        return new User.Builder()
                .setRole(new AdminRole())
                .build();
    }

    public void addWebsite() {
        StringBuilder errorMessages = new StringBuilder();

        String name = websiteTextFieldName.getText();
        if (name.isEmpty()) {
            errorMessages.append("Name is required!\n");
        }

        Integer clicks = Integer.parseInt(websiteTextFieldClicks.getText());
        if (clicks == null) {
            errorMessages.append("Clicks is required!\n");
        }

        String url = websiteTextFieldUrl.getText();
        if (url.isEmpty()) {
            errorMessages.append("Url is required!\n");
        }

        Integer users = Integer.parseInt(websiteTextFieldUsers.getText());
        if (users == null) {
            errorMessages.append("Users is required!\n");
        }

        BigDecimal bounceRate = BigDecimal.valueOf(Integer.parseInt(websiteTextFieldBounceRate.getText()));
        if (bounceRate == null) {
            errorMessages.append("Bounce rate is required!\n");
        }

        if (errorMessages.length() > 0){
            ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
        } else {
            Website newWebsite = new Website.Builder()
                    .setWebsiteName(name)
                    .setWebsiteClicks(clicks)
                    .setWebsiteUrl(url)
                    .setWebsiteUserCount(users)
                    .setBounceRate(bounceRate)
                    .setUsers(new HashSet<>())
                    .build();

            websiteRepository.save(newWebsite);

            User currentUser = getCurrentUser();
            String roleInfo = (currentUser != null && currentUser.getRole() != null)
                    ? currentUser.getRole().toString() : "Unknown";

            DataSerialization change = new DataSerialization(
                    "Website Added",
                    "N/A",
                    newWebsite.toString(),
                    roleInfo,
                    LocalDateTime.now()
            );

            DataSerializeUtil.serializeData(change);

            ShowAlertUtil.showAlert("Website added", "Website successfully added!", Alert.AlertType.INFORMATION);
            StringBuilder sb = new StringBuilder();
            sb.append("Name: ").append(newWebsite.getWebsiteName()).append("\n")
                    .append("Clicks: ").append(newWebsite.getWebsiteClicks()).append("\n")
                    .append("Url: ").append(newWebsite.getWebsiteUrl()).append("\n")
                    .append("Users: ").append(newWebsite.getWebsiteUserCount()).append("\n")
                    .append("Bounce rate: ").append(newWebsite.getBounceRate()).append("\n");

            websiteTextFieldName.clear();
            websiteTextFieldClicks.clear();
            websiteTextFieldUrl.clear();
            websiteTextFieldUsers.clear();
            websiteTextFieldBounceRate.clear();

        }
    }
}
