package hr.javafx.webtrackly.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenChangeUtil {
    public static void showLoginPanel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeUtil.class.getResource("/hr/javafx/webtrackly/loginPanel.fxml"));
            Parent loginRoot = loader.load();

            Scene loginScene = new Scene(loginRoot, 800, 600);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showRegisterPanel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeUtil.class.getResource("/hr/javafx/webtrackly/registerPanel.fxml"));
            Parent loginRoot = loader.load();

            Scene registerScene = new Scene(loginRoot, 800, 600);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(registerScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDashboardPanel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/javafx/webtrackly/dashboardPanel.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            MenuItem menuItem = (MenuItem) event.getSource();
            Stage stage = (Stage) (menuItem).getParentPopup().getOwnerWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserActionPanel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/javafx/webtrackly/userAction.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            MenuItem menuItem = (MenuItem) event.getSource();
            Stage stage = (Stage) (menuItem).getParentPopup().getOwnerWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSessionPanel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/javafx/webtrackly/sessionPanel.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            MenuItem menuItem = (MenuItem) event.getSource();
            Stage stage = (Stage) (menuItem).getParentPopup().getOwnerWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTrafficRecordPanel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/javafx/webtrackly/trafficRecordPanel.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            MenuItem menuItem = (MenuItem) event.getSource();
            Stage stage = (Stage) (menuItem).getParentPopup().getOwnerWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserPanel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/javafx/webtrackly/userPanel.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            MenuItem menuItem = (MenuItem) event.getSource();
            Stage stage = (Stage) (menuItem).getParentPopup().getOwnerWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserAddPanel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/javafx/webtrackly/userAddPanel.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            MenuItem menuItem = (MenuItem) event.getSource();
            Stage stage = (Stage) (menuItem).getParentPopup().getOwnerWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLogPanel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/javafx/webtrackly/logPanel.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            MenuItem menuItem = (MenuItem) event.getSource();
            Stage stage = (Stage) (menuItem).getParentPopup().getOwnerWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDataSerializationPanel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/javafx/webtrackly/dataSerializationPanel.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            MenuItem menuItem = (MenuItem) event.getSource();
            Stage stage = (Stage) (menuItem).getParentPopup().getOwnerWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
