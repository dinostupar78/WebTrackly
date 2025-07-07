package hr.javafx.webtrackly.utils;
import hr.javafx.webtrackly.app.db.*;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.Session;
import hr.javafx.webtrackly.app.model.TrafficRecord;
import hr.javafx.webtrackly.app.model.User;
import hr.javafx.webtrackly.app.model.UserAction;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import java.util.Optional;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * Util klasa za upravljanje brisanjem redaka u tablicama aplikacije WebTrackly.
 * Ova klasa pruža metode za dodavanje rukovatelja događajima brisanja redaka
 * i potvrdu brisanja za različite entitete poput korisnika, korisničkih akcija,
 * sesija i zapisa prometa.
 */

public class RowDeletion1Util {
    private RowDeletion1Util() {}

    private static final String ERROR_STRING = "Error";
    private static final String SUCCESS_STRING = "Success";

    private static final UserDbRepository2<User> userRepository = new UserDbRepository2<>();
    private static final UserActionDbRepository2<UserAction> userActionRepository = new UserActionDbRepository2<>();
    private static final SessionDbRepository2<Session> sessionRepository = new SessionDbRepository2<>();
    private static final TrafficRecordDbRepository2<TrafficRecord> trafficRecordRepository = new TrafficRecordDbRepository2<>();

    /**
     * Dodaje rukovatelja događajem za brisanje redaka u tablici korisnika.
     * Kada se klikne na redak, korisnik će biti upitan za potvrdu brisanja.
     *
     * @param tableView Tablica korisnika
     */

