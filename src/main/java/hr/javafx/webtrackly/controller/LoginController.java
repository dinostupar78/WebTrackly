package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.files.UserFileRepository;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.PasswordUtil;
import hr.javafx.webtrackly.utils.ScreenChangeUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField loginTextFieldUsername;

    @FXML
    private TextField loginTextFieldPassword;

    @FXML
    private ComboBox<Role> loginComboBoxRole;

    public void initialize() {
        loginComboBoxRole.getItems().addAll(new AdminRole(), new MarketingRole());
    }

    public void onClickLogin() {
        String username = loginTextFieldUsername.getText();
        String password = loginTextFieldPassword.getText();
        Role role = loginComboBoxRole.getValue();

        String hashedInputPassword = PasswordUtil.hashPassword(password);

        UserFileRepository<User> userRepo = new UserFileRepository<>();
        List<User> users = userRepo.findAll();


        Optional<User> matchingUser = users.stream()
                .filter(u -> {
                    return u.getUsername().equals(username)
                            && u.getHashedPassword().equals(hashedInputPassword)
                            && u.getRole().getClass().getSimpleName().equals(role.getClass().getSimpleName());
                })
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/javafx/webtrackly/websitePanel.fxml"));
            Parent dashboardRoot = loader.load();

            Scene dashboardScene = new Scene(dashboardRoot, 800, 600);

            Stage stage = (Stage) loginTextFieldUsername.getScene().getWindow();

            stage.setScene(dashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error opening dashboard: " + e.getMessage());
        }
    }










}
