package hr.javafx.webtrackly.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenChangeUtil {
    public static void showLoginPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    ScreenChangeUtil.class.getResource("/hr/javafx/webtrackly/login.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showRegisterPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    ScreenChangeUtil.class.getResource("/hr/javafx/webtrackly/register.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDashboardPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/websitePanel.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserActionPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/userActionPanel.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSessionPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/sessionSearchPanel.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTrafficRecordPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/trafficRecordPanel.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/userPanel.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserAddPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/userAdd.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLogPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/logs.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDataSerializationPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/dataSerializationPanel.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
