package hr.javafx.webtrackly.controller;
import hr.javafx.webtrackly.app.model.AdminRole;
import hr.javafx.webtrackly.app.model.DataSerialization;
import hr.javafx.webtrackly.app.model.MarketingRole;
import hr.javafx.webtrackly.app.model.Role;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.DateFormatterUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import java.util.List;
import java.util.Optional;

/**
 * Kontroler za upravljanje serijalizacijom podataka u aplikaciji WebTrackly.
 * Ovaj kontroler omogućuje filtriranje i prikaz promjena podataka u tablici.
 */

public class DataSerializationController {
    @FXML
    private TextField dataTextFieldFieldName;

    @FXML
    private ComboBox<Role> dataComboBoxRole;

    @FXML
    private TableView<DataSerialization> dataTableView;

    @FXML
    private TableColumn<DataSerialization, String> dataColumnName;

    @FXML
    private TableColumn<DataSerialization, String> dataColumnOldValue;

    @FXML
    private TableColumn<DataSerialization, String> dataColumnNewValue;

    @FXML
    private TableColumn<DataSerialization, String> dataColumnChangedByRole;

    @FXML
    private TableColumn<DataSerialization, String> dataColumnChangeTimestamp;

    /**
     * Inicijalizira kontroler i postavlja vrijednosti za kolone tablice.
     * Ova metoda se poziva prilikom učitavanja FXML datoteke.
     */

    public void initialize(){
        dataColumnName.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getFieldName()))
        );

        dataColumnOldValue.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getOldValue()))
        );

        dataColumnNewValue.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getNewValue()))
        );

        dataColumnChangedByRole.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getChangedByRole()))
        );

        dataComboBoxRole.getItems().setAll(new AdminRole(), new MarketingRole());
        dataComboBoxRole.setConverter(new StringConverter<Role>() {
            @Override
            public String toString(Role role) {
                return (role != null) ? role.toString() : "";
            }

            @Override
            public Role fromString(String string) {
                return null;
            }
        });

        dataColumnChangeTimestamp.setCellValueFactory(cellData ->
                new SimpleStringProperty(DateFormatterUtil.formatLocalDateTime(cellData.getValue().getChangeTimestamp()))
        );
    }

    /**
     * Filtrira podatke u tablici na temelju unosa u tekstualnom polju i odabranog role.
     * Ova metoda se poziva prilikom promjene unosa u tekstualnom polju ili odabira role.
     */

    public void filterData() {
        List<DataSerialization> changes = DataSerializeUtil.deserializeData();
        List<DataSerialization> initialSessionList = changes;

        String dataName = dataTextFieldFieldName.getText();
        if(!(dataName.isEmpty())){
            initialSessionList = initialSessionList.stream()
                    .filter(change -> change.getFieldName().toLowerCase().contains(dataName))
                    .toList();
        }

        String dataChangedByRole = String.valueOf(dataComboBoxRole.getValue());
        Optional<Role> selectedRole = Optional.ofNullable(dataComboBoxRole.getValue());
        if(selectedRole.isPresent()){
            initialSessionList = initialSessionList.stream()
                    .filter(change -> change.getChangedByRole().toLowerCase().contains(dataChangedByRole.toLowerCase()))
                    .toList();
        }


        dataTableView.getItems().setAll(initialSessionList);


    }
}