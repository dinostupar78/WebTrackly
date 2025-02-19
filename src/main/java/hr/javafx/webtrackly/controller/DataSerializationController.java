package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.DataSerializeUtil;
import hr.javafx.webtrackly.utils.DateFormatterUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class DataSerializationController {
    @FXML
    private TextField dataTextFieldFieldName;

    @FXML
    private TextField dataTextFieldOldValue;

    @FXML
    private TextField dataTextFieldNewValue;

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

        dataColumnChangedByRole.setCellValueFactory(cellData -> {
            String roleText = (cellData.getValue().getChangedByRole() != null) ? cellData.getValue().getChangedByRole() : "No Role";
            return new SimpleStringProperty(roleText);
        });

        dataComboBoxRole.getItems().setAll(new AdminRole(), new MarketingRole(), new UserRole());
        dataComboBoxRole.setConverter(new javafx.util.StringConverter<Role>() {
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

    public void filterData() {
        List<DataSerialization> changes = DataSerializeUtil.deserializeData();
        List<DataSerialization> initialSessionList = changes;

        String dataName = dataTextFieldFieldName.getText();
        if(!(dataName.isEmpty())){
            initialSessionList = initialSessionList.stream()
                    .filter(change -> change.getFieldName().toLowerCase().contains(dataName))
                    .toList();
        }

        String dataOldValue = dataTextFieldOldValue.getText();
        if(!(dataOldValue.isEmpty())){
            initialSessionList = initialSessionList.stream()
                    .filter(change -> change.getOldValue().toLowerCase().contains(dataOldValue))
                    .toList();
        }

        String dataNewValue = dataTextFieldNewValue.getText();
        if(!(dataNewValue.isEmpty())){
            initialSessionList = initialSessionList.stream()
                    .filter(change -> change.getNewValue().toLowerCase().contains(dataNewValue))
                    .toList();
        }

        String dataChangedByRole = String.valueOf(dataComboBoxRole.getValue());
        if(dataChangedByRole == null){
            initialSessionList = initialSessionList.stream()
                    .filter(change -> change.getChangedByRole().toLowerCase().contains(dataChangedByRole.toLowerCase()))
                    .toList();
        }


        dataTableView.getItems().setAll(initialSessionList);


    }
}
