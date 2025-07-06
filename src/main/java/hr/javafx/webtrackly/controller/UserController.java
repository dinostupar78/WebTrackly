package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.UserDbRepository1;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.exception.RepositoryException;
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
    private TextField userTextFieldEmail;

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
    private TableColumn<User, String> userColumnWebsite;

    @FXML
    private TableColumn<User, String> userColumnEmail;

    @FXML
    private TableColumn<User, String> userColumnFirstName;

    @FXML
    private TableColumn<User, String> userColumnLastName;

    @FXML
    private TableColumn<User, String> userColumnNationality;

    @FXML
    private Button deleteUser;

    private WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();
    private UserDbRepository1<User> userRepository = new UserDbRepository1<>();

    public void initialize(){
        userColumnID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        userColumnUsername.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getUsername()))
        );

        userColumnWebsite.setCellValueFactory(cellData -> {
            Long websiteId = cellData.getValue().getWebsiteId();
            Website website = websiteRepository.findById(websiteId);
            return new SimpleStringProperty(website != null ? website.getWebsiteName() : "No Website");
        });

        userColumnEmail.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getEmail()))
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

        RowDeletion1Util.addUserRowDeletionHandler(userTableView);

        deleteUser.setOnAction(event -> RowDeletion1Util.deleteUserWithConfirmation(userTableView));

        RowEditUtil<User> rowEditUtil = new RowEditUtil<>();
        rowEditUtil.addRowEditHandler(userTableView, selectedUser -> {
            EditData<User> container = new EditData<>(selectedUser);
            ScreenChangeButtonUtil.openUserEditScreen(container.getData());
        });

    }

    public void filterUsers(){
        List<User> initialUserList;
        try{
            initialUserList = userRepository.findAll();
        } catch (RepositoryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Database is offline. Please check your connection.");
            alert.showAndWait();
            return;
        }

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

        String email = userTextFieldEmail.getText();
        if(!(email.isEmpty())){
            initialUserList = initialUserList.stream()
                    .filter(user -> user.getEmail().equals(email))
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

        ObservableList<User> userObservableList = observableArrayList(initialUserList);

        SortedList<User> sortedList = new SortedList<>(userObservableList);

        sortedList.comparatorProperty().bind(userTableView.comparatorProperty());

        userTableView.setItems(sortedList);


    }


}
