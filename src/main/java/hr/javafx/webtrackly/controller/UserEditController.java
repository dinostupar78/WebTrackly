package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.UserDbRepository2;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
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
    private TextField userEditTextFieldEmail;

    @FXML
    private TextField userEditTextFieldPassword;

    @FXML
    private ComboBox<Website> userEditComboBoxWebsite;

    private User user;

    private UserDbRepository2<User> userRepository = new UserDbRepository2<>();
    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();

    public void setUser(User user) {
        this.user = user;

        userEditTextFieldFirstName.setText(user.getFirstName());
        userEditTextFieldLastName.setText(user.getLastName());
        userEditDatePickerBirth.setValue(user.getPersonalData().dateOfBirth());
        userEditTextFieldNationality.setText(user.getPersonalData().nationality());
        userEditComboBoxGender.setValue(user.getPersonalData().gender());
        userEditTextFieldUsername.setText(user.getUsername());
        userEditTextFieldEmail.setText(user.getEmail());
        userEditTextFieldPassword.setText(user.getPassword());
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
            Optional<LocalDate> optDateOfBirth = Optional.ofNullable(dateOfBirth);
            if (optDateOfBirth.isPresent()) {
                dateOfBirth = optDateOfBirth.get();
            } else {
                errorMessages.append("Date of birth is required!\n");
            }

            String nationality = userEditTextFieldNationality.getText();
            if(nationality.isEmpty()){
                errorMessages.append("Nationality is required!\n");
            }

            GenderType gender = userEditComboBoxGender.getValue();
            Optional<GenderType> optGender = Optional.ofNullable(gender);
            if (optGender.isPresent()) {
                gender = optGender.get();
            } else {
                errorMessages.append("Gender is required!\n");
            }

            String username = userEditTextFieldUsername.getText();
            if(username.isEmpty()){
                errorMessages.append("Username is required!\n");
            }

            String email = userEditTextFieldEmail.getText();
            if(email.isEmpty()){
                errorMessages.append("Email is required!\n");
            }

            String password = userEditTextFieldPassword.getText();
            if(password.isEmpty()){
                errorMessages.append("Password is required!\n");
            }

            Role role = new UserRole();

            Website selectedWebsite = userEditComboBoxWebsite.getValue();
            Optional<Website> optWebsite = Optional.ofNullable(selectedWebsite);
            if (optWebsite.isPresent()) {
                selectedWebsite = optWebsite.get();
            } else {
                errorMessages.append("Website is required!\n");
            }

            Long websiteId = Optional.ofNullable(selectedWebsite)
                    .map(Website::getId)
                    .orElse(null);

            if(errorMessages.length() > 0){
                ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
            } else {
                String oldUserData = user.toString();

                User newUser = new User.Builder()
                        .setId(user.getId())
                        .setName(firstName)
                        .setSurname(lastName)
                        .setPersonalData(new PersonalData(dateOfBirth, nationality, gender))
                        .setUsername(username)
                        .setEmail(email)
                        .setPassword(password)
                        .setRole(role)
                        .setWebsiteId(websiteId)
                        .build();

                userRepository.update(newUser);

                DataSerialization change = new DataSerialization(
                        "User Edited",
                        oldUserData,
                        newUser.toString(),
                        user.getUsername(),
                        LocalDateTime.now()
                );

                DataSerializeUtil.serializeData(change);
                ShowAlertUtil.showAlert("Success", "User updated successfully!", Alert.AlertType.INFORMATION);
                clearForm();
            }
        } else {
            ShowAlertUtil.showAlert("Error", "Session not updated!", Alert.AlertType.ERROR);
        }
    }

    private void clearForm(){
        userEditTextFieldFirstName.clear();
        userEditTextFieldLastName.clear();
        userEditDatePickerBirth.getEditor().clear();
        userEditTextFieldNationality.clear();
        userEditComboBoxGender.getSelectionModel().clearSelection();
        userEditTextFieldUsername.clear();
        userEditTextFieldEmail.clear();
        userEditTextFieldPassword.clear();
        userEditComboBoxWebsite.getSelectionModel().clearSelection();
    }

}
