package hr.javafx.webtrackly.controller;
import hr.javafx.webtrackly.app.db.SessionDbRepository3;
import hr.javafx.webtrackly.app.db.UserActionDbRepository3;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository3;
import hr.javafx.webtrackly.app.enums.WebsiteType;
import hr.javafx.webtrackly.app.generics.EditData;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.threads.DisplayMostFrequentActionThread;
import hr.javafx.webtrackly.threads.DisplayMostFrequentCategoryThread;
import hr.javafx.webtrackly.threads.DisplayMostFrequentDeviceThread;
import hr.javafx.webtrackly.threads.DisplayMostFrequentUrlThread;
import hr.javafx.webtrackly.utils.RowDeletion2Util;
import hr.javafx.webtrackly.utils.RowEditUtil;
import hr.javafx.webtrackly.utils.ScreenChangeButtonUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static javafx.animation.Animation.INDEFINITE;
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
    private Label mostFrequentDeviceLabel;

    @FXML
    private Label mostFrequentDeviceCountLabel;

    @FXML
    private Label mostFrequentActionCountLabel;

    @FXML
    private Label mostFrequentActionLabel;

    @FXML
    private Label mostFrequentCategoryCountLabel;

    @FXML
    private Label mostFrequentCategoryLabel;

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
    WebsiteDbRepository3 websiteRepository3 = new WebsiteDbRepository3();
    UserActionDbRepository3 userActionRepository3 = new UserActionDbRepository3();
    SessionDbRepository3 sessionRepository3 = new SessionDbRepository3();

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

        DisplayMostFrequentDeviceThread deviceThread =
                new DisplayMostFrequentDeviceThread(
                        sessionRepository3,
                        mostFrequentDeviceLabel,
                        mostFrequentDeviceCountLabel
                );

        DisplayMostFrequentActionThread actionThread =
                new DisplayMostFrequentActionThread(
                        userActionRepository3,
                        mostFrequentActionLabel,
                        mostFrequentActionCountLabel
                );

        DisplayMostFrequentUrlThread urlThread =
                new DisplayMostFrequentUrlThread(
                        websiteRepository3,
                        mostFrequentDomainLabel,
                        mostFrequentDomainCountLabel
                );

        DisplayMostFrequentCategoryThread categoryThread =
                new DisplayMostFrequentCategoryThread(
                        websiteRepository3,
                        mostFrequentCategoryLabel,
                        mostFrequentCategoryCountLabel
                );

        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    new Thread(deviceThread).start();
                    new Thread(actionThread).start();
                    new Thread(categoryThread).start();
                    new Thread(urlThread).start();
                }),
                new KeyFrame(Duration.seconds(30))
        );
        tl.setCycleCount(INDEFINITE);
        tl.play();
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
