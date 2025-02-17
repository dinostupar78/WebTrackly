package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.db.SessionDbRepository;
import hr.javafx.webtrackly.app.db.UserActionDbRepository;
import hr.javafx.webtrackly.app.enums.BehaviorType;
import hr.javafx.webtrackly.app.generics.EditContainer;
import hr.javafx.webtrackly.app.model.Session;
import hr.javafx.webtrackly.app.model.UserAction;
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

import java.time.format.DateTimeFormatter;
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
    private TableColumn<UserAction, String> userActionTableColumnTimestamp;

    @FXML
    private TableColumn<UserAction, String> userActionTableColumnDetails;

    @FXML
    private PieChart userActionBehaviourTypeChart;

    @FXML
    private AreaChart<String, Number> userActionAreaChart;

    @FXML
    private Button deleteUserAction;

    private UserActionDbRepository<UserAction> userActionRepository = new UserActionDbRepository<>();
    private SessionDbRepository<Session> sessionRepository = new SessionDbRepository<>();

    public void initialize() {
        userActionTableColumnID.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        userActionTableColumnUser.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getUser().getUsername()))
        );

        userActionTableColumnAction.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAction()))
        );

        userActionTableColumnTimestamp.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getTimestamp()))
        );

        userActionTableColumnDetails.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDetails()))
        );

        userActionTableView.getSortOrder().add(userActionTableColumnID);

        RowDeletion1Util.addUserActionRowDeletionHandler(userActionTableView);

        deleteUserAction.setOnAction(event -> RowDeletion1Util.deleteUserActionWithConfirmation(userActionTableView));

        RowEditUtil<UserAction> rowEditUtil = new RowEditUtil<>();
        rowEditUtil.addRowEditHandler(userActionTableView, selectedAction -> {
            EditContainer<UserAction> container = new EditContainer<>(selectedAction);
            ScreenChangeButtonUtil.openUserActionEditScreen(container.getData());
        });
    }


    @FXML
    private void openAddUserActionScreen(ActionEvent event) {
        ScreenChangeButtonUtil.openUserActionAddScreen(event);
    }

    public void filterUserActions(){
        showUserActionBehaviourTypeChart();
        showUserActionAreaChart();

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
    }

    private void showUserActionBehaviourTypeChart() {
        userActionBehaviourTypeChart.getData().clear();

        List<UserAction> actions = userActionRepository.findAll();

        Map<BehaviorType, Long> actionCounts = actions.stream()
                .collect(Collectors.groupingBy(UserAction::getAction, Collectors.counting()));

        actionCounts.forEach((action, count) ->
                userActionBehaviourTypeChart.getData().add(new PieChart.Data(action.toString(), count))
        );

        userActionBehaviourTypeChart.setLegendSide(Side.BOTTOM);
        userActionBehaviourTypeChart.setLabelsVisible(true);
        userActionBehaviourTypeChart.layout();
    }

    private void showUserActionAreaChart() {
        // Clear old data before adding new search results
        userActionAreaChart.getData().clear();
        userActionAreaChart.layout(); // Force chart to refresh

        List<UserAction> actions = userActionRepository.findAll();

        // Group actions by date (YYYY-MM-DD) instead of full timestamp
        Map<String, Long> actionsByDate = actions.stream()
                .collect(Collectors.groupingBy(
                        action -> action.getTimestamp().toLocalDate().toString(), // Converts to date only
                        Collectors.counting()
                ));

        // Create new series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("User Actions Over Time");

        // Sort and add data points
        actionsByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Ensures chronological order
                .forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));

        userActionAreaChart.getData().add(series);

        // Fix X-Axis formatting issues
        CategoryAxis xAxis = (CategoryAxis) userActionAreaChart.getXAxis();
        xAxis.getCategories().clear(); // Remove old labels
        xAxis.setAutoRanging(true); // Ensure the X-axis adjusts to new data
        xAxis.setTickLabelRotation(45); // Rotate for readability
    }









}
