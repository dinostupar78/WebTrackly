package hr.javafx.webtrackly.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class OnClickUtil {
    private OnClickUtil() {}

    public static void showLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(OnClickUtil.class.getResource("/hr/javafx/webtrackly/loginPanel.fxml"));
            Parent loginRoot = loader.load();

            Scene loginScene = new Scene(loginRoot, 800, 600);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(OnClickUtil.class.getResource("/hr/javafx/webtrackly/registerPanel.fxml"));
            Parent loginRoot = loader.load();

            Scene loginScene = new Scene(loginRoot, 800, 600);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
