package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.SessionDbRepository;
import hr.javafx.webtrackly.app.db.TrafficRecordDbRepository;
import hr.javafx.webtrackly.app.db.UserDbRepository;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository;
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

    private WebsiteDbRepository<Website> websiteRepository = new WebsiteDbRepository<>();
    private UserDbRepository<User> userRepository = new UserDbRepository<>();
    private SessionDbRepository<Session> sessionRepository = new SessionDbRepository<>();
    private TrafficRecordDbRepository<TrafficRecord> trafficRecordRepository = new TrafficRecordDbRepository<>();

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
        if (website == null) {
            errorMessages.append("Website is required!\n");
        }

        User user = sessionComboBoxUser.getValue();
        if (user == null) {
            errorMessages.append("User is required!\n");
        }

        DeviceType deviceType = sessionComboBoxDeviceType.getValue();
        if (deviceType == null) {
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
        LocalDateTime startTime = (startDate != null) ? startDate.atTime(LocalTime.of(0, 0)) : null;

        if (startTime == null) {
            errorMessages.append("Start time is required!\n");
        }

        LocalDate endDate = sessionDatePickerEndTime.getValue();
        LocalDateTime endTime = (endDate != null) ? endDate.atTime(LocalTime.of(23, 59)) : null;

        if (endTime == null) {
            errorMessages.append("End time is required!\n");
        }

        Boolean activity = sessionComboBoxActivity.getValue();
        if (activity == null) {
            errorMessages.append("Activity is required!\n");
        }

        TrafficRecord selectedTrafficRecord = sessionComboBoxTrafficRecord.getValue();
        if (selectedTrafficRecord == null) {
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
                    .setStartTime(startTime)
                    .setEndTime(endTime)
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

            ShowAlertUtil.showAlert("Uspješno dodavanje sesije", "Sesija je uspješno dodana!", Alert.AlertType.INFORMATION);
            StringBuilder sb = new StringBuilder();
            sb.append("Website: ").append(website.getWebsiteName()).append("\n")
                    .append("User: ").append(user.getUsername()).append("\n")
                    .append("Device type: ").append(deviceType).append("\n")
                    .append("Duration: ").append(duration).append("\n")
                    .append("Start time: ").append(startTime).append("\n")
                    .append("End time: ").append(endTime).append("\n")
                    .append("Activity: ").append(activity).append("\n");

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
