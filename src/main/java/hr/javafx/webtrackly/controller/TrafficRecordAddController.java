package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.TrafficRecordDbRepository1;
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

public class TrafficRecordAddController {
    @FXML
    private ComboBox<Website> trafficRecordComboBoxWebsite;

    @FXML
    private DatePicker trafficRecordDatePickerDateOfVisit;

    @FXML
    private TextField trafficRecordDatePickerTimeOfVisit;


    private ObjectBinding<LocalDateTime> dateTimeBinding;

    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
    private TrafficRecordDbRepository1<TrafficRecord> trafficRecordRepository = new TrafficRecordDbRepository1<>();

    public void initialize() {
        trafficRecordComboBoxWebsite.getItems().setAll(websiteRepository.findAll());

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        LocalTimeStringConverter converter = new LocalTimeStringConverter(timeFmt, timeFmt);
        TextFormatter<LocalTime> tf = new TextFormatter<>(converter, null);
        trafficRecordDatePickerTimeOfVisit.setTextFormatter(tf);

        dateTimeBinding = Bindings.createObjectBinding(() -> {
            LocalDate ld = trafficRecordDatePickerDateOfVisit.getValue();
            LocalTime lt = tf.getValue();
            return ld == null || lt == null ? null : LocalDateTime.of(ld, lt);
        }, trafficRecordDatePickerDateOfVisit.valueProperty(), tf.valueProperty());
    }

    public void addTrafficRecord() {
        StringBuilder errorMessages = new StringBuilder();

        Website website = trafficRecordComboBoxWebsite.getValue();
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
            TrafficRecord newTrafficRecord = new TrafficRecord.Builder()
                    .setWebsite(website)
                    .setTimeOfVisit(timeOfVisit)
                    .build();

            trafficRecordRepository.save(newTrafficRecord);

            String roleString = Optional.ofNullable(UserSession.getInstance().getCurrentUser())
                    .map(User::getRole)
                    .map(Role::toString)
                    .orElse("UNKNOWN");

            DataSerialization change = new DataSerialization(
                    "Traffic Record Added",
                    "N/A",
                    newTrafficRecord.toString(),
                    roleString,
                    LocalDateTime.now()
            );

            DataSerializeUtil.serializeData(change);
            ShowAlertUtil.showAlert("Success", "Traffic Record has been successfully added!", Alert.AlertType.INFORMATION);
            clearForm();

        }
    }

    private void clearForm() {
        trafficRecordComboBoxWebsite.getSelectionModel().clearSelection();
        trafficRecordDatePickerDateOfVisit.getEditor().clear();
        trafficRecordDatePickerTimeOfVisit.clear();
    }
}
