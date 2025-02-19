package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.*;
import hr.javafx.webtrackly.app.enums.DeviceType;
import hr.javafx.webtrackly.app.model.Session;
import hr.javafx.webtrackly.app.model.TrafficRecord;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class SessionEditController {
    @FXML
    private ComboBox<Website> sessionEditComboBoxWebsite;

    @FXML
    private ComboBox<User> sessionEditComboBoxUser;

    @FXML
    private ComboBox<DeviceType> sessionEditComboBoxDeviceType;

    @FXML
    private TextField sessionEditTextFieldDuration;

    @FXML
    private DatePicker sessionEditDatePickerStartTime;

    @FXML
    private DatePicker sessionEditDatePickerEndTime;

    @FXML
    private ComboBox<Boolean> sessionEditComboBoxActivity;

    @FXML
    private ComboBox<TrafficRecord> sessionEditComboBoxTrafficRecord;

    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
    private UserDbRepository1<User> userRepository = new UserDbRepository1<>();
    private SessionDbRepository2<Session> sessionRepository = new SessionDbRepository2<>();
    private TrafficRecordDbRepository1<TrafficRecord> trafficRecordRepository = new TrafficRecordDbRepository1<>();

    private Session session;

    public void setSessionData(Session session) {
        this.session = session;

        sessionEditComboBoxWebsite.setValue(session.getWebsite());
        sessionEditComboBoxUser.setValue(session.getUser());
        sessionEditComboBoxDeviceType.setValue(session.getDeviceType());
        sessionEditTextFieldDuration.setText(session.getSessionDuration().toString());
        sessionEditDatePickerStartTime.setValue(session.getStartTime().toLocalDate());
        sessionEditDatePickerEndTime.setValue(session.getEndTime().toLocalDate());
        sessionEditComboBoxActivity.setValue(session.getActive());
        sessionEditComboBoxTrafficRecord.setValue(trafficRecordRepository.findById(session.getTrafficRecordId()));
    }


    public void initialize() {
        sessionEditComboBoxWebsite.getItems().setAll(websiteRepository.findAll());
        sessionEditComboBoxUser.getItems().setAll(userRepository.findAll());
        sessionEditComboBoxDeviceType.getItems().setAll(DeviceType.values());
        sessionEditComboBoxActivity.getItems().setAll(TRUE, FALSE);
        sessionEditComboBoxTrafficRecord.getItems().setAll(trafficRecordRepository.findAll());
    }

    public void editSession(){
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultEdit();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            StringBuilder errorMessages = new StringBuilder();
            Website website = sessionEditComboBoxWebsite.getValue();
            if (website == null) {
                errorMessages.append("Website is required!\n");
            }

            User user = sessionEditComboBoxUser.getValue();
            if (user == null) {
                errorMessages.append("User is required!\n");
            }

            DeviceType deviceType = sessionEditComboBoxDeviceType.getValue();
            if (deviceType == null) {
                errorMessages.append("Device type is required!\n");
            }

            BigDecimal duration = BigDecimal.ZERO;
            try {
                duration = new BigDecimal(sessionEditTextFieldDuration.getText());
                if (duration.compareTo(BigDecimal.ZERO) <= 0) {
                    errorMessages.append("Duration must be greater than zero!\n");
                }
            } catch (NumberFormatException e) {
                errorMessages.append("Invalid duration format!\n");
            }

            LocalDate startDate = sessionEditDatePickerStartTime.getValue();
            LocalDateTime startTime = (startDate != null) ? startDate.atTime(LocalTime.of(0, 0)) : null;

            if (startTime == null) {
                errorMessages.append("Start time is required!\n");
            }

            LocalDate endDate = sessionEditDatePickerEndTime.getValue();
            LocalDateTime endTime = (endDate != null) ? endDate.atTime(LocalTime.of(23, 59)) : null;

            if (endTime == null) {
                errorMessages.append("End time is required!\n");
            }

            Boolean activity = sessionEditComboBoxActivity.getValue();
            if (activity == null) {
                errorMessages.append("Activity is required!\n");
            }

            TrafficRecord selectedTrafficRecord = sessionEditComboBoxTrafficRecord.getValue();
            if (selectedTrafficRecord == null) {
                errorMessages.append("Traffic record is required!\n");
            }

            if (errorMessages.length() > 0) {
                ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
            } else {
                Session updatedSession = new Session.Builder()
                        .setId(session.getId())
                        .setWebsite(website)
                        .setUser(user)
                        .setDeviceType(deviceType)
                        .setSessionDuration(duration)
                        .setStartTime(startTime)
                        .setEndTime(endTime)
                        .setActive(activity)
                        .setTrafficRecordId(selectedTrafficRecord.getId())
                        .build();

                sessionRepository.update(updatedSession);

                ShowAlertUtil.showAlert("Success", "Session updated successfully!", Alert.AlertType.INFORMATION);

                sessionEditComboBoxWebsite.getSelectionModel().clearSelection();
                sessionEditComboBoxUser.getSelectionModel().clearSelection();
                sessionEditComboBoxDeviceType.getSelectionModel().clearSelection();
                sessionEditTextFieldDuration.clear();
                sessionEditDatePickerStartTime.getEditor().clear();
                sessionEditDatePickerEndTime.getEditor().clear();
                sessionEditComboBoxActivity.getSelectionModel().clearSelection();
                sessionEditComboBoxTrafficRecord.getSelectionModel().clearSelection();
            }

        } else {
            ShowAlertUtil.showAlert("Error", "Session not updated!", Alert.AlertType.ERROR);
        }
    }
}
