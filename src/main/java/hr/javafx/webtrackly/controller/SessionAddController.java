package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.SessionDbRepository1;
import hr.javafx.webtrackly.app.db.TrafficRecordDbRepository1;
import hr.javafx.webtrackly.app.db.UserDbRepository1;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.enums.DeviceType;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static hr.javafx.webtrackly.utils.DateFormatterUtil.formatLocalDateTime;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class SessionAddController {
    @FXML
    private ComboBox<Website> sessionComboBoxWebsite;

    @FXML
    private ComboBox<User> sessionComboBoxUser;

    @FXML
    private ComboBox<DeviceType> sessionComboBoxDeviceType;

    @FXML
    private TextField sessionTextFieldDuration;

    @FXML
    private DatePicker sessionDatePickerStartTime;

    @FXML
    private DatePicker sessionDatePickerEndTime;

    @FXML
    private ComboBox<Boolean> sessionComboBoxActivity;

    @FXML
    private ComboBox<TrafficRecord> sessionComboBoxTrafficRecord;

    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
    private UserDbRepository1<User> userRepository = new UserDbRepository1<>();
    private SessionDbRepository1<Session> sessionRepository = new SessionDbRepository1<>();
    private TrafficRecordDbRepository1<TrafficRecord> trafficRecordRepository = new TrafficRecordDbRepository1<>();

    public void initialize() {
        sessionComboBoxWebsite.getItems().setAll(websiteRepository.findAll());
        sessionComboBoxUser.getItems().setAll(userRepository.findAll());
        sessionComboBoxDeviceType.getItems().setAll(DeviceType.values());
        sessionComboBoxActivity.getItems().setAll(TRUE, FALSE);
        sessionComboBoxTrafficRecord.getItems().setAll(trafficRecordRepository.findAll());
    }

    public void addSession() {
        StringBuilder errorMessages = new StringBuilder();

        Website website = sessionComboBoxWebsite.getValue();
        Optional<Website> optWebsite = Optional.ofNullable(website);
        if (optWebsite.isPresent()) {
            website = optWebsite.get();
        } else {
            errorMessages.append("Website is required!\n");
        }

        User user = sessionComboBoxUser.getValue();
        Optional<User> optUser = Optional.ofNullable(user);
        if (optUser.isPresent()) {
            user = optUser.get();
        } else {
            errorMessages.append("User is required!\n");
        }

        DeviceType deviceType = sessionComboBoxDeviceType.getValue();
        Optional<DeviceType> optDeviceType = Optional.ofNullable(deviceType);
        if (optDeviceType.isPresent()) {
            deviceType = optDeviceType.get();
        } else {
            errorMessages.append("Device type is required!\n");
        }

        BigDecimal duration = BigDecimal.ZERO;
        try {
            duration = new BigDecimal(sessionTextFieldDuration.getText());
            if (duration.compareTo(BigDecimal.ZERO) <= 0) {
                errorMessages.append("Duration must be greater than zero!\n");
            }
        } catch (NumberFormatException e) {
            errorMessages.append("Invalid duration format!\n");
        }

        LocalDate startDate = sessionDatePickerStartTime.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.now());
        Optional<LocalDateTime> optStartDate = Optional.ofNullable(startDateTime);
        if(optStartDate.isPresent()){
            startDateTime = optStartDate.get();
            formatLocalDateTime(startDateTime);
        } else {
            errorMessages.append("Start date is required!\n");
        }


        LocalDate endDate = sessionDatePickerStartTime.getValue();
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.now());
        Optional<LocalDateTime> optEndDate = Optional.ofNullable(endDateTime);
        if(optEndDate.isPresent()){
            endDateTime = optEndDate.get();
            formatLocalDateTime(endDateTime);
        } else {
            errorMessages.append("Start date is required!\n");
        }

        Boolean activity = sessionComboBoxActivity.getValue();
        Optional<Boolean> optActivity = Optional.ofNullable(activity);
        if (optActivity.isPresent()) {
            activity = optActivity.get();
        } else {
            errorMessages.append("Activity is required!\n");
        }

        TrafficRecord selectedTrafficRecord = sessionComboBoxTrafficRecord.getValue();
        Optional<TrafficRecord> optTrafficRecord = Optional.ofNullable(selectedTrafficRecord);
        if (optTrafficRecord.isPresent()) {
            selectedTrafficRecord = optTrafficRecord.get();
        } else {
            errorMessages.append("Traffic record is required!\n");
        }


        if (errorMessages.length() > 0) {
            ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
        } else {
            Session newSession = new Session.Builder()
                    .setWebsite(website)
                    .setUser(user)
                    .setDeviceType(deviceType)
                    .setSessionDuration(duration)
                    .setStartTime(startDateTime)
                    .setEndTime(endDateTime)
                    .setActive(activity)
                    .setTrafficRecordId(selectedTrafficRecord.getId())
                    .build();

            sessionRepository.save(newSession);

            DataSerialization change = new DataSerialization(
                    "Session Added",
                    "N/A",
                    newSession.toString(),
                    user.getRole().toString(),
                    LocalDateTime.now()
            );

            DataSerializeUtil.serializeData(change);

            ShowAlertUtil.showAlert("Session successfully added", "Session is successfully added!", Alert.AlertType.INFORMATION);

            sessionComboBoxWebsite.getSelectionModel().clearSelection();
            sessionComboBoxUser.getSelectionModel().clearSelection();
            sessionComboBoxDeviceType.getSelectionModel().clearSelection();
            sessionTextFieldDuration.clear();
            sessionDatePickerStartTime.getEditor().clear();
            sessionDatePickerEndTime.getEditor().clear();
            sessionComboBoxActivity.getSelectionModel().clearSelection();
            sessionComboBoxTrafficRecord.getSelectionModel().clearSelection();

        }
    }

}
