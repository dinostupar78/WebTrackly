package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.TrafficRecordDbRepository;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository;
import hr.javafx.webtrackly.app.model.DataSerialization;
import hr.javafx.webtrackly.app.model.TrafficRecord;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class TrafficRecordEditController {
    @FXML
    private ComboBox<Website> trafficRecordEditComboBoxWebsite;

    @FXML
    private DatePicker trafficRecordEditDatePickerTimeOfVisit;

    @FXML
    private TextField trafficRecordEditTextFieldUserCount;

    @FXML
    private TextField trafficRecordEditTextFieldPageViews;

    @FXML
    private TextField trafficRecordEditTextFieldBounceRate;

    private TrafficRecord trafficRecord;

    private WebsiteDbRepository<Website> websiteRepository = new WebsiteDbRepository<>();
    private TrafficRecordDbRepository<TrafficRecord> trafficRecordRepository = new TrafficRecordDbRepository<>();

    public void setTrafficRecordData(TrafficRecord trafficRecord) {
        this.trafficRecord = trafficRecord;

        trafficRecordEditComboBoxWebsite.setValue(trafficRecord.getWebsite());
        trafficRecordEditDatePickerTimeOfVisit.setValue(trafficRecord.getTimeOfVisit().toLocalDate());
        trafficRecordEditTextFieldUserCount.setText(trafficRecord.getUserCount().toString());
        trafficRecordEditTextFieldPageViews.setText(trafficRecord.getPageViews().toString());
        trafficRecordEditTextFieldBounceRate.setText(trafficRecord.getBounceRate().toString());
    }

    public void initialize(){
        trafficRecordEditComboBoxWebsite.getItems().setAll(websiteRepository.findAll());
    }

    public void editTrafficRecord(){
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultEdit();

        if (result.isPresent() && result.get() == ButtonType.OK){
            StringBuilder errorMessages = new StringBuilder();

            Website website = trafficRecordEditComboBoxWebsite.getValue();
            if (website == null) {
                errorMessages.append("Website is required!\n");
            }

            LocalDate dateOfVisit = trafficRecordEditDatePickerTimeOfVisit.getValue();
            LocalDateTime timeOfVisit = null;
            if (dateOfVisit != null) {
                timeOfVisit = dateOfVisit.atTime(LocalTime.of(23, 59));
            } else {
                errorMessages.append("Time of visit is required!\n");
            }


            Integer userCount = Integer.parseInt(trafficRecordEditTextFieldUserCount.getText());
            if(userCount < 0){
                errorMessages.append("User count must be a positive number!\n");
            }

            Integer pageViews = Integer.parseInt(trafficRecordEditTextFieldPageViews.getText());
            if(pageViews < 0){
                errorMessages.append("Page views must be a positive number!\n");
            }

            BigDecimal bounceRate = new BigDecimal(trafficRecordEditTextFieldBounceRate.getText());
            if(bounceRate.compareTo(BigDecimal.ZERO) < 0 ){
                errorMessages.append("Bounce rate must be a positive number!\n");
            }

            if (errorMessages.length() > 0) {
                ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
            } else {

                TrafficRecord newTrafficRecord = new TrafficRecord.Builder()
                        .setId(trafficRecord.getId())
                        .setWebsite(website)
                        .setTimeOfVisit(timeOfVisit)
                        .setUserCount(userCount)
                        .setPageViews(pageViews)
                        .setBounceRate(bounceRate)
                        .build();

                trafficRecordRepository.update(newTrafficRecord);

                DataSerialization change = new DataSerialization(
                        "Traffic Record Edited",
                        trafficRecord.toString(),
                        newTrafficRecord.toString(),
                        website.getWebsiteName(),
                        LocalDateTime.now()
                );

                DataSerializeUtil.serializeData(change);

                ShowAlertUtil.showAlert("Success", "Traffic record updated successfully!", Alert.AlertType.INFORMATION);

                trafficRecordEditComboBoxWebsite.getSelectionModel().clearSelection();
                trafficRecordEditDatePickerTimeOfVisit.getEditor().clear();
                trafficRecordEditTextFieldUserCount.clear();
                trafficRecordEditTextFieldPageViews.clear();
                trafficRecordEditTextFieldBounceRate.clear();


            }


        } else{
            ShowAlertUtil.showAlert("Error", "Session not updated!", Alert.AlertType.ERROR);
        }
    }
}
