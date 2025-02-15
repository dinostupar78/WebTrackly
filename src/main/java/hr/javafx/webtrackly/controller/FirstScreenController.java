package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.model.MarketingRole;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.UserSession;
import hr.javafx.webtrackly.main.HelloApplication;
import hr.javafx.webtrackly.utils.ScreenChangeUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;

import java.io.IOException;

public class FirstScreenController {
    @FXML
    private Menu menuUser;

    @FXML
    private Menu menuLog;

    public void initialize() {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser.getRole() instanceof MarketingRole) {
            menuUser.setVisible(false);
            menuLog.setVisible(false);
        }
    }

    public Scene showLoginPanel() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/hr/javafx/webtrackly/loginPanel.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 600);
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


}
