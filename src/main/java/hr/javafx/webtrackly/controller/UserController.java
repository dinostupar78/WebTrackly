package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.UserDbRepository1;
import hr.javafx.webtrackly.app.generics.EditData;
import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.utils.RowDeletion1Util;
import hr.javafx.webtrackly.utils.RowEditUtil;
import hr.javafx.webtrackly.utils.ScreenChangeButtonUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class UserController {
    @FXML
    private TextField userTextFieldID;

    @FXML
    private TextField userTextFieldUsername;

    @FXML
    private TextField userTextFieldFirstName;

    @FXML
    private TextField userTextFieldLastName;

    @FXML
    private TextField userTextFieldNationality;

    @FXML
    private ComboBox<Role> userComboBoxRole;

    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<User, String> userColumnID;

    @FXML
    private TableColumn<User, String> userColumnUsername;

    @FXML
    private TableColumn<User, String> userColumnFirstName;

    @FXML
    private TableColumn<User, String> userColumnLastName;

    @FXML
    private TableColumn<User, String> userColumnNationality;

    @FXML
    private TableColumn<User, String> userColumnRole;

    @FXML
    private TableColumn<User, String> userColumnRegistrationDate;

    @FXML
    private Button deleteUser;

    private UserDbRepository1<User> userRepository = new UserDbRepository1<>();

    public void initialize(){
        userColumnID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        userColumnUsername.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getUsername()))
        );

        userColumnFirstName.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getFirstName()))
        );

        userColumnLastName.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getLastName()))
        );

        userColumnNationality.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPersonalData().nationality()))
        );


        userColumnRole.setCellValueFactory(cellData -> {
            Role role = cellData.getValue().getRole();
            String roleText = (role != null) ? role.toString() : "No Role";
            return new SimpleStringProperty(roleText);
        });

        userComboBoxRole.getItems().clear();
        userComboBoxRole.getItems().addAll(new AdminRole(), new MarketingRole(), new UserRole());

        userComboBoxRole.setConverter(new javafx.util.StringConverter<Role>() {
            @Override
            public String toString(Role role) {
                return (role != null) ? role.toString() : "";
            }
            @Override
            public Role fromString(String string) {
                return null;
            }
        });

        userColumnRegistrationDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRegistrationDate()))
        );

        RowDeletion1Util.addUserRowDeletionHandler(userTableView);

        deleteUser.setOnAction(event -> RowDeletion1Util.deleteUserWithConfirmation(userTableView));

        RowEditUtil<User> rowEditUtil = new RowEditUtil<>();
        rowEditUtil.addRowEditHandler(userTableView, selectedUser -> {
            EditData<User> container = new EditData<>(selectedUser);
            ScreenChangeButtonUtil.openUserEditScreen(container.getData());
        });

    }

    public void filterUsers(){
        List<User> initialUserList = userRepository.findAll();

        String userID = userTextFieldID.getText();
        if(!(userID.isEmpty())){
            initialUserList = initialUserList.stream()
                    .filter(user -> user.getId().equals(Long.valueOf(userID)))
                    .toList();
        }

        String username = userTextFieldUsername.getText();
        if(!(username.isEmpty())){
            initialUserList = initialUserList.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .toList();
        }

        String firstName = userTextFieldFirstName.getText();
        if(!(firstName.isEmpty())){
            initialUserList = initialUserList.stream()
                    .filter(user -> user.getFirstName().equals(firstName))
                    .toList();
        }

        String lastName = userTextFieldLastName.getText();
        if(!(lastName.isEmpty())){
            initialUserList = initialUserList.stream()
                    .filter(user -> user.getLastName().equals(lastName))
                    .toList();
        }

        String nationality = userTextFieldNationality.getText();
        if(!(nationality.isEmpty())){
            initialUserList = initialUserList.stream()
                    .filter(user -> user.getPersonalData().nationality().equals(nationality))
                    .toList();
        }

        Role selectedRole = userComboBoxRole.getValue();
        if(selectedRole != null) {
            String selectedPermission = selectedRole.toString();
            initialUserList = initialUserList.stream()
                    .filter(user -> {
                        Role userRole = user.getRole();
                        return userRole != null && userRole.toString().equals(selectedPermission);
                    })
                    .toList();
        }

        ObservableList<User> userObservableList = observableArrayList(initialUserList);

        SortedList<User> sortedList = new SortedList<>(userObservableList);

        sortedList.comparatorProperty().bind(userTableView.comparatorProperty());

        userTableView.setItems(sortedList);


    }


}
