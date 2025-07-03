package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.UserActionDbRepository1;
import hr.javafx.webtrackly.app.enums.BehaviourType;
import hr.javafx.webtrackly.app.generics.EditData;
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
import javafx.geometry.Side;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

public class UserActionController {
    @FXML
    private TextField actionTextFieldID;

    @FXML
    private TextField actionTextFieldUser;

    @FXML
    private DatePicker actionDatePickerTimestamp;

    @FXML
    private TableView<UserAction> userActionTableView;

    @FXML
    private TableColumn<UserAction, String> userActionTableColumnID;

    @FXML
    private TableColumn<UserAction, String> userActionTableColumnUser;

    @FXML
    private TableColumn<UserAction, String> userActionTableColumnAction;

    @FXML
    private TableColumn<UserAction, String> userActionTableColumnSession;

    @FXML
    private TableColumn<UserAction, String> userActionTableColumnTimestamp;

    @FXML
    private TableColumn<UserAction, String> userActionTableColumnDetails;

    @FXML
    private PieChart userActionBehaviourTypeChart;

    @FXML
    private AreaChart<String, Number> userActionAreaChart;

    @FXML
    private Button deleteUserAction;

    private UserActionDbRepository1<UserAction> userActionRepository = new UserActionDbRepository1<>();

    public void initialize() {
        userActionTableColumnID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        userActionTableColumnUser.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getUser().getUsername()))
        );

        userActionTableColumnSession.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSession().getId()))
        );

        userActionTableColumnAction.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAction()))
        );

        userActionTableColumnTimestamp.setCellValueFactory(cellData ->
                new SimpleStringProperty(DateFormatterUtil.formatLocalDateTime(cellData.getValue().getTimestamp()))
        );

        userActionTableColumnDetails.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDetails()))
        );

        userActionTableView.getSortOrder().add(userActionTableColumnID);

        RowDeletion1Util.addUserActionRowDeletionHandler(userActionTableView);

        deleteUserAction.setOnAction(event -> RowDeletion1Util.deleteUserActionWithConfirmation(userActionTableView));

        RowEditUtil<UserAction> rowEditUtil = new RowEditUtil<>();
        rowEditUtil.addRowEditHandler(userActionTableView, selectedAction -> {
            EditData<UserAction> container = new EditData<>(selectedAction);
            ScreenChangeButtonUtil.openUserActionEditScreen(container.getData());
        });

    }


    @FXML
    private void openAddUserActionScreen(ActionEvent event) {
        ScreenChangeButtonUtil.openUserActionAddScreen(event);
    }

    public void filterUserActions(){

        List<UserAction> initialUserActionList = userActionRepository.findAll();

        String userActionID = actionTextFieldID.getText();
        if(!(userActionID.isEmpty())){
            initialUserActionList = initialUserActionList.stream()
                    .filter(action -> action.getId().toString().contains(userActionID))
                    .toList();
        }

        String userActionUsername = actionTextFieldUser.getText();
        if(!(userActionUsername.isEmpty())){
            initialUserActionList = initialUserActionList.stream()
                    .filter(action -> action.getUser().getUsername().contains(userActionUsername))
                    .toList();
        }

        if (actionDatePickerTimestamp.getValue() != null) {
            String userActionTimestamp = actionDatePickerTimestamp.getValue()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE);
            initialUserActionList = initialUserActionList.stream()
                    .filter(action -> action.getTimestamp().toString().contains(userActionTimestamp))
                    .toList();
        }

        ObservableList<UserAction> userActionObservableList = observableArrayList(initialUserActionList);

        SortedList<UserAction> sortedList = new SortedList<>(userActionObservableList);

        sortedList.comparatorProperty().bind(userActionTableView.comparatorProperty());

        userActionTableView.setItems(sortedList);

        showUserActionAreaChart(initialUserActionList);
        showUserActionBehaviourTypeChart(initialUserActionList);

    }

    private void showUserActionAreaChart(List<UserAction> actions) {
        userActionAreaChart.getData().clear();
        userActionAreaChart.setLegendVisible(false);

        Map<LocalDateTime, Long> countsByHour = actions.stream()
                .map(ua -> ua.getTimestamp().truncatedTo(ChronoUnit.HOURS))
                .collect(Collectors.groupingBy(h -> h, Collectors.counting()));

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd HH:mm");

        CategoryAxis xAxis = (CategoryAxis) userActionAreaChart.getXAxis();
        xAxis.setTickLabelRotation(-45);

        countsByHour.keySet().stream().sorted()
                .forEach(hour -> {
                    series.getData().add(
                            new XYChart.Data<>( hour.format(fmt), countsByHour.get(hour) )
                    );
                });

        userActionAreaChart.getData().add(series);
    }

    private void showUserActionBehaviourTypeChart(List<UserAction> actions) {
        userActionBehaviourTypeChart.getData().clear();

        Map<BehaviourType, Long> byType = actions.stream()
                .collect(Collectors.groupingBy(UserAction::getAction, Collectors.counting()));

        byType.forEach((type,count) ->
                userActionBehaviourTypeChart.getData()
                        .add(new PieChart.Data(type.name(), count))
        );

        userActionBehaviourTypeChart.setLegendSide(Side.BOTTOM);
        userActionBehaviourTypeChart.setLabelsVisible(true);
    }

}
