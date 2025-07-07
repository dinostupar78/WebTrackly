package hr.javafx.webtrackly.controller;
import hr.javafx.webtrackly.app.model.MarketingRole;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.UserSession;
import hr.javafx.webtrackly.main.App;
import hr.javafx.webtrackly.utils.DbActiveUtil;
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
import static hr.javafx.webtrackly.main.App.log;
import static hr.javafx.webtrackly.utils.ShowAlertUtil.showAlert;

/**
 * Kontroler za prvi ekran aplikacije WebTrackly.
 * Ovaj kontroler upravlja prikazom i interakcijom s početnim ekranom aplikacije.
 */

public class FirstScreenController {
    @FXML
    private TitledPane menuUser;

    @FXML
    private TitledPane menuLog;

    /**
     * Inicijalizacija kontrolera.
     * Ova metoda se poziva prilikom učitavanja FXML datoteke i postavlja vidljivost izbornika
     * ovisno o ulozi trenutnog korisnika.
     */

    public void initialize() {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser.getRole() instanceof MarketingRole) {
            menuUser.setVisible(false);
            menuLog.setVisible(false);
        }
    }

    /**
     * Metoda koja se poziva kada korisnik klikne na gumb za odjavu.
     * Prikazuje potvrdu o odjavi i, ako korisnik potvrdi, postavlja trenutnog korisnika na null
     * i prikazuje početni ekran prijave.
     *
     * @param event Događaj klika na gumb za odjavu.
     */

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

    /**
     * Metoda koja prikazuje početni ekran aplikacije.
     * Učitava FXML datoteku za početni ekran i vraća novu scenu.
     *
     * @return Nova scena s početnim ekranom.
     */

    public Scene showWelcomeScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/hr/javafx/webtrackly/welcomeScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1144, 771);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scene;
    }

    /**
     * Metode za navigaciju na različite panele aplikacije.
     * Svaka metoda koristi ScreenChangeUtil za promjenu prikaza.
     */

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
        if (!DbActiveUtil.isDatabaseOnline()) {
            log.error("Database is inactive. Please check your connection.");
            showAlert("Database error", "Database is inactive. Please check your connection.", Alert.AlertType.ERROR);
            return;
        }

        ScreenChangeUtil searchClickUtils = new ScreenChangeUtil();
        searchClickUtils.showUserAddPanel(event);
    }

    public void onClickShowDataSerializationPanel(ActionEvent event) {
        ScreenChangeUtil searchClickUtils = new ScreenChangeUtil();
        searchClickUtils.showDataSerializationPanel(event);
    }


}
