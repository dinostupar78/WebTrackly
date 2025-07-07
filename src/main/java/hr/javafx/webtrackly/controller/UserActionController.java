package hr.javafx.webtrackly.controller;
import hr.javafx.webtrackly.app.db.UserActionDbRepository1;
import hr.javafx.webtrackly.app.enums.BehaviourType;
import hr.javafx.webtrackly.app.exception.RepositoryException;
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
import java.util.*;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * Kontroler za upravljanje korisničkim akcijama u aplikaciji WebTrackly.
 * Ovaj kontroler omogućuje pregled, filtriranje, uređivanje i brisanje korisničkih akcija.
 */

public class UserActionController {
    @FXML
    private TextField actionTextFieldID;

    @FXML
    private TextField actionTextFieldUser;

    @FXML
    private TextField actionTextFieldAction;

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

    /**
     * Inicijalizira kontroler i postavlja sve potrebne postavke za prikaz korisničkih akcija.
     * Ova metoda se poziva prilikom učitavanja FXML datoteke.
     */

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

    /**
     * Metoda koja se poziva prilikom klika na gumb za filtriranje korisničkih akcija.
     * Ova metoda dohvaća sve korisničke akcije iz baze podataka i filtrira ih prema unosima u tekstualnim poljima.
     *
     * @param event Događaj klika na gumb.
     */

    @FXML
    private void openAddUserActionScreen(ActionEvent event) {
        ScreenChangeButtonUtil.openUserActionAddScreen(event);
    }

    /**
     * Metoda koja filtrira korisničke akcije na temelju unosa u tekstualnim poljima.
     * Prikazuje filtrirane rezultate u tablici i ažurira grafikone.
     */

    public void filterUserActions(){
        List<UserAction> initialUserActionList;
        try{
            initialUserActionList = userActionRepository.findAll();
        } catch (RepositoryException _) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Database is offline. Please check your connection.");
            alert.showAndWait();
            return;
        }

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

        String userAction = actionTextFieldAction.getText();
        if(!(userAction.isEmpty())){
            initialUserActionList = initialUserActionList.stream()
                    .filter(action -> action.getAction().name().contains(userAction))
                    .toList();
        }

        ObservableList<UserAction> userActionObservableList = observableArrayList(initialUserActionList);

        SortedList<UserAction> sortedList = new SortedList<>(userActionObservableList);

        sortedList.comparatorProperty().bind(userActionTableView.comparatorProperty());

        userActionTableView.setItems(sortedList);

        showUserActionAreaChart(initialUserActionList);
        showUserActionBehaviourTypeChart(initialUserActionList);

    }

    /**
     * Metoda koja se poziva prilikom klika na gumb za osvježavanje korisničkih akcija.
     * Ova metoda ponovno dohvaća sve korisničke akcije iz baze podataka i prikazuje ih u tablici.
     */

    private void showUserActionAreaChart(List<UserAction> actions) {
        userActionAreaChart.getData().clear();
        userActionAreaChart.setLegendVisible(false);

        Map<LocalDateTime, Integer> countsByHour = new HashMap<>();
        for (UserAction ua : actions) {
            LocalDateTime hour = ua.getTimestamp().truncatedTo(ChronoUnit.HOURS);
            Integer newCount = Optional.ofNullable(countsByHour.get(hour))
                    .map(prev -> prev + 1)
                    .orElse(1);

            countsByHour.put(hour, newCount);
        }

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd HH:mm");

        CategoryAxis xAxis = (CategoryAxis) userActionAreaChart.getXAxis();
        xAxis.setTickLabelRotation(-45);

        countsByHour.keySet().stream()
                .sorted()
                .forEach(hour -> series.getData().add(new XYChart.Data<>( hour.format(fmt), countsByHour.get(hour))));


        userActionAreaChart.getData().add(series);
    }

    /**
     * Metoda koja prikazuje statistiku korisničkih akcija prema vrsti ponašanja.
     * Ova metoda ažurira PieChart s brojem akcija po vrsti ponašanja.
     *
     * @param actions Lista korisničkih akcija koje se koriste za izračun statistike.
     */

    private void showUserActionBehaviourTypeChart(List<UserAction> actions) {
        userActionBehaviourTypeChart.getData().clear();

        Map<BehaviourType, Integer> counts = new EnumMap<>(BehaviourType.class);
        for (UserAction ua : actions) {
            BehaviourType type = ua.getAction();
            Integer newCount = Optional.ofNullable(counts.get(type))
                    .map(prev -> prev + 1)
                    .orElse(1);
            counts.put(type, newCount);
        }

        counts.forEach((type, count) ->
                userActionBehaviourTypeChart.getData().add(new PieChart.Data(type.name(), count))
        );

        userActionBehaviourTypeChart.setLegendSide(Side.BOTTOM);
        userActionBehaviourTypeChart.setLabelsVisible(true);
    }

}
