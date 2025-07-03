package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.SessionDbRepository2;
import hr.javafx.webtrackly.app.db.TrafficRecordDbRepository1;
import hr.javafx.webtrackly.app.db.UserDbRepository1;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.enums.DeviceType;
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

import static hr.javafx.webtrackly.utils.DateFormatterUtil.formatLocalDateTime;
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
    private DatePicker sessionEditDatePickerStartDate;

    @FXML
    private TextField sessionEditTextFieldStartTime;

    @FXML
    private DatePicker sessionEditDatePickerEndDate;

    @FXML
    private TextField sessionEditTextFieldEndTime;

    @FXML
    private ComboBox<Boolean> sessionEditComboBoxActivity;

    @FXML
    private ComboBox<TrafficRecord> sessionEditComboBoxTrafficRecord;

    private ObjectBinding<LocalDateTime> startDateTimeBinding;

    private ObjectBinding<LocalDateTime> endDateTimeBinding;

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
        sessionEditDatePickerStartDate.setValue(session.getStartTime().toLocalDate());
        sessionEditTextFieldStartTime.setText(session.getStartTime().toLocalTime().toString());
        sessionEditDatePickerEndDate.setValue(session.getEndTime().toLocalDate());
        sessionEditTextFieldEndTime.setText(session.getEndTime().toLocalTime().toString());
        sessionEditComboBoxActivity.setValue(session.getActive());
        sessionEditComboBoxTrafficRecord.setValue(trafficRecordRepository.findById(session.getTrafficRecordId()));
    }


    public void initialize() {
        sessionEditComboBoxWebsite.getItems().setAll(websiteRepository.findAll());
        sessionEditComboBoxUser.getItems().setAll(userRepository.findAll());
        sessionEditComboBoxDeviceType.getItems().setAll(DeviceType.values());
        sessionEditComboBoxActivity.getItems().setAll(TRUE, FALSE);
        sessionEditComboBoxTrafficRecord.getItems().setAll(trafficRecordRepository.findAll());

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        LocalTimeStringConverter converter = new LocalTimeStringConverter(timeFmt, timeFmt);

        TextFormatter<LocalTime> startFormatter = new TextFormatter<>(converter, null);
        sessionEditTextFieldStartTime.setTextFormatter(startFormatter);

        startDateTimeBinding = Bindings.createObjectBinding(() -> {
            LocalDate date = sessionEditDatePickerStartDate.getValue();
            LocalTime time = startFormatter.getValue();
            return (date != null && time != null) ? LocalDateTime.of(date, time) : null;
        }, sessionEditDatePickerStartDate.valueProperty(), startFormatter.valueProperty());

        TextFormatter<LocalTime> endFormatter = new TextFormatter<>(converter, null);
        sessionEditTextFieldEndTime.setTextFormatter(endFormatter);

        endDateTimeBinding = Bindings.createObjectBinding(() -> {
            LocalDate date = sessionEditDatePickerEndDate.getValue();
            LocalTime time = endFormatter.getValue();
            return (date != null && time != null) ? LocalDateTime.of(date, time) : null;
        }, sessionEditDatePickerEndDate.valueProperty(), endFormatter.valueProperty());
    }

    public void editSession(){
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultEdit();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            StringBuilder errorMessages = new StringBuilder();

            Website website = sessionEditComboBoxWebsite.getValue();
            Optional<Website> optWebsite = Optional.ofNullable(website);
            if (optWebsite.isPresent()) {
                website = optWebsite.get();
            } else {
                errorMessages.append("Website is required!\n");
            }

            User user = sessionEditComboBoxUser.getValue();
            Optional<User> optUser = Optional.ofNullable(user);
            if (optUser.isPresent()) {
                user = optUser.get();
            } else {
                errorMessages.append("User is required!\n");
            }

            DeviceType deviceType = sessionEditComboBoxDeviceType.getValue();
            Optional<DeviceType> optDeviceType = Optional.ofNullable(deviceType);
            if (optDeviceType.isPresent()) {
                deviceType = optDeviceType.get();
            } else {
                errorMessages.append("Device type is required!\n");
            }

            LocalDateTime startDateTime = startDateTimeBinding.get();
            Optional<LocalDateTime> optStartDate = Optional.ofNullable(startDateTime);
            if(optStartDate.isPresent()){
                startDateTime = optStartDate.get();
                formatLocalDateTime(startDateTime);
            } else {
                errorMessages.append("Start date is required!\n");
            }

            LocalDateTime endDateTime = endDateTimeBinding.get();
            Optional<LocalDateTime> optEndDate = Optional.ofNullable(endDateTime);
            if(optEndDate.isPresent()){
                endDateTime = optEndDate.get();
                formatLocalDateTime(endDateTime);
            } else {
                errorMessages.append("End date is required!\n");
            }

            Boolean activity = sessionEditComboBoxActivity.getValue();
            Optional<Boolean> optActivity = Optional.ofNullable(activity);
            if (optActivity.isPresent()) {
                activity = optActivity.get();
            } else {
                errorMessages.append("Activity is required!\n");
            }

            TrafficRecord selectedTrafficRecord = sessionEditComboBoxTrafficRecord.getValue();
            Optional<TrafficRecord> optTrafficRecord = Optional.ofNullable(selectedTrafficRecord);
            if (optTrafficRecord.isPresent()) {
                selectedTrafficRecord = optTrafficRecord.get();
            } else {
                errorMessages.append("Traffic record is required!\n");
            }
            Long trafficRecordId = Optional.ofNullable(selectedTrafficRecord)
                    .map(TrafficRecord::getId)
                    .orElse(null);

            if (errorMessages.length() > 0) {
                ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
            } else {
                String oldSessionData = session.toString();

                Session updatedSession = new Session.Builder()
                        .setId(session.getId())
                        .setWebsite(website)
                        .setUser(user)
                        .setDeviceType(deviceType)
                        .setStartTime(startDateTime)
                        .setEndTime(endDateTime)
                        .setActive(activity)
                        .setTrafficRecordId(trafficRecordId)
                        .build();

                sessionRepository.update(updatedSession);

                DataSerialization change = new DataSerialization(
                        "Session Edited",
                        oldSessionData,
                        updatedSession.toString(),
                        Optional.ofNullable(updatedSession.getWebsite().getWebsiteName()).orElse("Unknown Website"),
                        LocalDateTime.now()
                );

                DataSerializeUtil.serializeData(change);
                ShowAlertUtil.showAlert("Success", "Session updated successfully!", Alert.AlertType.INFORMATION);
                clearForm();
            }

        } else {
            ShowAlertUtil.showAlert("Error", "Session not updated!", Alert.AlertType.ERROR);
        }
    }

    private void clearForm() {
        sessionEditComboBoxWebsite.getSelectionModel().clearSelection();
        sessionEditComboBoxUser.getSelectionModel().clearSelection();
        sessionEditComboBoxDeviceType.getSelectionModel().clearSelection();
        sessionEditDatePickerStartDate.getEditor().clear();
        sessionEditTextFieldStartTime.clear();
        sessionEditDatePickerEndDate.getEditor().clear();
        sessionEditTextFieldEndTime.clear();
        sessionEditComboBoxActivity.getSelectionModel().clearSelection();
        sessionEditComboBoxTrafficRecord.getSelectionModel().clearSelection();
    }
}
