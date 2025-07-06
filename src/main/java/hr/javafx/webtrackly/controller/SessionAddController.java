package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.SessionDbRepository1;
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

public class SessionAddController {
    @FXML
    private ComboBox<Website> sessionComboBoxWebsite;

    @FXML
    private ComboBox<User> sessionComboBoxUser;

    @FXML
    private ComboBox<DeviceType> sessionComboBoxDeviceType;

    @FXML
    private DatePicker sessionDatePickerStartDate;

    @FXML
    private TextField sessionTextFieldStartTime;

    @FXML
    private DatePicker sessionDatePickerEndDate;

    @FXML
    private TextField sessionTextFieldEndTime;

    @FXML
    private ComboBox<Boolean> sessionComboBoxActivity;

    private ObjectBinding<LocalDateTime> startDateTimeBinding;

    private ObjectBinding<LocalDateTime> endDateTimeBinding;

    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
    private UserDbRepository1<User> userRepository = new UserDbRepository1<>();
    private SessionDbRepository1<Session> sessionRepository = new SessionDbRepository1<>();

    public void initialize() {
        sessionComboBoxWebsite.getItems().setAll(websiteRepository.findAll());
        sessionComboBoxUser.getItems().setAll(userRepository.findAll());
        sessionComboBoxDeviceType.getItems().setAll(DeviceType.values());
        sessionComboBoxActivity.getItems().setAll(TRUE, FALSE);

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        LocalTimeStringConverter converter = new LocalTimeStringConverter(timeFmt, timeFmt);

        TextFormatter<LocalTime> startFormatter = new TextFormatter<>(converter, null);
        sessionTextFieldStartTime.setTextFormatter(startFormatter);

        startDateTimeBinding = Bindings.createObjectBinding(() -> {
            LocalDate date = sessionDatePickerStartDate.getValue();
            LocalTime time = startFormatter.getValue();
            return (date != null && time != null) ? LocalDateTime.of(date, time) : null;
        }, sessionDatePickerStartDate.valueProperty(), startFormatter.valueProperty());

        TextFormatter<LocalTime> endFormatter = new TextFormatter<>(converter, null);
        sessionTextFieldEndTime.setTextFormatter(endFormatter);

        endDateTimeBinding = Bindings.createObjectBinding(() -> {
            LocalDate date = sessionDatePickerEndDate.getValue();
            LocalTime time = endFormatter.getValue();
            return (date != null && time != null) ? LocalDateTime.of(date, time) : null;
        }, sessionDatePickerEndDate.valueProperty(), endFormatter.valueProperty());

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
            errorMessages.append("Start date is required!\n");
        }

        Boolean activity = sessionComboBoxActivity.getValue();
        Optional<Boolean> optActivity = Optional.ofNullable(activity);
        if (optActivity.isPresent()) {
            activity = optActivity.get();
        } else {
            errorMessages.append("Activity is required!\n");
        }

        if (errorMessages.length() > 0) {
            ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
        } else {
            Session newSession = new Session.Builder()
                    .setWebsite(website)
                    .setUser(user)
                    .setDeviceType(deviceType)
                    .setStartTime(startDateTime)
                    .setEndTime(endDateTime)
                    .setActive(activity)
                    .build();

            sessionRepository.save(newSession);

            String roleString = Optional.ofNullable(UserSession.getInstance().getCurrentUser())
                    .map(User::getRole)
                    .map(Role::toString)
                    .orElse("UNKNOWN");

            DataSerialization change = new DataSerialization(
                    "Session Added",
                    "N/A",
                    newSession.toString(),
                    roleString,
                    LocalDateTime.now()
            );

            DataSerializeUtil.serializeData(change);

            ShowAlertUtil.showAlert("Success", "Session is successfully added!", Alert.AlertType.INFORMATION);

            sessionComboBoxWebsite.getSelectionModel().clearSelection();
            sessionComboBoxUser.getSelectionModel().clearSelection();
            sessionComboBoxDeviceType.getSelectionModel().clearSelection();
            sessionDatePickerStartDate.getEditor().clear();
            sessionTextFieldStartTime.clear();
            sessionDatePickerEndDate.getEditor().clear();
            sessionTextFieldEndTime.clear();
            sessionComboBoxActivity.getSelectionModel().clearSelection();

        }
    }

}
