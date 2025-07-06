package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.files.UserFileRepository;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import hr.javafx.webtrackly.utils.PasswordUtil;
import hr.javafx.webtrackly.utils.ScreenChangeUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField loginTextFieldUsername;

    @FXML
    private PasswordField loginPasswordFieldPass;

    @FXML
    private ComboBox<Role> loginComboBoxRole;

    public void initialize() {
        loginComboBoxRole.getItems().addAll(new AdminRole(), new MarketingRole());
    }

    public void onClickLogin() {
        if(!DbActiveUtil.isDatabaseOnline()) {
            showAlert("Database is offline. Please check your connection.");
            loginTextFieldUsername.clear();
            loginPasswordFieldPass.clear();
            loginComboBoxRole.getSelectionModel().selectFirst();
            return;

        }

        String username = loginTextFieldUsername.getText();
        String password = loginPasswordFieldPass.getText();
        Role role = loginComboBoxRole.getValue();

        String hashedInputPassword = PasswordUtil.hashPassword(password);

        UserFileRepository<User> userRepository = new UserFileRepository<>();
        List<User> users = userRepository.findAll();


        Optional<User> matchingUser = users.stream()
                .filter(u -> u.getUsername().equals(username)
                        && u.getPassword().equals(hashedInputPassword)
                        && role.getClass().isAssignableFrom(u.getRole().getClass()))
                .findFirst();


        if (matchingUser.isPresent()) {
            UserSession.createSession(matchingUser.get());
            showAlert("Login successful! Welcome, " + username + "!");
            openDashboard();
        } else {
            showAlert("Invalid credentials. Please try again.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Status");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onClickRegister(ActionEvent event) {
        ScreenChangeUtil.showRegisterPanel(event);
    }

    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/javafx/webtrackly/website.fxml"));
            Parent dashboardRoot = loader.load();

            Scene dashboardScene = new Scene(dashboardRoot, 1144, 771);

            Stage stage = (Stage) loginTextFieldUsername.getScene().getWindow();

            stage.setScene(dashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error opening dashboard: " + e.getMessage());
        }
    }










}
