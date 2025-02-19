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
import java.util.Optional;

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
        if (clicks < 0 || websiteTextFieldClicks.getText().isEmpty()) {
            errorMessages.append("Clicks are required and needs to be a positive number!\n");
        }

        String url = websiteTextFieldUrl.getText();
        if (url.isEmpty()) {
            errorMessages.append("Url is required!\n");
        }

        Integer users = Integer.parseInt(websiteTextFieldUsers.getText());
        if (users < 0 || websiteTextFieldUsers.getText().isEmpty()) {
            errorMessages.append("Users are required and needs to be positive number!\n");
        }

        BigDecimal bounceRate = new BigDecimal(websiteTextFieldBounceRate.getText());
        if(bounceRate.compareTo(BigDecimal.ZERO) < 0 || bounceRate.compareTo(BigDecimal.valueOf(100)) > 0){
            errorMessages.append("Bounce rate must be a positive number less than 100!\n");
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
            String roleInfo = Optional.ofNullable(currentUser)
                    .map(User::getRole)
                    .map(Object::toString)
                    .orElse("Unknown");

            DataSerialization change = new DataSerialization(
                    "Website Added",
                    "N/A",
                    newWebsite.toString(),
                    roleInfo,
                    LocalDateTime.now()
            );

            DataSerializeUtil.serializeData(change);
            ShowAlertUtil.showAlert("Success", "Website successfully added!", Alert.AlertType.INFORMATION);
            clearForm();

        }
    }

    private void clearForm(){
        websiteTextFieldName.clear();
        websiteTextFieldClicks.clear();
        websiteTextFieldUrl.clear();
        websiteTextFieldUsers.clear();
        websiteTextFieldBounceRate.clear();
    }
}
