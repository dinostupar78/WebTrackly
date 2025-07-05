package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.model.MarketingRole;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.UserSession;
import hr.javafx.webtrackly.main.HelloApplication;
import hr.javafx.webtrackly.utils.ScreenChangeUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class FirstScreenController {
    @FXML
    private TitledPane menuUser;

    @FXML
    private TitledPane menuLog;

    public void initialize() {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser.getRole() instanceof MarketingRole) {
            menuUser.setVisible(false);
            menuLog.setVisible(false);
        }
    }

    public void onClickLogout(ActionEvent event) {
        Optional<ButtonType> result = ShowAlertUtil.showConfirmationAlert("Logout Confirmation", "Are you sure you want to log out?");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            UserSession.getInstance().setCurrentUser(null);
            ShowAlertUtil.showAlert("Logout Success", "You have been logged out!", Alert.AlertType.INFORMATION);

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
    }

    public Scene showWelcomeScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/hr/javafx/webtrackly/welcomeScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1144, 771);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scene;
    }

    public void onClickShowDashboardPanel(ActionEvent event) {
        ScreenChangeUtil searchClickUtils = new ScreenChangeUtil();
        searchClickUtils.showDashboardPanel(event);
    }

    public void onClickShowUserActionPanel(ActionEvent event) {
        ScreenChangeUtil searchClickUtils = new ScreenChangeUtil();
        searchClickUtils.showUserActionPanel(event);
    }

    public void onClickShowSessionPanel(ActionEvent event) {
        ScreenChangeUtil searchClickUtils = new ScreenChangeUtil();
        searchClickUtils.showSessionPanel(event);
    }

    public void onClickShowTrafficRecordPanel(ActionEvent event) {
        ScreenChangeUtil searchClickUtils = new ScreenChangeUtil();
        searchClickUtils.showTrafficRecordPanel(event);
    }

    public void onClickShowUserPanel(ActionEvent event) {
        ScreenChangeUtil searchClickUtils = new ScreenChangeUtil();
        searchClickUtils.showUserPanel(event);
    }

    public void onClickShowLogPanel(ActionEvent event) {
        ScreenChangeUtil searchClickUtils = new ScreenChangeUtil();
        searchClickUtils.showLogPanel(event);
    }

    public void onClickShowUserAddPanel(ActionEvent event) {
        ScreenChangeUtil searchClickUtils = new ScreenChangeUtil();
        searchClickUtils.showUserAddPanel(event);
    }

    public void onClickShowDataSerializationPanel(ActionEvent event) {
        ScreenChangeUtil searchClickUtils = new ScreenChangeUtil();
        searchClickUtils.showDataSerializationPanel(event);
    }


}
