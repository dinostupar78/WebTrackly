package hr.javafx.webtrackly.controller;
import hr.javafx.webtrackly.app.enums.GenderType;
import hr.javafx.webtrackly.app.files.UserFileRepository;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.PasswordUtil;
import hr.javafx.webtrackly.utils.ScreenChangeUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.util.Optional;
import static hr.javafx.webtrackly.main.HelloApplication.log;

/**
 * Kontroler za registraciju korisnika u aplikaciji WebTrackly.
 * Omogućuje unos osobnih podataka, korisničkog imena, lozinke i odabir uloge.
 */

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

    /**
     * Inicijalizira kontroler i postavlja vrijednosti u ComboBox elemente.
     * Ova metoda se poziva prilikom učitavanja FXML datoteke.
     */

    public void initialize() {
        registerComboBoxGender.getItems().setAll(GenderType.values());
        registerComboBoxRole.getItems().addAll(new AdminRole(), new MarketingRole());
    }

    /**
     * Metoda koja se poziva prilikom klika na gumb "Registriraj se".
     * Provjerava unesene podatke, kreira novog korisnika i sprema ga u datoteku.
     * Ako su uneseni podaci neispravni, prikazuje se upozorenje.
     *
     * @param event Događaj klika na gumb.
     */

    public void onClickRegister(ActionEvent event) {
        StringBuilder errorMessages = new StringBuilder();

        String firstName = registerTextFieldFirstName.getText();
        if(firstName.isEmpty()){
            errorMessages.append("Username is required!\n");
        }

        String lastName = registerTextFieldLastName.getText();

        if(lastName.isEmpty()){
            errorMessages.append("Last name is required!\n");
        }

        LocalDate dateOfBirth = registerDatePickerBirth.getValue();
        Optional<LocalDate> optDateOfBirth = Optional.ofNullable(dateOfBirth);
        if (optDateOfBirth.isPresent()) {
            dateOfBirth = optDateOfBirth.get();
        } else {
            errorMessages.append("Date of birth is required!\n");
        }

        String nationality = registerTextFieldNationality.getText();
        if(nationality.isEmpty()){
            errorMessages.append("Date of birth is required!\n");
        }

        GenderType gender = registerComboBoxGender.getValue();
        Optional<GenderType> optGender = Optional.ofNullable(gender);
        if (optGender.isPresent()) {
            gender = optGender.get();
        } else {
            errorMessages.append("Gender is required!\n");
        }

        String username = registerTextFieldUsername.getText();
        if(username.isEmpty()){
            errorMessages.append("Username is required!\n");
        }

        String password = registerTextFieldPassword.getText();
        if(password.isEmpty()){
            errorMessages.append("Password is required!\n");
        }
        String hashedPassword = PasswordUtil.hashPassword(password);

        Role role = registerComboBoxRole.getValue();
        Optional<Role> optRole = Optional.ofNullable(role);
        if (optRole.isPresent()) {
            role = optRole.get();
        } else {
            errorMessages.append("Role is required!\n");
        }

        if(errorMessages.length() > 0){
            ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
        } else {
            User newUser = new User.Builder()
                    .setName(firstName)
                    .setSurname(lastName)
                    .setPersonalData(new PersonalData(dateOfBirth, nationality, gender))
                    .setUsername(username)
                    .setPassword(hashedPassword)
                    .setRole(role)
                    .build();

            UserFileRepository<User> userRepo = new UserFileRepository<>();
            userRepo.save(newUser);
            try{
                showAlert("Registration successful! You may now log in.");
                ScreenChangeUtil.showLoginPanel(event);
            } catch (Exception e){
                log.error("Registration failed: {} ", e.getMessage());
                showAlert("Registration failed: " + e.getMessage());
            }
        }
    }

    /**
     * Prikazuje obavijesni dijalog s porukom o statusu registracije.
     * @param message Poruka koja će biti prikazana u dijalogu.
     */

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Status");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Metoda koja se poziva prilikom klika na gumb "Prijavi se".
     * Mijenja prikaz na ekran za prijavu.
     *
     * @param event Događaj klika na gumb.
     */

    public void onClickSwitchToLogin(ActionEvent event) {
        ScreenChangeUtil.showLoginPanel(event);
    }

}
