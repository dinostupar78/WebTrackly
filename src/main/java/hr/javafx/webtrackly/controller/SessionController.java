package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.SessionDbRepository1;
import hr.javafx.webtrackly.app.enums.DeviceType;
import hr.javafx.webtrackly.app.generics.ChartData;
import hr.javafx.webtrackly.app.generics.EditData;
import hr.javafx.webtrackly.app.model.Session;
import hr.javafx.webtrackly.utils.DateFormatterUtil;
import hr.javafx.webtrackly.utils.RowDeletion1Util;
import hr.javafx.webtrackly.utils.RowEditUtil;
import hr.javafx.webtrackly.utils.ScreenChangeButtonUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static javafx.collections.FXCollections.observableArrayList;

public class SessionController {
    @FXML
    private TextField sessionTextFieldID;

    @FXML
    private TextField sessionTextFieldWebsite;

    @FXML
    private TextField sessionTextFieldUser;

    @FXML
    private TableView<Session> sessionTableView;

    @FXML
    private TableColumn<Session, String> sessionColumnID;

    @FXML
    private TableColumn<Session, String> sessionColumnWebsite;

    @FXML
    private TableColumn<Session, String> sessionColumnUser;

    @FXML
    private TableColumn<Session, String> sessionColumnStartTime;

    @FXML
    private TableColumn<Session, String> sessionColumnEndTime;

    @FXML
    private TableColumn<Session, String> sessionColumnDuration;

    @FXML
    private TableColumn<Session, String> sessionColumnDevice;

    @FXML
    private Button deleteSession;

    @FXML
    private LineChart<String, Number> sessionActivityLineChart;

    @FXML
    private PieChart sessionDeviceDistributionPieChart;

    private SessionDbRepository1<Session> sessionRepository = new SessionDbRepository1<>();

    @FXML
    private void openAddSessionScreen(ActionEvent event) {
        ScreenChangeButtonUtil.openSessionAddScreen(event);
    }

    public void initialize(){
        sessionColumnID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        sessionColumnWebsite.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getWebsite().getWebsiteName()))
        );

        sessionColumnUser.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getUser().getUsername()))
        );

        sessionColumnStartTime.setCellValueFactory(cellData ->
                new SimpleStringProperty(DateFormatterUtil.formatLocalDateTime(cellData.getValue().getStartTime()))
        );

        sessionColumnEndTime.setCellValueFactory(cellData ->
                new SimpleStringProperty(DateFormatterUtil.formatLocalDateTime(cellData.getValue().getEndTime()))
        );

        sessionColumnDuration.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getSessionDurationMinutes()
                                .map(duration -> duration + " minutes")
                                .orElse("N/A")
                )
        );

        sessionColumnDevice.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDeviceType()))
        );

        sessionTableView.getSortOrder().add(sessionColumnID);

        RowDeletion1Util.addSessionDeletionHandler(sessionTableView);
        deleteSession.setOnAction(event -> RowDeletion1Util.deleteSessionWithConfirmation(sessionTableView));

        RowEditUtil<Session> rowEditUtil = new RowEditUtil<>();
        rowEditUtil.addRowEditHandler(sessionTableView, selectedSession -> {
            EditData<Session> container = new EditData<>(selectedSession);
            ScreenChangeButtonUtil.openSessionEditScreen(container.getData());
        });
    }

    public void filterSessions(){



        List<Session> initialSessionList = sessionRepository.findAll();

        String sessionID = sessionTextFieldID.getText();
        if(!(sessionID.isEmpty())){
            initialSessionList = initialSessionList.stream()
                    .filter(session -> session.getId().toString().contains(sessionID))
                    .toList();
        }

        String websiteName = sessionTextFieldWebsite.getText();
        if(!(websiteName.isEmpty())){
            initialSessionList = initialSessionList.stream()
                    .filter(session -> session.getWebsite().getWebsiteName().contains(websiteName))
                    .toList();
        }

        String userName = sessionTextFieldUser.getText();
        if(!(userName.isEmpty())){
            initialSessionList = initialSessionList.stream()
                    .filter(session -> session.getUser().getUsername().contains(userName))
                    .toList();
        }

        ObservableList<Session> sessionObservableList = observableArrayList(initialSessionList);

        SortedList<Session> sortedList = new SortedList<>(sessionObservableList);

        sortedList.comparatorProperty().bind(sessionTableView.comparatorProperty());

        sessionTableView.setItems(sortedList);

        showDeviceDistributionPieChart(initialSessionList);
        showSessionActivityLineChart(initialSessionList);

    }

    private void showSessionActivityLineChart(List<Session> sessions) {
        sessionActivityLineChart.getData().clear();
        sessionActivityLineChart.setLegendVisible(false);

        Map<LocalDateTime, Integer> countsByHour = new HashMap<>();
        for (Session s : sessions) {
            LocalDateTime hour = s.getEndTime().truncatedTo(ChronoUnit.HOURS);
            Integer newCount = Optional.ofNullable(countsByHour.get(hour))
                    .map(prev -> prev + 1)
                    .orElse(1);

            countsByHour.put(hour, newCount);
        }

        List<LocalDateTime> sortedHours = new ArrayList<>(countsByHour.keySet());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd HH:mm");

        for (LocalDateTime hour : sortedHours) {
            String label = hour.format(fmt);
            Integer count = countsByHour.get(hour);
            series.getData().add(new XYChart.Data<>(label, count));
        }

        sessionActivityLineChart.getData().add(series);
        CategoryAxis xAxis = (CategoryAxis)sessionActivityLineChart.getXAxis();
        xAxis.setTickLabelRotation(-45);
    }

    private void showDeviceDistributionPieChart(List<Session> sessions) {
        sessionDeviceDistributionPieChart.getData().clear();

        Map<DeviceType, Integer> deviceCounts = new EnumMap<>(DeviceType.class);
        for (Session s : sessions) {
            DeviceType dt = s.getDeviceType();
            Integer newCount = Optional.ofNullable(deviceCounts.get(dt))
                    .map(prev -> prev + 1)
                    .orElse(1);
            deviceCounts.put(dt, newCount);
        }

        List<ChartData<String, Integer>> chartData = deviceCounts.entrySet().stream()
                .map(e -> new ChartData<>(e.getKey().name(), e.getValue()))
                .toList();

        for (ChartData<String, Integer> cd : chartData) {
            sessionDeviceDistributionPieChart.getData()
                    .add(new PieChart.Data(cd.getX(), cd.getY()));
        }

        sessionDeviceDistributionPieChart.setLegendSide(Side.BOTTOM);
        sessionDeviceDistributionPieChart.setLabelsVisible(true);
        sessionDeviceDistributionPieChart.layout();
    }
}
