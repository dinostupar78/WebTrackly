package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.WebsiteDbRepository;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

public class WebsiteEditController {
    @FXML
    private TextField websiteEditTextFieldName;

    @FXML
    private TextField websiteEditTextFieldClicks;

    @FXML
    private TextField websiteEditTextFieldUrl;

    @FXML
    private TextField websiteEditTextFieldUsers;

    @FXML
    private TextField websiteEditTextFieldBounceRate;

    private WebsiteDbRepository<Website> websiteRepository = new WebsiteDbRepository<>();
    private Website website;

    public void setWebsite(Website website){
        this.website = website;

        websiteEditTextFieldName.setText(website.getWebsiteName());
        websiteEditTextFieldClicks.setText(String.valueOf(website.getWebsiteClicks()));
        websiteEditTextFieldUrl.setText(website.getWebsiteUrl());
        websiteEditTextFieldUsers.setText(String.valueOf(website.getWebsiteUserCount()));
        websiteEditTextFieldBounceRate.setText(String.valueOf(website.getBounceRate()));
    }

    public void editWebsite(){
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultEdit();
        if (result.isPresent() && result.get() == ButtonType.OK){
            StringBuilder errorMessages = new StringBuilder();

            if (websiteEditTextFieldName.getText().isEmpty()) {
                errorMessages.append("Name is required!\n");
            }

            if (websiteEditTextFieldClicks.getText().isEmpty()) {
                errorMessages.append("Clicks is required!\n");
            }

            if (websiteEditTextFieldUrl.getText().isEmpty()) {
                errorMessages.append("Url is required!\n");
            }

            if (websiteEditTextFieldUsers.getText().isEmpty()) {
                errorMessages.append("Users is required!\n");
            }

            BigDecimal bounceRate = new BigDecimal(websiteEditTextFieldBounceRate.getText());
            if (bounceRate.compareTo(BigDecimal.ZERO) < 0) {
                errorMessages.append("Bounce rate is required!\n");
            }

            if (errorMessages.length() > 0){
                ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
            } else {
                Website newWebsite = new Website.Builder()
                        .setId(website.getId())
                        .setWebsiteName(websiteEditTextFieldName.getText())
                        .setWebsiteClicks(Integer.parseInt(websiteEditTextFieldClicks.getText()))
                        .setWebsiteUrl(websiteEditTextFieldUrl.getText())
                        .setWebsiteUserCount(Integer.parseInt(websiteEditTextFieldUsers.getText()))
                        .setBounceRate(bounceRate)
                        .setUsers(new HashSet<>())
                        .build();

                websiteRepository.update(newWebsite);

                ShowAlertUtil.showAlert("Success", "Website updated!", Alert.AlertType.INFORMATION);

                websiteEditTextFieldName.clear();
                websiteEditTextFieldClicks.clear();
                websiteEditTextFieldUrl.clear();
                websiteEditTextFieldUsers.clear();
                websiteEditTextFieldBounceRate.clear();

            }

        } else{
            ShowAlertUtil.showAlert("Error", "Website not updated!", Alert.AlertType.ERROR);

        }
    }

}
