package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.TrafficRecordDbRepository;
import hr.javafx.webtrackly.app.generics.EditContainer;
import hr.javafx.webtrackly.app.model.TrafficRecord;
import hr.javafx.webtrackly.utils.RowDeletionUtil;
import hr.javafx.webtrackly.utils.RowEditUtil;
import hr.javafx.webtrackly.utils.ScreenChangeButtonUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

public class TrafficRecordController {
    @FXML
    private TextField trafficTextFieldID;

    @FXML
    private TextField trafficTextFieldWebsite;

    @FXML
    private DatePicker trafficDatePickerTime;

    @FXML
    private TableView<TrafficRecord> trafficRecordTableView;

    @FXML
    private TableColumn<TrafficRecord, String> trafficColumnID;

    @FXML
    private TableColumn<TrafficRecord, String> trafficColumnWebsite;

    @FXML
    private TableColumn<TrafficRecord, String> trafficColumnTime;

    @FXML
    private TableColumn<TrafficRecord, String> trafficColumnViews;

    @FXML
    private TableColumn<TrafficRecord, String> trafficColumnBounceRate;

    @FXML
    private TableColumn<TrafficRecord, String> trafficColumnSessions;

    @FXML
    private Button deleteTrafficRecord;

    private TrafficRecordDbRepository<TrafficRecord> trafficRecordRepository = new TrafficRecordDbRepository<>();

    public void initialize(){
        trafficColumnID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        trafficColumnWebsite.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getWebsite().getWebsiteName()))
        );

        trafficColumnTime.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getTimeOfVisit()))
        );

        trafficColumnViews.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPageViews()))
        );

        trafficColumnBounceRate.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getBounceRate()))
        );

        trafficColumnSessions.setCellValueFactory(cellData -> {
            String trafficSessions = cellData.getValue().getSessions().stream()
                    .map(session -> session.getActive().toString().toUpperCase())
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(trafficSessions);
        });

        RowDeletionUtil.addTrafficRecordDeletionHandler(trafficRecordTableView);

        deleteTrafficRecord.setOnAction(event -> RowDeletionUtil.deleteTrafficRecordWithConfirmation(trafficRecordTableView));

        RowEditUtil<TrafficRecord> rowEditUtil = new RowEditUtil<>();
        rowEditUtil.addRowEditHandler(trafficRecordTableView, selectedTrafficRecord -> {
            EditContainer<TrafficRecord> container = new EditContainer<>(selectedTrafficRecord);
            ScreenChangeButtonUtil.openTrafficRecordEditScreen(container.getData());
        });
    }

    @FXML
    private void openAddTrafficRecordScreen(ActionEvent event) {
        ScreenChangeButtonUtil.openTrafficRecordAddScreen(event);
    }

    public void filterTrafficRecords(){
        List<TrafficRecord> initialTrafficRecordList = trafficRecordRepository.findAll();

        String trafficRecordID = trafficTextFieldID.getText();
        if(!(trafficRecordID.isEmpty())){
            initialTrafficRecordList = initialTrafficRecordList.stream()
                    .filter(traffic -> traffic.getId().toString().contains(trafficRecordID))
                    .toList();
        }

        String trafficRecordWebsite = trafficTextFieldWebsite.getText();
        if(!(trafficRecordWebsite.isEmpty())){
            initialTrafficRecordList = initialTrafficRecordList.stream()
                    .filter(traffic -> traffic.getWebsite().getWebsiteName().toString().contains(trafficRecordWebsite))
                    .toList();
        }

        if (trafficDatePickerTime.getValue() != null) {
            String trafficRecordVisit = trafficDatePickerTime.getValue()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE);
            initialTrafficRecordList = initialTrafficRecordList.stream()
                    .filter(traffic -> traffic.getTimeOfVisit().toString().contains(trafficRecordVisit))
                    .toList();
        }

        ObservableList<TrafficRecord> trafficRecordObservableList = observableArrayList(initialTrafficRecordList);

        SortedList<TrafficRecord> sortedList = new SortedList<>(trafficRecordObservableList);

        sortedList.comparatorProperty().bind(trafficRecordTableView.comparatorProperty());

        trafficRecordTableView.setItems(sortedList);

    }
}
