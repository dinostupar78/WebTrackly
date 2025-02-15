package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.UserActionDbRepository;
import hr.javafx.webtrackly.app.model.UserAction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class UserActionController {
    @FXML
    private TextField actionTextFieldID;

    @FXML
    private TextField actionTextFieldUser;

    @FXML
    private DatePicker actionDatePickerTimestamp;

    @FXML
    private TableView<UserAction> userActionTableView;

    @FXML
    private TableColumn<UserAction, String> userActionTableColumnID;

    @FXML
    private TableColumn<UserAction, String> userActionTableColumnUser;

    @FXML
    private TableColumn<UserAction, String> userActionTableColumnAction;

    @FXML
    private TableColumn<UserAction, String> userActionTableColumnTimestamp;

    @FXML
    private TableColumn<UserAction, String> userActionTableColumnDetails;

    private UserActionDbRepository<UserAction> userActionRepository = new UserActionDbRepository<>();

    public void initialize() {
        userActionTableColumnID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        userActionTableColumnUser.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getUser().getUsername()))
        );

        userActionTableColumnAction.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAction()))
        );

        userActionTableColumnTimestamp.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getTimestamp()))
        );

        userActionTableColumnDetails.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDetails()))
        );

        userActionTableView.getSortOrder().add(userActionTableColumnID);
    }

    public void filterUserActions(){
        List<UserAction> initialUserActionList = userActionRepository.findAll();

        String userActionID = actionTextFieldID.getText();
        if(!(userActionID.isEmpty())){
            initialUserActionList = initialUserActionList.stream()
                    .filter(action -> action.getId().toString().contains(userActionID))
                    .toList();
        }

        String userActionUsername = actionTextFieldUser.getText();
        if(!(userActionUsername.isEmpty())){
            initialUserActionList = initialUserActionList.stream()
                    .filter(action -> action.getUser().getUsername().toString().contains(userActionUsername))
                    .toList();
        }

        if (actionDatePickerTimestamp.getValue() != null) {
            String userActionTimestamp = actionDatePickerTimestamp.getValue()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE);
            initialUserActionList = initialUserActionList.stream()
                    .filter(action -> action.getTimestamp().toString().contains(userActionTimestamp))
                    .toList();
        }

        ObservableList<UserAction> userActionObservableList = observableArrayList(initialUserActionList);

        SortedList<UserAction> sortedList = new SortedList<>(userActionObservableList);

        sortedList.comparatorProperty().bind(userActionTableView.comparatorProperty());

        userActionTableView.setItems(sortedList);
    }


}
