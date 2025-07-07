package hr.javafx.webtrackly.controller;
import hr.javafx.webtrackly.utils.ScreenChangeUtil;
import javafx.event.ActionEvent;

/**
 * Kontroler za dobrodošlicu koji upravlja događajima na početnom ekranu aplikacije.
 * Ovaj kontroler omogućuje korisniku da se prebaci na ekran za prijavu.
 */

public class WelcomeController {
    public void onClickSwitchToLogin(ActionEvent event) {
        ScreenChangeUtil.showLoginPanel(event);
    }
}
