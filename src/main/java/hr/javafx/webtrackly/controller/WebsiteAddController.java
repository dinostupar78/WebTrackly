package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.WebsiteDbRepository;
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

    private WebsiteDbRepository<Website> websiteRepository = new WebsiteDbRepository<>();

    private User getCurrentUser() {
        return new User.Builder()
                .setRole(new AdminRole())
                .build();
    }

    public void addWebsite() {
        StringBuilder errorMessages = new StringBuilder();

        if (websiteTextFieldName.getText().isEmpty()) {
            errorMessages.append("Name is required!\n");
        }

        if (websiteTextFieldClicks.getText().isEmpty()) {
            errorMessages.append("Clicks is required!\n");
        }

        if (websiteTextFieldUrl.getText().isEmpty()) {
            errorMessages.append("Url is required!\n");
        }

        if (websiteTextFieldUsers.getText().isEmpty()) {
            errorMessages.append("Users is required!\n");
        }

        if (websiteTextFieldBounceRate.getText().isEmpty()) {
            errorMessages.append("Bounce rate is required!\n");
        }

        if (errorMessages.length() > 0){
            ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
        } else {
            Website newWebsite = new Website.Builder()
                    .setWebsiteName(websiteTextFieldName.getText())
                    .setWebsiteClicks(Integer.parseInt(websiteTextFieldClicks.getText()))
                    .setWebsiteUrl(websiteTextFieldUrl.getText())
                    .setWebsiteUserCount(Integer.parseInt(websiteTextFieldUsers.getText()))
                    .setBounceRate(BigDecimal.valueOf(Integer.parseInt(websiteTextFieldBounceRate.getText())))
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
