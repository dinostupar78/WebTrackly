package hr.javafx.webtrackly.controller;
import hr.javafx.webtrackly.app.db.TrafficRecordDbRepository1;
import hr.javafx.webtrackly.app.db.UserActionDbRepository1;
import hr.javafx.webtrackly.app.enums.BehaviourType;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.generics.EditData;
import hr.javafx.webtrackly.app.model.TrafficRecord;
import hr.javafx.webtrackly.app.model.UserAction;
import hr.javafx.webtrackly.utils.DateFormatterUtil;
import hr.javafx.webtrackly.utils.RowDeletion1Util;
import hr.javafx.webtrackly.utils.RowEditUtil;
import hr.javafx.webtrackly.utils.ScreenChangeButtonUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * Kontroler za upravljanje prometnim zapisima u aplikaciji WebTrackly.
 * Ovaj kontroler omogućuje pregled, filtriranje, uređivanje i brisanje prometnih zapisa.
 */

public class TrafficRecordController {
    @FXML
    private TextField trafficTextFieldID;

    @FXML
    private TextField trafficTextFieldWebsite;

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
    private LineChart<String, Number> pageViewsChart;

    @FXML
    private BarChart<String, Number> totalPageViewsChart;

    @FXML
    private Button deleteTrafficRecord;

    private TrafficRecordDbRepository1<TrafficRecord> trafficRecordRepository = new TrafficRecordDbRepository1<>();
    private UserActionDbRepository1<UserAction> userActionRepository = new UserActionDbRepository1<>();

/**
     * Inicijalizira kontroler i postavlja vrijednosti za stupce tablice prometnih zapisa.
     * Također dodaje rukovatelje događaja za brisanje i uređivanje redaka.
     */

