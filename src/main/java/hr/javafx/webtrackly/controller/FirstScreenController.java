package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.main.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import static hr.javafx.webtrackly.main.HelloApplication.log;

public class FirstScreenController {
    @FXML
    private MenuBar menuBar;

    public Scene showItemSearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/hr/javafx/webtrackly/loginPanel.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scene;
    }

    public void onClickLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("You will be redirected to the login screen.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logoutUser();
        }
    }

    private void logoutUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/javafx/webtrackly/loginPanel.fxml"));
            Parent loginRoot = loader.load();

            Scene loginScene = new Scene(loginRoot, 800, 600);

            Stage stage = (Stage) menuBar.getScene().getWindow();

            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            log.info("Error logging out: " + e.getMessage());
            showAlert("Error logging out: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Notification");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
