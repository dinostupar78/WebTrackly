package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.model.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class TrafficRecordController{

    @FXML
    private TextField websiteSearchField;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private PieChart pieChartSources;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private TableView<Session> sessionTableView;

    @FXML
    private TableColumn<Session, String> sessionColumnID;

    @FXML
    private TableColumn<Session, String> sessionColumnUser;

    @FXML
    private TableColumn<Session, String> sessionColumnDevice;

    @FXML
    private TableColumn<Session, String> sessionColumnDuration;

    @FXML
    private TableColumn<Session, String> sessionColumnActive;


    public void initialize() {
        sessionColumnID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        sessionColumnUser.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getUser().getUsername()))
        );

        sessionColumnDevice.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDeviceType()))
        );

        sessionColumnDuration.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSessionDuration()))
        );

        sessionColumnActive.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getActive()))
        );

    }



}
