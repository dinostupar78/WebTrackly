package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.SessionDbRepository1;
import hr.javafx.webtrackly.app.db.UserActionDbRepository1;
import hr.javafx.webtrackly.app.db.UserDbRepository1;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.enums.BehaviourType;
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

public class UserActionAddController {
    @FXML
    private ComboBox<User> actionComboBoxUser;

    @FXML
    private ComboBox<BehaviourType> actionComboBoxAction;

    @FXML
    private ComboBox<Website> actionComboBoxWebsite;

    @FXML
    private ComboBox<Session> actionComboBoxSession;

    @FXML
    private DatePicker actionDatePickerDateOfAction;

    @FXML
    private TextField actionTextFieldTimeOfAction;

    private ObjectBinding<LocalDateTime> dateTimeBinding;

    @FXML
    private TextField actionTextFieldDetails;

    private UserDbRepository1<User> userRepository = new UserDbRepository1<>();
    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
    private UserActionDbRepository1<UserAction> userActionRepository = new UserActionDbRepository1<>();
    private SessionDbRepository1<Session> sessionRepository = new SessionDbRepository1<>();

    public void initialize() {
        actionComboBoxUser.getItems().setAll(userRepository.findAll());
        actionComboBoxAction.getItems().setAll(BehaviourType.values());
        actionComboBoxWebsite.getItems().setAll(websiteRepository.findAll());
        actionComboBoxSession.getItems().setAll(sessionRepository.findAll());

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        LocalTimeStringConverter converter = new LocalTimeStringConverter(timeFmt, timeFmt);
        TextFormatter<LocalTime> tf = new TextFormatter<>(converter, null);
        actionTextFieldTimeOfAction.setTextFormatter(tf);

        dateTimeBinding = Bindings.createObjectBinding(() -> {
            LocalDate ld = actionDatePickerDateOfAction.getValue();
            LocalTime lt = tf.getValue();
            return ld == null || lt == null ? null : LocalDateTime.of(ld, lt);
        }, actionDatePickerDateOfAction.valueProperty(), tf.valueProperty());
    }

    public void addAction() {
        StringBuilder errorMessages = new StringBuilder();

        User user = actionComboBoxUser.getValue();
        Optional<User> optUser = Optional.ofNullable(user);
        if (optUser.isPresent()) {
            user = optUser.get();
        } else {
            errorMessages.append("User is required!\n");
        }

        Session session = actionComboBoxSession.getValue();
        Optional<Session> optSession = Optional.ofNullable(session);
        if (optSession.isPresent()) {
            session = optSession.get();
        } else {
            errorMessages.append("Session is required!\n");
        }

        BehaviourType action = actionComboBoxAction.getValue();
        Optional<BehaviourType> optAction = Optional.ofNullable(action);
        if (optAction.isPresent()) {
            action = optAction.get();
        } else {
            errorMessages.append("Action is required!\n");
        }

        Website website = actionComboBoxWebsite.getValue();
        Optional<Website> optWebsite = Optional.ofNullable(website);
        if (optWebsite.isPresent()) {
            website = optWebsite.get();
        } else {
            errorMessages.append("Website is required!\n");
        }

        LocalDateTime actionTimestamp = dateTimeBinding.get();
        Optional<LocalDateTime> optTimestamp = Optional.ofNullable(actionTimestamp);
        if (optTimestamp.isPresent()) {
            actionTimestamp = optTimestamp.get();
            formatLocalDateTime(actionTimestamp);
        } else {
            errorMessages.append("Timestamp is required!\n");
        }

        String details = actionTextFieldDetails.getText();
        if (details.isEmpty()) {
            errorMessages.append("Details are required!\n");
        }

        if (errorMessages.length() > 0) {
            ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
        } else {
            UserAction newUserAction = new UserAction.Builder()
                    .setUser(user)
                    .setAction(action)
                    .setPage(website)
                    .setSession(session)
                    .setActionTimestamp(actionTimestamp)
                    .setDetails(details)
                    .build();

            userActionRepository.save(newUserAction);

            DataSerialization change = new DataSerialization(
                    "UserAction",
                    "Add",
                    newUserAction.toString(),
                    newUserAction.getUser().getRole().toString(),
                    newUserAction.getTimestamp());

            DataSerializeUtil.serializeData(change);

            ShowAlertUtil.showAlert("Success", "User Action successfully added!", Alert.AlertType.INFORMATION);
            StringBuilder sb = new StringBuilder();
            sb.append("User: ")
                    .append(user)
                    .append("\nAction: ")
                    .append(action)
                    .append("\nWebsite: ")
                    .append(website)
                    .append("\nTimestamp: ")
                    .append(formatLocalDateTime(actionTimestamp))
                    .append("\nDetails: ")
                    .append(details);

            actionComboBoxUser.getSelectionModel().clearSelection();
            actionComboBoxAction.getSelectionModel().clearSelection();
            actionComboBoxWebsite.getSelectionModel().clearSelection();
            actionDatePickerDateOfAction.getEditor().clear();
            actionTextFieldTimeOfAction.clear();
            actionTextFieldDetails.clear();

        }
    }
}
