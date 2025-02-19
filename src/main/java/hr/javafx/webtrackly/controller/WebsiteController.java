package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.UserDbRepository1;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository1;
import hr.javafx.webtrackly.app.generics.ChartData;
import hr.javafx.webtrackly.app.generics.EditData;
import hr.javafx.webtrackly.app.model.Website;
import hr.javafx.webtrackly.threads.*;
import hr.javafx.webtrackly.utils.RowDeletion2Util;
import hr.javafx.webtrackly.utils.RowEditUtil;
import hr.javafx.webtrackly.utils.ScreenChangeButtonUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

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
    private LineChart<String, Number> totalClicksChart;

    @FXML
    private BarChart<String, Number> bounceRateChart;

    WebsiteDbRepository1<Website> websiteRepository = new WebsiteDbRepository1<>();

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

        RowDeletion2Util.addWebsiteRowDeletionHandler(websiteTableView);

        deleteWebsite.setOnAction(event -> RowDeletion2Util.deleteWebsiteWithConfirmation(websiteTableView));

        RowEditUtil<Website> rowEditUtil = new RowEditUtil<>();
        rowEditUtil.addRowEditHandler(websiteTableView, selectedWebsite -> {
            EditData<Website> container = new EditData<>(selectedWebsite);
            ScreenChangeButtonUtil.openWebsiteEditScreen(container.getData());
        });

    }

    public void filterWebsites() {
        showTotalClicksChart();
        showBounceRateChart();

        TotalUsersThread totalUsersThread = new TotalUsersThread(new WebsiteDbRepository1<>(), totalUsersLabel);
        Thread usersThread = new Thread(totalUsersThread);
        usersThread.start();

        NewUsersThread newUsersThread = new NewUsersThread(new UserDbRepository1<>(), newUsersLabel);
        Thread nUsersThread = new Thread(newUsersThread);
        nUsersThread.start();

        TotalClicksThread totalClicksThread = new TotalClicksThread(new WebsiteDbRepository1<>(), totalClicksLabel);
        Thread clicksThread = new Thread(totalClicksThread);
        clicksThread.start();

        HighestClicksThread highestClicksThread = new HighestClicksThread(new WebsiteDbRepository1<>(), highestClicksLabel);
        Thread hClicksThread = new Thread(highestClicksThread);
        hClicksThread.start();

        AvgBounceRateThread avgBounceRateThread = new AvgBounceRateThread(new WebsiteDbRepository1<>(), avgBounceRateLabel);
        Thread bounceRateThread = new Thread(avgBounceRateThread);
        bounceRateThread.start();

        HighestBounceRateThread highestBounceRateThread = new HighestBounceRateThread(new WebsiteDbRepository1<>(), highestBounceRateLabel);
        Thread hBounceRateThread = new Thread(highestBounceRateThread);
        hBounceRateThread.start();

        FrequentDomainsThread frequentDomainsThread = new FrequentDomainsThread(new WebsiteDbRepository1<>(), mostFrequentDomainLabel, mostFrequentDomainCountLabel);
        Thread frequentThread = new Thread(frequentDomainsThread);
        frequentThread.start();

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

    private void showTotalClicksChart() {
        List<Website> websites = websiteRepository.findAll();
        List<ChartData<String, Number>> dataPoints = new ArrayList<>();

        for (Website website : websites) {
            dataPoints.add(new ChartData<>(website.getWebsiteName(), website.getWebsiteClicks()));
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Website Clicks");
        for (ChartData<String, Number> data : dataPoints) {
            XYChart.Data<String, Number> chartData = new XYChart.Data<>(data.getX(), data.getY());
            series.getData().add(chartData);
        }

        totalClicksChart.getData().clear();
        totalClicksChart.getData().add(series);
    }

    private void showBounceRateChart() {
        List<Website> websites = websiteRepository.findAll();
        List<ChartData<String, Number>> dataPoints = new ArrayList<>();

        for (Website website : websites) {
            dataPoints.add(new ChartData<>(website.getWebsiteName(), website.getBounceRate()));
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bounce Rate");

        for (ChartData<String, Number> data : dataPoints) {
            XYChart.Data<String, Number> chartData = new XYChart.Data<>(data.getX(), data.getY());
            series.getData().add(chartData);
        }

        bounceRateChart.getData().clear();
        bounceRateChart.getData().add(series);
    }

}
