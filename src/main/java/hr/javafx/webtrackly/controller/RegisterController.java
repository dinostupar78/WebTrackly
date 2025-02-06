package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.enums.GenderType;
import hr.javafx.webtrackly.app.files.UserFileRepo;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.OnClickUtil;
import hr.javafx.webtrackly.utils.PasswordUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class RegisterController {
    @FXML
    private TextField registerTextFieldFirstName;

    @FXML
    private TextField registerTextFieldLastName;

    @FXML
    private DatePicker registerDatePickerBirth;

    @FXML
    private TextField registerTextFieldNationality;

    @FXML
    private ComboBox<GenderType> registerComboBoxGender;

    @FXML
    private TextField registerTextFieldUsername;

    @FXML
    private TextField registerTextFieldPassword;

    @FXML
    private ComboBox<Role> registerComboBoxRole;

    public void initialize() {
        registerComboBoxGender.getItems().setAll(GenderType.values());
        registerComboBoxRole.getItems().addAll(new AdminRole(), new MarketingRole());
    }

    public void onClickRegister(){
        String firstName = registerTextFieldFirstName.getText();
        String lastName = registerTextFieldLastName.getText();
        LocalDate dateOfBirth = registerDatePickerBirth.getValue();
        String nationality = registerTextFieldNationality.getText();
        GenderType gender = registerComboBoxGender.getValue();
        String username = registerTextFieldUsername.getText();
        String password = registerTextFieldPassword.getText();
        Role role = registerComboBoxRole.getValue();

        String hashedPassword = PasswordUtil.hashPassword(password);

        User newUser = new User.Builder()
                .setName(firstName)
                .setSurname(lastName)
                .setPersonalData(new PersonalData(dateOfBirth, nationality, gender))
                .setUsername(username)
                .setHashedPassword(hashedPassword)
                .setRole(role)
                .build();

        UserFileRepo<User> userRepo = new UserFileRepo<>();
        try{
            userRepo.save(newUser);
            showAlert("Registration successful! You may now log in.");
        } catch (Exception e){
            e.printStackTrace();
            showAlert("Registration failed: " + e.getMessage());
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Status");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onClickSwitchToLogin(ActionEvent event) {
        OnClickUtil.showLogin(event);
    }


}
