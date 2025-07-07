package hr.javafx.webtrackly.controller;
import hr.javafx.webtrackly.app.db.TrafficRecordDbRepository1;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import static hr.javafx.webtrackly.main.App.log;
import static hr.javafx.webtrackly.utils.ShowAlertUtil.showAlert;

/**
 * Kontroler za dodavanje zapisa o prometu u aplikaciji WebTrackly.
 * Ovaj kontroler upravlja unosom podataka o posjetama web stranicama i njihovo spremanje u bazu podataka.
 */

public class TrafficRecordAddController {
    @FXML
    private ComboBox<Website> trafficRecordComboBoxWebsite;

    @FXML
    private DatePicker trafficRecordDatePickerDateOfVisit;

    @FXML
    private TextField trafficRecordDatePickerTimeOfVisit;


    private ObjectBinding<LocalDateTime> dateTimeBinding;

    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
    private TrafficRecordDbRepository1<TrafficRecord> trafficRecordRepository = new TrafficRecordDbRepository1<>();

    /**
     * Inicijalizira kontroler i postavlja potrebne vrijednosti.
     * Provjerava je li baza podataka aktivna i učitava sve web stranice u ComboBox.
     * Postavlja formatiranje vremena za unos posjeta.
     */

    public void initialize() {
        if (!DbActiveUtil.isDatabaseOnline()) {
            log.error("Database is inactive. Please check your connection.");
            showAlert("Database error", "Database is inactive. Please check your connection.", Alert.AlertType.ERROR);

            Platform.runLater(() -> {
                Stage stage = (Stage) trafficRecordComboBoxWebsite.getScene().getWindow();
                stage.close();
            });

            return;
        }

        trafficRecordComboBoxWebsite.getItems().setAll(websiteRepository.findAll());

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        LocalTimeStringConverter converter = new LocalTimeStringConverter(timeFmt, timeFmt);
        TextFormatter<LocalTime> tf = new TextFormatter<>(converter, null);
        trafficRecordDatePickerTimeOfVisit.setTextFormatter(tf);

        dateTimeBinding = Bindings.createObjectBinding(() -> {
            LocalDate ld = trafficRecordDatePickerDateOfVisit.getValue();
            LocalTime lt = tf.getValue();
            return ld == null || lt == null ? null : LocalDateTime.of(ld, lt);
        }, trafficRecordDatePickerDateOfVisit.valueProperty(), tf.valueProperty());
    }

    /**
     * Metoda koja se poziva prilikom dodavanja novog zapisa o prometu.
     * Provjerava unesene podatke, stvara novi zapis i sprema ga u bazu podataka.
     * Ako su uneseni podaci neispravni, prikazuje upozorenje.
     */

    public void addTrafficRecord() {
        StringBuilder errorMessages = new StringBuilder();

        Website website = trafficRecordComboBoxWebsite.getValue();
        Optional<Website> optWebsite = Optional.ofNullable(website);
        if (optWebsite.isPresent()) {
            website = optWebsite.get();
        } else {
            errorMessages.append("Website is required!\n");
        }

        LocalDateTime timeOfVisit = dateTimeBinding.get();
        Optional<LocalDateTime> optDate = Optional.ofNullable(timeOfVisit);
        if (optDate.isPresent()) {
            timeOfVisit = optDate.get();
        } else {
            errorMessages.append("Time of visit is required!\n");
        }

        if (errorMessages.length() > 0) {
            ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
        } else {
            TrafficRecord newTrafficRecord = new TrafficRecord.Builder()
                    .setWebsite(website)
                    .setTimeOfVisit(timeOfVisit)
                    .build();

            trafficRecordRepository.save(newTrafficRecord);

            String roleString = Optional.ofNullable(UserSession.getInstance().getCurrentUser())
                    .map(User::getRole)
                    .map(Role::toString)
                    .orElse("UNKNOWN");

            DataSerialization change = new DataSerialization(
                    "Traffic Record Added",
                    "N/A",
                    newTrafficRecord.toString(),
                    roleString,
                    LocalDateTime.now()
            );

            DataSerializeUtil.serializeData(change);
            ShowAlertUtil.showAlert("Success", "Traffic Record has been successfully added!", Alert.AlertType.INFORMATION);
            clearForm();

        }
    }

    /**
     * Metoda koja se poziva prilikom zatvaranja prozora za dodavanje zapisa o prometu.
     * Zatvara prozor i vraća se na prethodni ekran.
     */

    private void clearForm() {
        trafficRecordComboBoxWebsite.getSelectionModel().clearSelection();
        trafficRecordDatePickerDateOfVisit.getEditor().clear();
        trafficRecordDatePickerTimeOfVisit.clear();
    }
}
