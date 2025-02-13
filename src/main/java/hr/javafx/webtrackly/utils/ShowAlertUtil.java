package hr.javafx.webtrackly.utils;

import javafx.scene.control.Alert;

public class ShowAlertUtil {
    private ShowAlertUtil() {}

    public static void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
