package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.WebsiteDbRepository;
import hr.javafx.webtrackly.app.generics.EditContainer;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.RowDeletion2Util;
import hr.javafx.webtrackly.utils.RowEditUtil;
import hr.javafx.webtrackly.utils.ScreenChangeButtonUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

public class WebsiteController {
    @FXML
    private TextField websiteSearchTextField;

    @FXML
    private TableView<Website> websiteTableView;

    @FXML
    private TableColumn<Website, String> websiteTableColumnID;

    @FXML
    private TableColumn<Website, String> websiteTableColumnName;

    @FXML
    private TableColumn<Website, String> websiteTableColumnClicks;

    @FXML
    private TableColumn<Website, String> websiteTableColumnUrl;

    @FXML
    private TableColumn<Website, String> websiteTableColumnCount;

    @FXML
    private TableColumn<Website, String> websiteTableColumnBounceRate;

    @FXML
    private TableColumn<Website, String> websiteTableColumnUsers;

    @FXML
    private Button deleteWebsite;


    WebsiteDbRepository<Website> websiteRepository = new WebsiteDbRepository<>();

    @FXML
    private void openAddWebsiteScreen(ActionEvent event) {
        ScreenChangeButtonUtil.openWebsiteAddScreen(event);
    }

    public void initialize(){
        websiteTableColumnID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        websiteTableColumnName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getWebsiteName())
        );

        websiteTableColumnClicks.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getWebsiteClicks()))
        );

        websiteTableColumnUrl.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getWebsiteUrl())
        );

        websiteTableColumnCount.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getWebsiteUserCount()))
        );

        websiteTableColumnBounceRate.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getBounceRate()))
        );

        websiteTableColumnUsers.setCellValueFactory(cellData -> {
            String usernames = cellData.getValue().getUsers().stream()
                    .map(user -> user.getUsername())
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(usernames);
        });

        RowDeletion2Util.addWebsiteRowDeletionHandler(websiteTableView);

        deleteWebsite.setOnAction(event -> RowDeletion2Util.deleteWebsiteWithConfirmation(websiteTableView));

        RowEditUtil<Website> rowEditUtil = new RowEditUtil<>();
        rowEditUtil.addRowEditHandler(websiteTableView, selectedWebsite -> {
            EditContainer<Website> container = new EditContainer<>(selectedWebsite);
            ScreenChangeButtonUtil.openWebsiteEditScreen(container.getData());
        });

        websiteTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    public void filterWebsites() {
        List<Website> initialWebsiteList = websiteRepository.findAll();

        String websiteName = websiteSearchTextField.getText();
        if(!(websiteName.isEmpty())){
            initialWebsiteList = initialWebsiteList.stream()
                    .filter(website -> website.getWebsiteName().equals(websiteName))
                    .toList();
        }

        ObservableList<Website> userObservableList = observableArrayList(initialWebsiteList);

        SortedList<Website> sortedList = new SortedList<>(userObservableList);

        sortedList.comparatorProperty().bind(websiteTableView.comparatorProperty());

        websiteTableView.setItems(sortedList);

    }

















}
