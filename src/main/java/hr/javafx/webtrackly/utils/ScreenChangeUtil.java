package hr.javafx.webtrackly.utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Utility klasa za promjenu ekrana u JavaFX aplikaciji.
 * Ova klasa sadrži metode za prikaz različitih panela u aplikaciji WebTrackly.
 */

public class ScreenChangeUtil {
    /**
     * Prikazuje panel za prijavu korisnika.
     * Učitava FXML datoteku za prijavu i postavlja scenu na trenutni prozor.
     * @param event događaj koji pokreće promjenu ekrana
     */
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

    /**
     * Prikazuje panel za registraciju korisnika.
     * Učitava FXML datoteku za registraciju i postavlja scenu na trenutni prozor.
     *
     * @param event događaj koji pokreće promjenu ekrana
     */

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

    /**
     * Prikazuje glavni panel aplikacije.
     * Učitava FXML datoteku za glavni panel i postavlja scenu na trenutni prozor.
     *
     * @param event događaj koji pokreće promjenu ekrana
     */

    public void showDashboardPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/website.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prikazuje panel za akcije korisnika.
     * Učitava FXML datoteku za akcije korisnika i postavlja scenu na trenutni prozor.
     *
     * @param event događaj koji pokreće promjenu ekrana
     */

    public void showUserActionPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/userAction.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prikazuje panel za sesije korisnika.
     * Učitava FXML datoteku za sesije i postavlja scenu na trenutni prozor.
     *
     * @param event događaj koji pokreće promjenu ekrana
     */

    public void showSessionPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/session.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prikazuje panel za evidenciju prometa.
     * Učitava FXML datoteku za evidenciju prometa i postavlja scenu na trenutni prozor.
     *
     * @param event događaj koji pokreće promjenu ekrana
     */

    public void showTrafficRecordPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/trafficRecord.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prikazuje panel za korisničke podatke.
     * Učitava FXML datoteku za korisničke podatke i postavlja scenu na trenutni prozor.
     * @param event događaj koji pokreće promjenu ekrana
     */

    public void showUserPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/user.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prikazuje panel za dodavanje korisnika.
     * Učitava FXML datoteku za dodavanje korisnika i postavlja scenu na trenutni prozor.
     *
     * @param event događaj koji pokreće promjenu ekrana
     */

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

    /**
     * Prikazuje panel za logove aplikacije.
     * Učitava FXML datoteku za logove i postavlja scenu na trenutni prozor.
     *
     * @param event događaj koji pokreće promjenu ekrana
     */

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

    /**
     * Prikazuje panel za serijalizaciju podataka.
     * Učitava FXML datoteku za serijalizaciju i postavlja scenu na trenutni prozor.
     *
     * @param event događaj koji pokreće promjenu ekrana
     */

    public void showDataSerializationPanel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/hr/javafx/webtrackly/dataSerialization.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
