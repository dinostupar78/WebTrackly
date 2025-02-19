package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.UserActionDbRepository2;
import hr.javafx.webtrackly.app.db.UserDbRepository1;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.enums.BehaviorType;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.UserAction;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class UserActionEditController {
    @FXML
    private ComboBox<User> actionEditComboBoxUser;

    @FXML
    private ComboBox<BehaviorType> actionEditComboBoxAction;

    @FXML
    private ComboBox<Website> actionEditComboBoxWebsite;

    @FXML
    private DatePicker actionEditDatePickerTimestamp;

    @FXML
    private TextField actionEditTextFieldDetails;

    private UserAction userAction;

    private UserDbRepository1<User> userRepository = new UserDbRepository1<>();
    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
    private UserActionDbRepository2<UserAction> userActionRepository = new UserActionDbRepository2<>();

    public void setUserActionData(UserAction userAction) {
        this.userAction = userAction;

        actionEditComboBoxUser.setValue(userAction.getUser());
        actionEditComboBoxAction.setValue(userAction.getAction());
        actionEditComboBoxWebsite.setValue(userAction.getPage());
        actionEditDatePickerTimestamp.setValue(userAction.getTimestamp().toLocalDate());
        actionEditTextFieldDetails.setText(userAction.getDetails());
    }

    public void initialize() {
        actionEditComboBoxUser.getItems().setAll(userRepository.findAll());
        actionEditComboBoxAction.getItems().setAll(BehaviorType.values());
        actionEditComboBoxWebsite.getItems().setAll(websiteRepository.findAll());
    }

    public void editUserAction(){
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultEdit();
        if (result.isPresent() && result.get() == ButtonType.OK){
            StringBuilder errorMessages = new StringBuilder();

            User user = actionEditComboBoxUser.getValue();
            if (user == null) {
                errorMessages.append("User is required!\n");
            }

            BehaviorType action = actionEditComboBoxAction.getValue();
            if (action == null) {
                errorMessages.append("Action is required!\n");
            }

            Website website = actionEditComboBoxWebsite.getValue();
            if (website == null) {
                errorMessages.append("Website is required!\n");
            }

            if (actionEditDatePickerTimestamp.getValue() == null) {
                errorMessages.append("Timestamp is required!\n");
            }

            String details = actionEditTextFieldDetails.getText();
            if (details.isEmpty()) {
                errorMessages.append("Details are required!\n");
            }

            if (errorMessages.length() > 0) {
                ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
            } else {
                UserAction newUserAction = new UserAction.Builder()
                        .setId(userAction.getId())
                        .setUser(user)
                        .setAction(action)
                        .setPage(website)
                        .setActionTimestamp(actionEditDatePickerTimestamp.getValue().atStartOfDay())
                        .setDetails(details)
                        .build();

                userActionRepository.update(newUserAction);

                ShowAlertUtil.showAlert("Success", "Traffic record updated successfully!", Alert.AlertType.INFORMATION);

                actionEditComboBoxUser.getSelectionModel().clearSelection();
                actionEditComboBoxAction.getSelectionModel().clearSelection();
                actionEditComboBoxWebsite.getSelectionModel().clearSelection();
                actionEditDatePickerTimestamp.getEditor().clear();
                actionEditTextFieldDetails.clear();
            }

        } else {
            ShowAlertUtil.showAlert("Error", "User action not updated!", Alert.AlertType.ERROR);
        }

    }
}