    public void initialize(){
        trafficColumnID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        trafficColumnWebsite.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getWebsite().getWebsiteName()))
        );

        trafficColumnTime.setCellValueFactory(cellData ->
                new SimpleStringProperty(DateFormatterUtil.formatLocalDateTime(cellData.getValue().getTimeOfVisit()))
        );

        trafficColumnViews.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPageViews()))
        );

        trafficColumnBounceRate.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getBounceRate()))
        );

        RowDeletion1Util.addTrafficRecordDeletionHandler(trafficRecordTableView);

        deleteTrafficRecord.setOnAction(event -> RowDeletion1Util.deleteTrafficRecordWithConfirmation(trafficRecordTableView));

        RowEditUtil<TrafficRecord> rowEditUtil = new RowEditUtil<>();
        rowEditUtil.addRowEditHandler(trafficRecordTableView, selectedTrafficRecord -> {
            EditData<TrafficRecord> container = new EditData<>(selectedTrafficRecord);
            ScreenChangeButtonUtil.openTrafficRecordEditScreen(container.getData());
        });
    }

    /**
     * Metoda koja se poziva prilikom promjene teksta u polju za ID prometnog zapisa.
     * Pokreće filtriranje prometnih zapisa na temelju unesenog ID-a.
     */

    @FXML
    private void openAddTrafficRecordScreen(ActionEvent event) {
        ScreenChangeButtonUtil.openTrafficRecordAddScreen(event);
    }

    /**
     * Metoda koja se poziva prilikom promjene teksta u polju za naziv web stranice.
     * Pokreće filtriranje prometnih zapisa na temelju unesenog naziva web stranice.
     */

    public void filterTrafficRecords(){
        List<TrafficRecord> initialTrafficRecordList;
        try{
            initialTrafficRecordList = trafficRecordRepository.findAll();
        } catch (RepositoryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Database is offline. Please check your connection.");
            alert.showAndWait();
            return;
        }

        initialTrafficRecordList.forEach(traffic -> {
            LocalDateTime end   = traffic.getTimeOfVisit();
            LocalDateTime start = end.minusHours(24);

            List<UserAction> clicks = userActionRepository.findAll().stream()
                    .filter(ua -> ua.getAction() == BehaviourType.CLICK)
                    .filter(ua -> !ua.getTimestamp().isBefore(start) && !ua.getTimestamp().isAfter(end))
                    .filter(ua -> ua.getUser().getWebsiteId().equals(traffic.getWebsite().getId()))
                    .toList();

            traffic.setPageViews(clicks.size());

            Map<Long, Long> clicksPerUser = clicks.stream()
                    .collect(Collectors.groupingBy(ua -> ua.getUser().getId(),
                            Collectors.counting()
                    ));

            Long uniqueUsers = (long) clicksPerUser.size();

            BigDecimal pageViews = BigDecimal.valueOf(clicks.size());
            Long bounces = clicksPerUser.values().stream()
                    .filter(click -> click == 1)
                    .count();

            BigDecimal bounceRate;

            if (uniqueUsers > 0) {
                bounceRate = BigDecimal.valueOf((double) bounces / uniqueUsers * 100)
                        .setScale(2, RoundingMode.HALF_UP);
            } else {
                bounceRate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            }

            traffic.setBounceRate(bounceRate);
            traffic.setPageViews(pageViews.intValue());

        });

        String trafficRecordID = trafficTextFieldID.getText();
        if(!(trafficRecordID.isEmpty())){
            initialTrafficRecordList = initialTrafficRecordList.stream()
                    .filter(traffic -> traffic.getId().toString().contains(trafficRecordID))
                    .toList();
        }

        String trafficRecordWebsite = trafficTextFieldWebsite.getText();
        if(!(trafficRecordWebsite.isEmpty())){
            initialTrafficRecordList = initialTrafficRecordList.stream()
                    .filter(traffic -> traffic.getWebsite().getWebsiteName().contains(trafficRecordWebsite))
                    .toList();
        }

        ObservableList<TrafficRecord> trafficRecordObservableList = observableArrayList(initialTrafficRecordList);
        SortedList<TrafficRecord> sortedList = new SortedList<>(trafficRecordObservableList);
        sortedList.comparatorProperty().bind(trafficRecordTableView.comparatorProperty());
        trafficRecordTableView.setItems(sortedList);

        showPageViewsTrendChart(initialTrafficRecordList);
        showPageViewsBySiteChart(initialTrafficRecordList);

    }

    /**
     * Metoda koja se poziva prilikom promjene teksta u polju za naziv web stranice.
     * Pokreće filtriranje prometnih zapisa na temelju unesenog naziva web stranice.
     */

    private void showPageViewsTrendChart(List<TrafficRecord> data) {
        pageViewsChart.getData().clear();

        pageViewsChart.setLegendVisible(false);

        LocalDateTime last24hours = LocalDateTime.now().minusHours(200);
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM-dd HH:mm");

        List<TrafficRecord> recent = new ArrayList<>();
        for (TrafficRecord r : data) {
            if (!r.getTimeOfVisit().isBefore(last24hours)) {
                recent.add(r);
            }
        }

        recent.sort(Comparator.comparing(TrafficRecord::getTimeOfVisit));
        XYChart.Series<String,Number> series = new XYChart.Series<>();

        for (TrafficRecord trafficRecord : recent) {
            String label = trafficRecord.getTimeOfVisit().format(formatDate);
            series.getData().add(new XYChart.Data<>(label, trafficRecord.getPageViews()));
        }

        pageViewsChart.getData().add(series);
        CategoryAxis xAxis = (CategoryAxis) pageViewsChart.getXAxis();
        xAxis.setTickLabelRotation(-45);
    }

    /**
     * Metoda koja prikazuje ukupne prikaze stranica po web stranicama u obliku stupčastog grafikona.
     * Prikazuje samo podatke za zadnjih 200 sati.
     *
     * @param data Lista prometnih zapisa koji sadrže podatke o prikazima stranica.
     */

    private void showPageViewsBySiteChart(List<TrafficRecord> data) {
        totalPageViewsChart.getData().clear();

        LocalDateTime last24hours = LocalDateTime.now().minusHours(200);

        Map<String,Integer> viewsBySite = new HashMap<>();
        for (TrafficRecord trafficRecord : data) {
            if (!trafficRecord.getTimeOfVisit().isBefore(last24hours)) {
                String site = trafficRecord.getWebsite().getWebsiteName();
                viewsBySite.put(site,
                        viewsBySite.getOrDefault(site, 0) + trafficRecord.getPageViews()
                );
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Page-Views");

        viewsBySite.forEach((site, views) ->
                series.getData().add(new XYChart.Data<>(site, views))
        );

        totalPageViewsChart.getData().add(series);
    }

}





