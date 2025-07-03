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
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

public class SessionController {
    @FXML
    private TextField sessionTextFieldID;

    @FXML
    private DatePicker sessionDatePickerStartDate;

    @FXML
    private DatePicker sessionDatePickerEndDate;

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

        if (sessionDatePickerStartDate.getValue() != null) {
            LocalDate filterStartDate = sessionDatePickerStartDate.getValue();
            initialSessionList = initialSessionList.stream()
                    .filter(session -> session.getStartTime().toLocalDate().equals(filterStartDate))
                    .toList();
        }

        if (sessionDatePickerEndDate.getValue() != null) {
            LocalDate filterEndDate = sessionDatePickerEndDate.getValue();
            initialSessionList = initialSessionList.stream()
                    .filter(session -> session.getEndTime().toLocalDate().equals(filterEndDate))
                    .toList();
        }

        ObservableList<Session> sessionObservableList = observableArrayList(initialSessionList);

        SortedList<Session> sortedList = new SortedList<>(sessionObservableList);

        sortedList.comparatorProperty().bind(sessionTableView.comparatorProperty());

        sessionTableView.setItems(sortedList);

        showDeviceDistributionPieChart();
        showSessionActivityLineChart(initialSessionList);

    }

    private void showSessionActivityLineChart(List<Session> sessions) {
        sessionActivityLineChart.getData().clear();
        sessionActivityLineChart.setLegendVisible(false);

        Map<LocalDateTime, Long> countsByHour = sessions.stream()
                .map(s -> s.getStartTime().truncatedTo(ChronoUnit.HOURS))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        List<ChartData<String, Long>> chartData = countsByHour.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new ChartData<>(e.getKey().format(fmt), e.getValue()))
                .toList();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (ChartData<String, Long> data : chartData) {
            series.getData().add(new XYChart.Data<>(data.getX(), data.getY()));
        }

        sessionActivityLineChart.getData().add(series);

        CategoryAxis xAxis = (CategoryAxis)sessionActivityLineChart.getXAxis();
        xAxis.setTickLabelRotation(-45);
    }

    private void showDeviceDistributionPieChart() {
        sessionDeviceDistributionPieChart.getData().clear();

        List<Session> sessions = sessionRepository.findAll();

        Map<DeviceType, Long> countDevices = sessions.stream()
                .collect(Collectors.groupingBy(Session::getDeviceType, Collectors.counting()));

        countDevices.forEach((device, count) ->
                sessionDeviceDistributionPieChart.getData().add(new PieChart.Data(device.name(), count))
        );

        sessionDeviceDistributionPieChart.setLegendSide(Side.BOTTOM);
        sessionDeviceDistributionPieChart.setLabelsVisible(true);
        sessionDeviceDistributionPieChart.layout();
    }
}
