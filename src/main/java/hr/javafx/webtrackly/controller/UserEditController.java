package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.UserDbRepository;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository;
import hr.javafx.webtrackly.app.enums.GenderType;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserEditController {
    @FXML
    private TextField userEditTextFieldFirstName;

    @FXML
    private TextField userEditTextFieldLastName;

    @FXML
    private DatePicker userEditDatePickerBirth;

    @FXML
    private TextField userEditTextFieldNationality;

    @FXML
    private ComboBox<GenderType> userEditComboBoxGender;

    @FXML
    private TextField userEditTextFieldUsername;

    @FXML
    private TextField userEditTextFieldPassword;

    @FXML
    private ComboBox<Website> userEditComboBoxWebsite;

    private User user;

    private UserDbRepository<User> userRepository = new UserDbRepository<>();
    private WebsiteDbRepository<Website> websiteRepository = new WebsiteDbRepository<>();

    public void setUser(User user) {
        this.user = user;

        userEditTextFieldFirstName.setText(user.getFirstName());
        userEditTextFieldLastName.setText(user.getLastName());
        userEditDatePickerBirth.setValue(user.getPersonalData().dateOfBirth());
        userEditTextFieldNationality.setText(user.getPersonalData().nationality());
        userEditComboBoxGender.setValue(user.getPersonalData().gender());
        userEditTextFieldUsername.setText(user.getUsername());
        userEditTextFieldPassword.setText(user.getHashedPassword());
        userEditComboBoxWebsite.setValue(websiteRepository.findById(user.getWebsiteId()));
    }

    public void initialize() {
        userEditComboBoxGender.getItems().setAll(GenderType.values());
        userEditComboBoxWebsite.getItems().setAll(websiteRepository.findAll());
    }

    public void editUser(){
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultEdit();

        if (result.isPresent() && result.get() == ButtonType.OK){
            StringBuilder errorMessages = new StringBuilder();

            String firstName = userEditTextFieldFirstName.getText();
            if(firstName.isEmpty()){
                errorMessages.append("First name is required!\n");
            }

            String lastName = userEditTextFieldLastName.getText();
            if(lastName.isEmpty()){
                errorMessages.append("Last name is required!\n");
            }

            LocalDate dateOfBirth = userEditDatePickerBirth.getValue();
            if(dateOfBirth == null){
                errorMessages.append("Date of birth is required!\n");
            }

            String nationality = userEditTextFieldNationality.getText();
            if(nationality.isEmpty()){
                errorMessages.append("Nationality is required!\n");
            }

            GenderType gender = userEditComboBoxGender.getValue();
            if(gender == null){
                errorMessages.append("Gender is required!\n");
            }

            String username = userEditTextFieldUsername.getText();
            if(username.isEmpty()){
                errorMessages.append("Username is required!\n");
            }

            String password = userEditTextFieldPassword.getText();
            if(password.isEmpty()){
                errorMessages.append("Password is required!\n");
            }

            Role role = new UserRole();

            Website selectedWebsite = userEditComboBoxWebsite.getValue();
            Long websiteId = (selectedWebsite != null) ? selectedWebsite.getId() : null;

            if (websiteId == null) {
                errorMessages.append("Website is required!\n");
            }
            if(errorMessages.length() > 0){
                ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
            } else {
                User newUser = new User.Builder()
                        .setId(user.getId())
                        .setName(firstName)
                        .setSurname(lastName)
                        .setPersonalData(new PersonalData(dateOfBirth, nationality, gender))
                        .setUsername(username)
                        .setHashedPassword(password)
                        .setRole(role)
                        .setWebsiteId(websiteId)
                        .setRegistrationDate(LocalDateTime.now())
                        .build();

                userRepository.update(newUser);

                DataSerialization change = new DataSerialization(
                        "User Edited",
                        user.toString(),
                        newUser.toString(),
                        user.getUsername(),
                        LocalDateTime.now()
                );

                DataSerializeUtil.serializeData(change);

                ShowAlertUtil.showAlert("Success", "User updated successfully!", Alert.AlertType.INFORMATION);

                userEditTextFieldFirstName.clear();
                userEditTextFieldLastName.clear();
                userEditDatePickerBirth.getEditor().clear();
                userEditTextFieldNationality.clear();
                userEditComboBoxGender.getSelectionModel().clearSelection();
                userEditTextFieldUsername.clear();
                userEditTextFieldPassword.clear();
                userEditComboBoxWebsite.getSelectionModel().clearSelection();

            }
        } else {
            ShowAlertUtil.showAlert("Error", "Session not updated!", Alert.AlertType.ERROR);
        }
    }

}
