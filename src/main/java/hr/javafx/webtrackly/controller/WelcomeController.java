package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.utils.ScreenChangeUtil;
import javafx.event.ActionEvent;

public class WelcomeController {
    public void onClickSwitchToLogin(ActionEvent event) {
        ScreenChangeUtil.showLoginPanel(event);
    }
}
