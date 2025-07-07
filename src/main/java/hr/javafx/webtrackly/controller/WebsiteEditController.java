package hr.javafx.webtrackly.controller;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository2;
import hr.javafx.webtrackly.app.enums.WebsiteType;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

/**
 * Kontroler za uređivanje web stranica u aplikaciji WebTrackly.
 * Omogućuje korisnicima da ažuriraju informacije o web stranicama.
 */

public class WebsiteEditController {
    @FXML
    private TextField websiteEditTextFieldName;

    @FXML
    private TextField websiteEditTextFieldUrl;

    @FXML
    private ComboBox<WebsiteType> websiteEditComboBoxCategory;

    @FXML
    private TextField websiteEditTextFieldDescription;

    private WebsiteDbRepository2<Website> websiteRepository = new WebsiteDbRepository2<>();
    private Website website;

    /**
     * Postavlja web stranicu koja se uređuje.
     * Ova metoda inicijalizira polja u sučelju s podacima web stranice.
     *
     * @param website Web stranica koja se uređuje.
     */

    public void setWebsite(Website website){
        this.website = website;

        websiteEditTextFieldName.setText(website.getWebsiteName());
        websiteEditTextFieldUrl.setText(website.getWebsiteUrl());
        websiteEditComboBoxCategory.getItems().setAll(WebsiteType.values());
        websiteEditTextFieldDescription.setText(website.getWebsiteDescription());
    }

    /**
     * Inicijalizira polja u sučelju za uređivanje web stranica.
     * Postavlja prazne vrijednosti za ime, URL, kategoriju i opis web stranice.
     */

    public void initialize() {
        websiteEditTextFieldName.setText("");
        websiteEditTextFieldUrl.setText("");
        websiteEditComboBoxCategory.getItems().setAll(WebsiteType.values());
        websiteEditTextFieldDescription.setText("");
    }

    /**
     * Metoda koja se poziva kada korisnik želi urediti web stranicu.
     * Provjerava unesene podatke i ažurira web stranicu u bazi podataka.
     * Ako su podaci ispravni, prikazuje poruku o uspjehu, inače prikazuje greške.
     */

    public void editWebsite(){
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultEdit();
        if (result.isPresent() && result.get() == ButtonType.OK){
            StringBuilder errorMessages = new StringBuilder();

            String name = websiteEditTextFieldName.getText();
            if (name.isEmpty()) {
                errorMessages.append("Name is required!\n");
            }

            String url = websiteEditTextFieldUrl.getText();
            if (url.isEmpty()) {
                errorMessages.append("Url is required!\n");
            }

            WebsiteType websiteType = websiteEditComboBoxCategory.getValue();
            Optional<WebsiteType> selectedType = Optional.ofNullable(websiteType);
            if (selectedType.isPresent()) {
                websiteType = selectedType.get();
            } else {
                errorMessages.append("Category is required!\n");
            }

            String description = websiteEditTextFieldDescription.getText();
            if (description.isEmpty()) {
                errorMessages.append("Description is required!\n");
            }

            if (errorMessages.length() > 0){
                ShowAlertUtil.showAlert("Error", errorMessages.toString(), Alert.AlertType.ERROR);
            } else {
                String oldWebsiteData = website.toString();

                Website newWebsite = new Website.Builder()
                        .setId(website.getId())
                        .setWebsiteName(name)
                        .setWebsiteUrl(url)
                        .setWebsiteCategory(websiteType)
                        .setWebsiteDescription(description)
                        .setUsers(new HashSet<>())
                        .build();

                websiteRepository.update(newWebsite);

                String roleString = Optional.ofNullable(UserSession.getInstance().getCurrentUser())
                        .map(User::getRole)
                        .map(Role::toString)
                        .orElse("UNKNOWN");

                DataSerialization change = new DataSerialization(
                        "Website Edited",
                        oldWebsiteData,
                        newWebsite.toString(),
                        roleString,
                        LocalDateTime.now()
                );

                DataSerializeUtil.serializeData(change);
                ShowAlertUtil.showAlert("Success", "Website updated!", Alert.AlertType.INFORMATION);
                clearForm();
            }

        } else{
            ShowAlertUtil.showAlert("Error", "Website not updated!", Alert.AlertType.ERROR);
        }
    }

    /**
     * Metoda koja se poziva kada korisnik želi otkazati uređivanje web stranice.
     * Vraća korisnika na prethodni ekran i čisti formu.
     */

    private void clearForm(){
        websiteEditTextFieldName.clear();
        websiteEditTextFieldUrl.clear();
        websiteEditComboBoxCategory.getSelectionModel().select(WebsiteType.OTHER);
        websiteEditTextFieldDescription.clear();

    }

}
