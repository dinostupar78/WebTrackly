package hr.javafx.webtrackly.utils;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository2;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.Website;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import java.util.Optional;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * Util klasa za upravljanje brisanjem redaka u TableView-u za entitet Website.
 * Ova klasa pruža metode za dodavanje rukovatelja događajima za brisanje redaka i potvrdu brisanja.
 */

public class RowDeletion2Util {
    private RowDeletion2Util() {}

    private static final String ERROR_STRING = "Error";
    private static final String SUCCESS_STRING = "Success";

    private static final WebsiteDbRepository2<Website> websiteRepository = new WebsiteDbRepository2<>();

    /**
     * Dodaje rukovatelja događajima za brisanje redaka u TableView.
     * Kada se klikne na redak, ako je redak prazan, pokreće se metoda za brisanje s potvrdom.
     *
     * @param tableView TableView koji sadrži entitete Website
     */

    public static void addWebsiteRowDeletionHandler(TableView<Website> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<Website> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 0) {
                    deleteWebsiteWithConfirmation(tableView, row.getItem());
                }
            });
            return row;
        });
    }

    /**
     * Metoda za brisanje odabranog entiteta Website s potvrdom.
     * Ako nije odabran nijedan entitet, prikazuje se greška.
     *
     * @param tableView TableView koji sadrži entitete Website
     */

    public static void deleteWebsiteWithConfirmation(TableView<Website> tableView) {
        Website selectedWebsite = tableView.getSelectionModel().getSelectedItem();
        if (selectedWebsite == null) {
            ShowAlertUtil.showAlert(ERROR_STRING, "No website selected for deletion!", Alert.AlertType.ERROR);
            return;
        }

        deleteWebsiteWithConfirmation(tableView, selectedWebsite);
    }

    /**
     * Pomaže u brisanju odabranog entiteta Website s potvrdom.
     * Ako korisnik potvrdi brisanje, entitet se briše iz baze i iz TableView-a.
     *
     * @param tableView TableView koji sadrži entitete Website
     * @param website Odabrani entitet Website koji se briše
     */

    private static void deleteWebsiteWithConfirmation(TableView<Website> tableView, Website website) {
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultDelete();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                websiteRepository.delete(website.getId());

                ObservableList<Website> websites = observableArrayList(tableView.getItems());
                websites.remove(website);
                tableView.setItems(websites);

                ShowAlertUtil.showAlert(SUCCESS_STRING, "Website deleted successfully.", Alert.AlertType.INFORMATION);
            } catch (RepositoryException e) {
                e.printStackTrace();
                ShowAlertUtil.showAlert(ERROR_STRING, "Failed to delete user: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}
