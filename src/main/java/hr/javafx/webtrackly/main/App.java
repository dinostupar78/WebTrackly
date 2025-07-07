package hr.javafx.webtrackly.main;
import hr.javafx.webtrackly.controller.FirstScreenController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * Glavna klasa JavaFX aplikacije koja pokreće korisničko sučelje aplikacije WebTrackly.
 * Inicijalizira primarni prozor aplikacije i prikazuje početni ekran.
 */

public class App extends Application {
    /**
     * Logger za praćenje događaja u aplikaciji.
     */
    public static final Logger log = LoggerFactory.getLogger(App.class);

    /**
     * Metoda koja se poziva prilikom pokretanja aplikacije.
     * Postavlja naslov prozora i prikazuje početni ekran.
     *
     * @param stage Primarni prozor aplikacije.
     * @throws IOException Ako dođe do greške pri učitavanju FXML datoteke.
     */
    @Override
    public void start(Stage stage) throws IOException {
        log.info("Application started!");
        stage.setTitle("WebTrackly");
        stage.setScene(new FirstScreenController().showWelcomeScreen());
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}