    public static void addUserRowDeletionHandler(TableView<User> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 0) {
                    deleteUserWithConfirmation(tableView, row.getItem());
                }
            });
            return row;
        });
    }

    public static void deleteUserWithConfirmation(TableView<User> tableView) {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            ShowAlertUtil.showAlert(ERROR_STRING, "No user selected for deletion!", Alert.AlertType.ERROR);
            return;
        }

        deleteUserWithConfirmation(tableView, selectedUser);
    }

    /**
     * Briše korisnika nakon potvrde.
     * Ako je brisanje uspješno, redak se uklanja iz tablice.
     *
     * @param tableView Tablica korisnika
     * @param user      Korisnik koji se briše
     */

    private static void deleteUserWithConfirmation(TableView<User> tableView, User user) {
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultDelete();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                userRepository.delete(user.getId());

                ObservableList<User> users = observableArrayList(tableView.getItems());
                users.remove(user);
                tableView.setItems(users);

                ShowAlertUtil.showAlert(SUCCESS_STRING, "User deleted successfully.", Alert.AlertType.INFORMATION);
            } catch (RepositoryException e) {
                e.printStackTrace();
                ShowAlertUtil.showAlert(ERROR_STRING, "Failed to delete user: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Dodaje rukovatelja događajem za brisanje redaka u tablici korisničkih akcija.
     * Kada se klikne na redak, korisnik će biti upitan za potvrdu brisanja.
     *
     * @param tableView Tablica korisničkih akcija
     */

    public static void addUserActionRowDeletionHandler(TableView<UserAction> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<UserAction> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 0) {
                    deleteUserActionWithConfirmation(tableView, row.getItem());
                }
            });
            return row;
        });
    }

    /**
     * Briše korisničku akciju nakon potvrde.
     * Ako je brisanje uspješno, redak se uklanja iz tablice.
     *
     * @param tableView Tablica korisničkih akcija
     */

    public static void deleteUserActionWithConfirmation(TableView<UserAction> tableView) {
        UserAction selectedAction = tableView.getSelectionModel().getSelectedItem();
        if (selectedAction == null) {
            ShowAlertUtil.showAlert(ERROR_STRING, "No action selected for deletion!", Alert.AlertType.ERROR);
            return;
        }

        deleteUserActionWithConfirmation(tableView, selectedAction);
    }

    /**
     * Briše korisničku akciju nakon potvrde.
     * Ako je brisanje uspješno, redak se uklanja iz tablice.
     *
     * @param tableView Tablica korisničkih akcija
     * @param action    Korisnička akcija koja se briše
     */

    private static void deleteUserActionWithConfirmation(TableView<UserAction> tableView, UserAction action) {
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultDelete();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                userActionRepository.delete(action.getId());

                ObservableList<UserAction> actions = observableArrayList(tableView.getItems());
                actions.remove(action);
                tableView.setItems(actions);

                ShowAlertUtil.showAlert(SUCCESS_STRING, "Action deleted successfully.", Alert.AlertType.INFORMATION);
            } catch (RepositoryException e) {
                e.printStackTrace();
                ShowAlertUtil.showAlert(ERROR_STRING, "Failed to delete action: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Dodaje rukovatelja događajem za brisanje redaka u tablici sesija.
     * Kada se klikne na redak, korisnik će biti upitan za potvrdu brisanja.
     *
     * @param tableView Tablica sesija
     */

    public static void addSessionDeletionHandler(TableView<Session> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<Session> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 0) {
                    deleteSessionWithConfirmation(tableView, row.getItem());
                }
            });
            return row;
        });
    }

    /**
     * Briše sesiju nakon potvrde.
     * Ako je brisanje uspješno, redak se uklanja iz tablice.
     *
     * @param tableView Tablica sesija
     */

    public static void deleteSessionWithConfirmation(TableView<Session> tableView) {
        Session selectedSession = tableView.getSelectionModel().getSelectedItem();
        if (selectedSession == null) {
            ShowAlertUtil.showAlert(ERROR_STRING, "No session selected for deletion!", Alert.AlertType.ERROR);
            return;
        }

        deleteSessionWithConfirmation(tableView, selectedSession);
    }

    /**
     * Briše sesiju nakon potvrde.
     * Ako je brisanje uspješno, redak se uklanja iz tablice.
     *
     * @param tableView Tablica sesija
     * @param session   Sesija koja se briše
     */

    private static void deleteSessionWithConfirmation(TableView<Session> tableView, Session session) {
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultDelete();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                sessionRepository.delete(session.getId());

                ObservableList<Session> sessions = observableArrayList(tableView.getItems());
                sessions.remove(session);
                tableView.setItems(sessions);

                ShowAlertUtil.showAlert(SUCCESS_STRING, "Session deleted successfully.", Alert.AlertType.INFORMATION);
            } catch (RepositoryException e) {
                e.printStackTrace();
                ShowAlertUtil.showAlert(ERROR_STRING, "Failed to delete session: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Dodaje rukovatelja događajem za brisanje redaka u tablici zapisa prometa.
     * Kada se klikne na redak, korisnik će biti upitan za potvrdu brisanja.
     *
     * @param tableView Tablica zapisa prometa
     */

    public static void addTrafficRecordDeletionHandler(TableView<TrafficRecord> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<TrafficRecord> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 0) {
                    deleteTrafficRecordWithConfirmation(tableView, row.getItem());
                }
            });
            return row;
        });
    }

    /**
     * Briše zapis prometa nakon potvrde.
     * Ako je brisanje uspješno, redak se uklanja iz tablice.
     *
     * @param tableView Tablica zapisa prometa
     */

    public static void deleteTrafficRecordWithConfirmation(TableView<TrafficRecord> tableView) {
        TrafficRecord selectedTrafficRecord = tableView.getSelectionModel().getSelectedItem();
        if (selectedTrafficRecord == null) {
            ShowAlertUtil.showAlert(ERROR_STRING, "No traffic record selected for deletion!", Alert.AlertType.ERROR);
            return;
        }

        deleteTrafficRecordWithConfirmation(tableView, selectedTrafficRecord);
    }

    /**
     * Briše zapis prometa nakon potvrde.
     * Ako je brisanje uspješno, redak se uklanja iz tablice.
     *
     * @param tableView Tablica zapisa prometa
     * @param trafficRecord Zapis prometa koji se briše
     */

    private static void deleteTrafficRecordWithConfirmation(TableView<TrafficRecord> tableView, TrafficRecord trafficRecord) {
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultDelete();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                trafficRecordRepository.delete(trafficRecord.getId());

                ObservableList<TrafficRecord> trafficRecords = observableArrayList(tableView.getItems());
                trafficRecords.remove(trafficRecord);
                tableView.setItems(trafficRecords);

                ShowAlertUtil.showAlert(SUCCESS_STRING, "Traffic record deleted successfully.", Alert.AlertType.INFORMATION);
            } catch (RepositoryException e) {
                e.printStackTrace();
                ShowAlertUtil.showAlert(ERROR_STRING, "Failed to delete session: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

}
