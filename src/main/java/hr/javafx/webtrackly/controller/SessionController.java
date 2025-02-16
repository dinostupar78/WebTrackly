package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.SessionDbRepository;
import hr.javafx.webtrackly.app.model.Session;
import hr.javafx.webtrackly.utils.ScreenChangeButtonUtil;
import hr.javafx.webtrackly.utils.ShowAlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private LineChart<String, Number> sessionActivityLineChart;

    @FXML
    private PieChart sessionDeviceDistributionPieChart;

    private SessionDbRepository<Session> sessionRepository = new SessionDbRepository<>();

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
                new SimpleStringProperty(String.valueOf(cellData.getValue().getStartTime()))
        );

        sessionColumnEndTime.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getEndTime()))
        );

        sessionColumnDuration.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSessionDuration()))
        );

        sessionColumnDevice.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDeviceType()))
        );

        sessionTableView.getSortOrder().add(sessionColumnID);
    }

    public void filterSessions(){
        showSessionActivityLineChart();

        showDeviceDistributionPieChart();

        List<Session> initialSessionList = sessionRepository.findAll();

        String sessionID = sessionTextFieldID.getText();
        if(!(sessionID.isEmpty())){
            initialSessionList = initialSessionList.stream()
                    .filter(session -> session.getId().toString().contains(sessionID))
                    .toList();
        }

        if (sessionDatePickerStartDate.getValue() != null) {
            String sessionStartDate = sessionDatePickerStartDate.getValue()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE);
            initialSessionList = initialSessionList.stream()
                    .filter(session -> session.getStartTime().toString().contains(sessionStartDate))
                    .toList();
        }

        if (sessionDatePickerEndDate.getValue() != null) {
            String sessionEndDate = sessionDatePickerEndDate.getValue()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE);
            initialSessionList = initialSessionList.stream()
                    .filter(session -> session.getStartTime().toString().contains(sessionEndDate))
                    .toList();
        }

        ObservableList<Session> sessionObservableList = observableArrayList(initialSessionList);

        SortedList<Session> sortedList = new SortedList<>(sessionObservableList);

        sortedList.comparatorProperty().bind(sessionTableView.comparatorProperty());

        sessionTableView.setItems(sortedList);

    }

    private void showSessionActivityLineChart() {
        sessionActivityLineChart.getData().clear();

        String sessionIDTextField = sessionTextFieldID.getText();

        List<Session> filteredSessions = sessionRepository.findAll().stream()
                .filter(session -> session.getId().toString().contains(sessionIDTextField))
                .toList();

        if (filteredSessions.isEmpty()) {
            ShowAlertUtil.showAlert("Error", "No sessions found matching the given ID", Alert.AlertType.ERROR);
        }

        Map<String, Long> sessionCountByDate = new HashMap<>();

        filteredSessions.forEach(session -> {
            String sessionDate = session.getStartTime().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            sessionCountByDate.merge(sessionDate, 1L, Long::sum);
        });

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Session Count");

        sessionCountByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));

        sessionActivityLineChart.getData().add(series);

    }

    private void showDeviceDistributionPieChart() {

    }




}
