package hr.javafx.webtrackly.utils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Utility klasa za prikazivanje alert dijaloga u JavaFX aplikaciji.
 * Ova klasa sadrži metode za prikazivanje različitih vrsta alert dijaloga,
 * uključujući obavijesti, potvrde i upozorenja.
 */

public class ShowAlertUtil {
    private ShowAlertUtil() {}

    /**
     * Prikazuje alert dijalog s danim naslovom, porukom i tipom.
     *
     * @param title Naslov alert dijaloga
     * @param message Poruka koja će biti prikazana u alert dijalogu
     * @param alertType Tip alert dijaloga (npr. Alert.AlertType.INFORMATION, Alert.AlertType.WARNING, itd.)
     */

    public static void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Prikazuje alert dijalog s potvrdom.
     *
     * @param title Naslov alert dijaloga
     * @param message Poruka koja će biti prikazana u alert dijalogu
     * @return Optional<ButtonType> - rezultat odabira korisnika (OK ili Cancel)
     */

    public static Optional<ButtonType> showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    /**
     * Prikazuje alert dijalog s upozorenjem.
     * @return Optional<ButtonType> - rezultat odabira korisnika (OK ili Cancel)
     */

    public static Optional<ButtonType> getAlertResultDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this item?");

        return alert.showAndWait();
    }

    /**
     * Prikazuje alert dijalog s pitanjem o uređivanju.
     * @return Optional<ButtonType> - rezultat odabira korisnika (OK ili Cancel)
     */

    public static Optional<ButtonType> getAlertResultEdit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to edit this item?");

        return alert.showAndWait();
    }

}
