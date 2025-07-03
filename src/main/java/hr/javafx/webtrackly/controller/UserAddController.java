package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.UserDbRepository1;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.enums.GenderType;
import hr.javafx.webtrackly.app.exception.EMailValidatorException;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.PasswordUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAddController {
    @FXML
    private TextField userTextFieldFirstName;

    @FXML
    private TextField userTextFieldLastName;

    @FXML
    private DatePicker userDatePickerBirth;

    @FXML
    private TextField userTextFieldNationality;

    @FXML
    private ComboBox<GenderType> userComboBoxGender;

    @FXML
    private TextField userTextFieldUsername;

    @FXML
    private TextField userTextFieldEmail;

    @FXML
    private TextField userTextFieldPassword;

    @FXML
    private ComboBox<Website> userComboBoxWebsite;

    private UserDbRepository1<User> userRepository = new UserDbRepository1<>();
    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();

    public void initialize() {
        userComboBoxGender.getItems().setAll(GenderType.values());
        userComboBoxWebsite.getItems().setAll(websiteRepository.findAll());
    }

    public void addUser(){
        StringBuilder errorMessages = new StringBuilder();

        String firstName = userTextFieldFirstName.getText();
        if(firstName.isEmpty()){
            errorMessages.append("First name is required!\n");
        }

        String lastName = userTextFieldLastName.getText();
        if(lastName.isEmpty()){
            errorMessages.append("Last name is required!\n");
        }

        LocalDate dateOfBirth = userDatePickerBirth.getValue();
        Optional<LocalDate> optDateOfBirth = Optional.ofNullable(dateOfBirth);
        if (optDateOfBirth.isPresent()) {
            dateOfBirth = optDateOfBirth.get();
        } else {
            errorMessages.append("Date of birth is required!\n");
        }

        String nationality = userTextFieldNationality.getText();
        if(nationality.isEmpty()){
            errorMessages.append("Nationality is required!\n");
        }

        GenderType gender = userComboBoxGender.getValue();
        Optional<GenderType> optGender = Optional.ofNullable(gender);
        if (optGender.isPresent()) {
            gender = optGender.get();
        } else {
            errorMessages.append("Gender is required!\n");
        }

        String username = userTextFieldUsername.getText();
        if(username.isEmpty()){
            errorMessages.append("Username is required!\n");
        }

        String email = userTextFieldEmail.getText();
        try {
            validateEmail(email);
            if(email.isEmpty()){
                errorMessages.append("Username is required!\n");
            }
        } catch (EMailValidatorException e) {
            errorMessages.append(e.getMessage()).append("\n");
        }

        String password = userTextFieldPassword.getText();
        if(password.isEmpty()){
            errorMessages.append("Password is required!\n");
        }
        String hashedPassword = PasswordUtil.hashPassword(password);

        Role role = new UserRole();

        Website selectedWebsite = userComboBoxWebsite.getValue();
        Optional<Website> optWebsite = Optional.ofNullable(selectedWebsite);
        if (optWebsite.isPresent()) {
            selectedWebsite = optWebsite.get();
        } else {
            errorMessages.append("Website is required!\n");
        }

        Long websiteId = Optional.ofNullable(selectedWebsite)
                .map(Website::getId)
                .orElse(0L);


        if(errorMessages.length() > 0){
            ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
        } else{
            User newUser = new User.Builder()
                    .setName(firstName)
                    .setSurname(lastName)
                    .setPersonalData(new PersonalData(dateOfBirth, nationality, gender))
                    .setUsername(username)
                    .setEmail(email)
                    .setPassword(hashedPassword)
                    .setRole(role)
                    .setWebsiteId(websiteId)
                    .build();

            userRepository.save(newUser);

            DataSerialization change = new DataSerialization(
                    "User Created",
                    "N/A",
                    newUser.getUsername(),
                    newUser.getRole().toString(),
                    LocalDateTime.now()
            );

            DataSerializeUtil.serializeData(change);
            ShowAlertUtil.showAlert("Success", "User is successfully added!", Alert.AlertType.INFORMATION);
            clearForm();
        }

    }

    public static Boolean validateEmail(String email) throws EMailValidatorException {

        Pattern emailRegex =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = emailRegex.matcher(email);

        if (!matcher.matches()) {
            throw new EMailValidatorException("Email " + email + " is not valid");
        }
        else {
            return true;
        }
    }

    private void clearForm(){
        userTextFieldFirstName.clear();
        userTextFieldLastName.clear();
        userDatePickerBirth.getEditor().clear();
        userTextFieldNationality.clear();
        userComboBoxGender.getSelectionModel().clearSelection();
        userTextFieldUsername.clear();
        userTextFieldEmail.clear();
        userTextFieldPassword.clear();
        userComboBoxWebsite.getSelectionModel().clearSelection();
    }
}
