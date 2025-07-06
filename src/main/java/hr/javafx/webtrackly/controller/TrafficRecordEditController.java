package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.TrafficRecordDbRepository2;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.LocalTimeStringConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TrafficRecordEditController {
    @FXML
    private ComboBox<Website> trafficRecordEditComboBoxWebsite;

    @FXML
    private DatePicker trafficRecordEditDatePickerDateOfVisit;

    @FXML
    private TextField trafficRecordEditDatePickerTimeOfVisit;

    private ObjectBinding<LocalDateTime> dateTimeBinding;

    private TrafficRecord trafficRecord;

    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
    private TrafficRecordDbRepository2<TrafficRecord> trafficRecordRepository = new TrafficRecordDbRepository2<>();

    public void setTrafficRecordData(TrafficRecord trafficRecord) {
        this.trafficRecord = trafficRecord;
        trafficRecordEditComboBoxWebsite.setValue(trafficRecord.getWebsite());
        trafficRecordEditDatePickerDateOfVisit.setValue(trafficRecord.getTimeOfVisit().toLocalDate());
        trafficRecordEditDatePickerTimeOfVisit.setText(trafficRecord.getTimeOfVisit().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));

    }

    public void initialize(){
        trafficRecordEditComboBoxWebsite.getItems().setAll(websiteRepository.findAll());

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        LocalTimeStringConverter converter = new LocalTimeStringConverter(timeFmt, timeFmt);
        TextFormatter<LocalTime> tf = new TextFormatter<>(converter, null);
        trafficRecordEditDatePickerTimeOfVisit.setTextFormatter(tf);

        dateTimeBinding = Bindings.createObjectBinding(() -> {
            LocalDate ld = trafficRecordEditDatePickerDateOfVisit.getValue();
            LocalTime lt = tf.getValue();
            return ld == null || lt == null ? null : LocalDateTime.of(ld, lt);
        }, trafficRecordEditDatePickerDateOfVisit.valueProperty(), tf.valueProperty());
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

            LocalDateTime timeOfVisit = dateTimeBinding.get();
            Optional<LocalDateTime> optDate = Optional.ofNullable(timeOfVisit);
            if (optDate.isPresent()) {
                timeOfVisit = optDate.get();
            } else {
                errorMessages.append("Time of visit is required!\n");
            }

            if (errorMessages.length() > 0) {
                ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
            } else {
                String oldTrafficRecord = trafficRecord.toString();

                TrafficRecord newTrafficRecord = new TrafficRecord.Builder()
                        .setId(trafficRecord.getId())
                        .setWebsite(website)
                        .setTimeOfVisit(timeOfVisit)
                        .build();

                trafficRecordRepository.update(newTrafficRecord);

                String roleString = Optional.ofNullable(UserSession.getInstance().getCurrentUser())
                        .map(User::getRole)
                        .map(Role::toString)
                        .orElse("UNKNOWN");

                DataSerialization change = new DataSerialization(
                        "Traffic Record Edited",
                        oldTrafficRecord,
                        newTrafficRecord.toString(),
                        roleString,
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
        trafficRecordEditDatePickerDateOfVisit.getEditor().clear();
        trafficRecordEditDatePickerTimeOfVisit.clear();

    }
}
