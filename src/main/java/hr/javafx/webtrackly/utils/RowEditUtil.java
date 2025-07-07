package hr.javafx.webtrackly.utils;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import java.util.function.Consumer;

/**
 * Util klasa za upravljanje uređivanjem redaka u TableView.
 * Ova klasa omogućuje dodavanje handlera za uređivanje redaka u TableView kada se redak dvaput klikne.
 * @param <T> tip podataka koji se prikazuje u TableView
 */

public class RowEditUtil<T> {

    /**
     * Dodaje handler za uređivanje redaka u TableView.
     * Kada se redak dvaput klikne, poziva se funkcija editHandler s odabranim elementom.
     *
     * @param tableView TableView u kojem se dodaje handler
     * @param editHandler funkcija koja se poziva prilikom uređivanja retka
     */

    public void addRowEditHandler(TableView<T> tableView, Consumer<T> editHandler) {
        tableView.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    T item = row.getItem();
                    editHandler.accept(item);
                }
            });
            return row;
        });
    }
}
