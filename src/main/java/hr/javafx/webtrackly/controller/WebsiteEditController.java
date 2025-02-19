package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.WebsiteDbRepository2;
import hr.javafx.webtrackly.app.model.DataSerialization;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    private WebsiteDbRepository2<Website> websiteRepository = new WebsiteDbRepository2<>();
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

            String name = websiteEditTextFieldName.getText();
            if (name.isEmpty()) {
                errorMessages.append("Name is required!\n");
            }

            Integer clicks = Integer.parseInt(websiteEditTextFieldClicks.getText());
            if (clicks < 0 || websiteEditTextFieldClicks.getText().isEmpty()) {
                errorMessages.append("Clicks are required and needs to be a positive number!\n");
            }

            String url = websiteEditTextFieldUrl.getText();
            if (url.isEmpty()) {
                errorMessages.append("Url is required!\n");
            }

            Integer users = Integer.parseInt(websiteEditTextFieldUsers.getText());
            if (users < 0 || websiteEditTextFieldUsers.getText().isEmpty()) {
                errorMessages.append("Users are required and needs to be positive number!\n");
            }

            BigDecimal bounceRate = new BigDecimal(websiteEditTextFieldBounceRate.getText());
            if(bounceRate.compareTo(BigDecimal.ZERO) < 0 || bounceRate.compareTo(BigDecimal.valueOf(100)) > 0){
                errorMessages.append("Bounce rate must be a positive number less than 100!\n");
            }

            if (errorMessages.length() > 0){
                ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
            } else {
                String oldWebsiteData = website.toString();

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

                DataSerialization change = new DataSerialization(
                        "Website Edited",
                        oldWebsiteData,
                        newWebsite.toString(),
                        Optional.ofNullable(website.getWebsiteName()).orElse("Unknown Website"),
                        LocalDateTime.now()
                );

                DataSerializeUtil.serializeData(change);
                ShowAlertUtil.showAlert("Success", "Website updated!", Alert.AlertType.INFORMATION);
                clearForm();
            }

        } else{
            ShowAlertUtil.showAlert("Error", "Website not updated!", Alert.AlertType.ERROR);
        }
    }

    private void clearForm(){
        websiteEditTextFieldName.clear();
        websiteEditTextFieldClicks.clear();
        websiteEditTextFieldUrl.clear();
        websiteEditTextFieldUsers.clear();
        websiteEditTextFieldBounceRate.clear();
    }

}
