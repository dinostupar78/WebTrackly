package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.enums.WebsiteType;
import hr.javafx.webtrackly.app.generics.EditData;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.utils.RowDeletion2Util;
import hr.javafx.webtrackly.utils.RowEditUtil;
import hr.javafx.webtrackly.utils.ScreenChangeButtonUtil;
import hr.javafx.webtrackly.utils.WebsiteLabelUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

public class WebsiteController {
    @FXML
    private TextField websiteSearchTextField;

    @FXML
    private TextField websiteSearchIDTextField;

    @FXML
    private TableView<Website> websiteTableView;

    @FXML
    private TableColumn<Website, String> websiteTableColumnID;

    @FXML
    private TableColumn<Website, String> websiteTableColumnName;

    @FXML
    private TableColumn<Website, String> websiteTableColumnUrl;

    @FXML
    private TableColumn<Website, Integer> websiteTableColumnUsers;

    @FXML
    private TableColumn<Website, String> websiteTableColumnCategory;

    @FXML
    private TableColumn<Website, String> websiteTableColumnDescription;

    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label newUsersLabel;

    @FXML
    private Label totalClicksLabel;

    @FXML
    private Label highestClicksLabel;

    @FXML
    private Label avgBounceRateLabel;

    @FXML
    private Label highestBounceRateLabel;

    @FXML
    private Label mostFrequentDomainLabel;

    @FXML
    private Label mostFrequentDomainCountLabel;

    @FXML
    private Button deleteWebsite;

    @FXML
    private BarChart<String, Number> pageViewsByCategoryChart;

    @FXML
    private PieChart categoryBreakdownChart;

    WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();

    private WebsiteLabelUtil cardsUtil;

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

        websiteTableColumnUrl.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getWebsiteUrl())
        );

        websiteTableColumnUsers.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getUsers().size()).asObject()
        );

        websiteTableColumnCategory.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getWebsiteCategory().name())
        );

        websiteTableColumnDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getWebsiteDescription())
        );

        RowDeletion2Util.addWebsiteRowDeletionHandler(websiteTableView);

        deleteWebsite.setOnAction(event -> RowDeletion2Util.deleteWebsiteWithConfirmation(websiteTableView));

        RowEditUtil<Website> rowEditUtil = new RowEditUtil<>();
        rowEditUtil.addRowEditHandler(websiteTableView, selectedWebsite -> {
            EditData<Website> container = new EditData<>(selectedWebsite);
            ScreenChangeButtonUtil.openWebsiteEditScreen(container.getData());
        });





    }

    public void filterWebsites() {

        List<Website> initialWebsiteList = websiteRepository.findAll();

        String websiteID = websiteSearchIDTextField.getText();
        if(!(websiteID.isEmpty())){
            initialWebsiteList = initialWebsiteList.stream()
                    .filter(website -> website.getId() == Integer.parseInt(websiteID))
                    .toList();
        }

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

        pageViewsByCategoryChart(initialWebsiteList);
        showCategoryBreakdownPieChart(initialWebsiteList);

        cardsUtil = new WebsiteLabelUtil(
                mostFrequentDomainLabel,
                mostFrequentDomainCountLabel
        );

        cardsUtil.frequentDomainLabel();

    }

    private void showCategoryBreakdownPieChart(List<Website> websites) {
        categoryBreakdownChart.getData().clear();

        Map<WebsiteType, Long> byCategory = websites.stream()
                .collect(Collectors.groupingBy(Website::getWebsiteCategory, Collectors.counting()));

        byCategory.forEach((category, count) ->
                categoryBreakdownChart.getData().add(new PieChart.Data(category.name(), count)));

        categoryBreakdownChart.setLegendSide(Side.BOTTOM);
        categoryBreakdownChart.setLabelsVisible(true);
    }

    private void pageViewsByCategoryChart(List<Website> websites) {
        pageViewsByCategoryChart.getData().clear();

        Map<WebsiteType, Long> countByCategory = websites.stream()
                .collect(Collectors.groupingBy(Website::getWebsiteCategory, Collectors.counting()));

        XYChart.Series<String,Number> series = new XYChart.Series<>();

        countByCategory.forEach((category,count) ->
                series.getData().add(new XYChart.Data<>(category.name(), count)));

        pageViewsByCategoryChart.getData().add(series);

        CategoryAxis xAxis = (CategoryAxis)pageViewsByCategoryChart.getXAxis();
        xAxis.setTickLabelRotation(-45);
        pageViewsByCategoryChart.setLegendVisible(false);

    }

}
