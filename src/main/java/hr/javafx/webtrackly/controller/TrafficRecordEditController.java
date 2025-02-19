package hr.javafx.webtrackly.controller;
import hr.javafx.webtrackly.app.db.TrafficRecordDbRepository2;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.model.DataSerialization;
import hr.javafx.webtrackly.app.model.TrafficRecord;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
    private TrafficRecordDbRepository2<TrafficRecord> trafficRecordRepository = new TrafficRecordDbRepository2<>();

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
            Optional<Website> optWebsite = Optional.ofNullable(website);
            if (optWebsite.isPresent()) {
                website = optWebsite.get();
            } else {
                errorMessages.append("Website is required!\n");
            }
            String websiteName = Optional.ofNullable(website)
                    .map(Website::getWebsiteName)
                    .orElse("Unknown Website");

            LocalDateTime timeOfVisit = trafficRecordEditDatePickerTimeOfVisit.getValue().atStartOfDay();
            Optional<LocalDateTime> optDate = Optional.ofNullable(timeOfVisit);
            if (optDate.isPresent()) {
                timeOfVisit = optDate.get();
            } else {
                errorMessages.append("Time of visit is required!\n");
            }

            Integer userCount = Integer.parseInt(trafficRecordEditTextFieldUserCount.getText());
            if(userCount < 0 || trafficRecordEditTextFieldUserCount.getText().isEmpty()){
                errorMessages.append("User count is required and must be a positive number!\n");
            }

            Integer pageViews = Integer.parseInt(trafficRecordEditTextFieldPageViews.getText());
            if(pageViews < 0 || trafficRecordEditTextFieldPageViews.getText().isEmpty()){
                errorMessages.append("Page views are required and must be a positive number!\n");
            }

            BigDecimal bounceRate = new BigDecimal(trafficRecordEditTextFieldBounceRate.getText());
            if(bounceRate.compareTo(BigDecimal.ZERO) < 0 || bounceRate.compareTo(BigDecimal.valueOf(100)) > 0){
                errorMessages.append("Bounce rate must be a positive number less than 100!\n");
            }


            if (errorMessages.length() > 0) {
                ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
            } else {
                String oldTrafficRecord = trafficRecord.toString();

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
                        oldTrafficRecord,
                        newTrafficRecord.toString(),
                        websiteName,
                        LocalDateTime.now()
                );

                DataSerializeUtil.serializeData(change);
                ShowAlertUtil.showAlert("Success", "Traffic record updated successfully!", Alert.AlertType.INFORMATION);
                clearForm();
            }

        } else{
            ShowAlertUtil.showAlert("Error", "Session not updated!", Alert.AlertType.ERROR);
        }
    }

    public void clearForm() {
        trafficRecordEditComboBoxWebsite.getSelectionModel().clearSelection();
        trafficRecordEditDatePickerTimeOfVisit.getEditor().clear();
        trafficRecordEditTextFieldUserCount.clear();
        trafficRecordEditTextFieldPageViews.clear();
        trafficRecordEditTextFieldBounceRate.clear();
    }
}
