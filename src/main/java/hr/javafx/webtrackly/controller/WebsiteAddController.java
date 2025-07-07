package hr.javafx.webtrackly.controller;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.enums.WebsiteType;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.DbActiveUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import static hr.javafx.webtrackly.main.HelloApplication.log;
import static hr.javafx.webtrackly.utils.ShowAlertUtil.showAlert;

/**
 * Kontroler za dodavanje web stranica u aplikaciji WebTrackly.
 * Ova klasa upravlja unosom podataka o web stranicama i njihovim kategorijama.
 */

public class WebsiteAddController {
    @FXML
    private TextField websiteTextFieldName;

    @FXML
    private TextField websiteTextFieldUrl;

    @FXML
    private ComboBox<WebsiteType> websiteComboBoxCategory;

    @FXML
    private TextField websiteTextFieldDescription;

    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();

    /**
     * Inicijalizira kontroler i postavlja početne vrijednosti za unos web stranica.
     * Provjerava je li baza podataka aktivna i postavlja kategorije web stranica.
     */

    public void initialize(){
        if (!DbActiveUtil.isDatabaseOnline()) {
            log.error("Database is inactive. Please check your connection.");
            showAlert("Database error", "Database is inactive. Please check your connection.", Alert.AlertType.ERROR);

            Platform.runLater(() -> {
                Stage stage = (Stage) websiteTextFieldName.getScene().getWindow();
                stage.close();
            });

            return;
        }

        websiteComboBoxCategory.getItems().setAll(WebsiteType.values());
        websiteComboBoxCategory.getSelectionModel().selectFirst();
        websiteTextFieldName.setText("");
        websiteTextFieldUrl.setText("");
        websiteTextFieldDescription.setText("");

        websiteComboBoxCategory.getSelectionModel().select(WebsiteType.OTHER);
    }

    /**
     * Metoda koja se poziva prilikom klika na gumb za dodavanje web stranice.
     * Provjerava unesene podatke i dodaje novu web stranicu u bazu podataka.
     * Ako su uneseni podaci neispravni, prikazuje upozorenje.
     */

    public void addWebsite() {
        StringBuilder errorMessages = new StringBuilder();

        String name = websiteTextFieldName.getText();
        if (name.isEmpty()) {
            errorMessages.append("Name is required!\n");
        }

        String url = websiteTextFieldUrl.getText();
        if (url.isEmpty()) {
            errorMessages.append("Url is required!\n");
        }

        WebsiteType websiteType = websiteComboBoxCategory.getValue();
        Optional<WebsiteType> selectedType = Optional.ofNullable(websiteType);
        if (selectedType.isPresent()) {
            websiteType = selectedType.get();
        } else {
            errorMessages.append("Category is required!\n");
        }

        String description = websiteTextFieldDescription.getText();
        if (description.isEmpty()) {
            errorMessages.append("Description is required!\n");
        }

        if (errorMessages.length() > 0){
            ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
        } else {
            Website newWebsite = new Website.Builder()
                    .setWebsiteName(name)
                    .setWebsiteUrl(url)
                    .setWebsiteDescription(description)
                    .setWebsiteCategory(websiteType)
                    .setUsers(new HashSet<>())
                    .build();

            websiteRepository.save(newWebsite);

            String roleString = Optional.ofNullable(UserSession.getInstance().getCurrentUser())
                    .map(User::getRole)
                    .map(Role::toString)
                    .orElse("UNKNOWN");

            DataSerialization change = new DataSerialization(
                    "Website Added",
                    "N/A",
                    newWebsite.toString(),
                    roleString,
                    LocalDateTime.now()
            );

            DataSerializeUtil.serializeData(change);
            ShowAlertUtil.showAlert("Success", "Website successfully added!", Alert.AlertType.INFORMATION);
            clearForm();

        }
    }

    /**
     * Metoda koja se poziva prilikom klika na gumb za zatvaranje prozora.
     * Zatvara prozor bez spremanja promjena.
     */

    private void clearForm(){
        websiteTextFieldName.clear();
        websiteTextFieldUrl.clear();
        websiteComboBoxCategory.getSelectionModel().select(WebsiteType.OTHER);
        websiteTextFieldDescription.clear();
    }
}
