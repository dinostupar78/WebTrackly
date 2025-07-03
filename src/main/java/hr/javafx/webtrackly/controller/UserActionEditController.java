package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.SessionDbRepository1;
import hr.javafx.webtrackly.app.db.UserActionDbRepository2;
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

public class UserActionEditController {
    @FXML
    private ComboBox<User> actionEditComboBoxUser;

    @FXML
    private ComboBox<BehaviourType> actionEditComboBoxAction;

    @FXML
    private ComboBox<Website> actionEditComboBoxWebsite;

    @FXML
    private ComboBox<Session> actionEditComboBoxSession;

    @FXML
    private DatePicker actionEditDatePickerDateOfAction;

    @FXML
    private TextField actionEditTextFieldTimeOfAction;

    private ObjectBinding<LocalDateTime> dateTimeBinding;

    @FXML
    private TextField actionEditTextFieldDetails;

    private UserAction userAction;

    private UserDbRepository1<User> userRepository = new UserDbRepository1<>();
    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
    private UserActionDbRepository2<UserAction> userActionRepository = new UserActionDbRepository2<>();
    private SessionDbRepository1<Session> sessionRepository = new SessionDbRepository1<>();

    public void setUserActionData(UserAction userAction) {
        this.userAction = userAction;

        actionEditComboBoxUser.setValue(userAction.getUser());
        actionEditComboBoxAction.setValue(userAction.getAction());
        actionEditComboBoxWebsite.setValue(userAction.getPage());
        actionEditComboBoxSession.setValue(userAction.getSession());
        actionEditDatePickerDateOfAction.setValue(userAction.getTimestamp().toLocalDate());
        actionEditTextFieldTimeOfAction.setText(userAction.getTimestamp().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        actionEditTextFieldDetails.setText(userAction.getDetails());

    }

    public void initialize() {
        actionEditComboBoxUser.getItems().setAll(userRepository.findAll());
        actionEditComboBoxAction.getItems().setAll(BehaviourType.values());
        actionEditComboBoxWebsite.getItems().setAll(websiteRepository.findAll());
        actionEditComboBoxSession.getItems().setAll(sessionRepository.findAll());
        actionEditDatePickerDateOfAction.setValue(LocalDate.now());


        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        LocalTimeStringConverter converter = new LocalTimeStringConverter(timeFmt, timeFmt);
        TextFormatter<LocalTime> tf = new TextFormatter<>(converter, null);
        actionEditTextFieldTimeOfAction.setTextFormatter(tf);

        dateTimeBinding = Bindings.createObjectBinding(() -> {
            LocalDate ld = actionEditDatePickerDateOfAction.getValue();
            LocalTime lt = tf.getValue();
            return ld == null || lt == null ? null : LocalDateTime.of(ld, lt);
        }, actionEditDatePickerDateOfAction.valueProperty(), tf.valueProperty());
    }

    public void editUserAction(){
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultEdit();
        if (result.isPresent() && result.get() == ButtonType.OK){
            StringBuilder errorMessages = new StringBuilder();

            User user = actionEditComboBoxUser.getValue();
            Optional<User> optUser = Optional.ofNullable(user);
            if (optUser.isPresent()) {
                user = optUser.get();
            } else {
                errorMessages.append("User is required!\n");
            }


            BehaviourType action = actionEditComboBoxAction.getValue();
            Optional<BehaviourType> optAction = Optional.ofNullable(action);
            if (optAction.isPresent()) {
                action = optAction.get();
            } else {
                errorMessages.append("Action is required!\n");
            }

            Website website = actionEditComboBoxWebsite.getValue();
            Optional<Website> optWebsite = Optional.ofNullable(website);
            if (optWebsite.isPresent()) {
                website = optWebsite.get();
            } else {
                errorMessages.append("Website is required!\n");
            }

            Session session = actionEditComboBoxSession.getValue();
            Optional<Session> optSession = Optional.ofNullable(session);
            if (optSession.isPresent()) {
                session = optSession.get();
            } else {
                errorMessages.append("Session is required!\n");
            }

            LocalDateTime actionTimestamp = dateTimeBinding.get();
            Optional<LocalDateTime> optTimestamp = Optional.ofNullable(actionTimestamp);
            if (optTimestamp.isPresent()) {
                actionTimestamp = optTimestamp.get();
                formatLocalDateTime(actionTimestamp);
            } else {
                errorMessages.append("Timestamp is required!\n");
            }

            String details = actionEditTextFieldDetails.getText();
            if (details.isEmpty()) {
                errorMessages.append("Details are required!\n");
            }

            if (errorMessages.length() > 0) {
                ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
            } else {
                String oldUserAction = userAction.toString();

                UserAction newUserAction = new UserAction.Builder()
                        .setId(userAction.getId())
                        .setUser(user)
                        .setAction(action)
                        .setPage(website)
                        .setSession(session)
                        .setActionTimestamp(actionTimestamp)
                        .setDetails(details)
                        .build();

                userActionRepository.update(newUserAction);

                DataSerialization change = new DataSerialization(
                        "UserAction Edited",
                        oldUserAction,
                        newUserAction.toString(),
                        Optional.ofNullable(userAction.getUser().getUsername()).orElse("Unknown User"),
                        LocalDateTime.now()
                );

                DataSerializeUtil.serializeData(change);
                ShowAlertUtil.showAlert("Success", "User Action updated successfully!", Alert.AlertType.INFORMATION);
                clearForm();
            }

        } else {
            ShowAlertUtil.showAlert("Error", "User action not updated!", Alert.AlertType.ERROR);
        }
    }

    private void clearForm(){
        actionEditComboBoxUser.setValue(null);
        actionEditComboBoxAction.getSelectionModel().clearSelection();
        actionEditComboBoxWebsite.setValue(null);
        actionEditComboBoxSession.getSelectionModel().clearSelection();
        actionEditDatePickerDateOfAction.getEditor().clear();
        actionEditTextFieldTimeOfAction.clear();
        actionEditTextFieldDetails.clear();
    }
}
