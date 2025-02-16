package hr.javafx.webtrackly.utils;

import hr.javafx.webtrackly.app.db.SessionDbRepository;
import hr.javafx.webtrackly.app.db.TrafficRecordDbRepository;
import hr.javafx.webtrackly.app.db.UserActionDbRepository;
import hr.javafx.webtrackly.app.db.UserDbRepository;
import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
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

public class RowDeletionUtil {
    private RowDeletionUtil() {}

    private static final String ERROR_STRING = "Error";
    private static final String SUCCESS_STRING = "Success";

    private static final UserDbRepository<User> userRepository = new UserDbRepository<>();
    private static final UserActionDbRepository<UserAction> userActionRepository = new UserActionDbRepository<>();
    private static final SessionDbRepository<Session> sessionRepository = new SessionDbRepository<>();
    private static final TrafficRecordDbRepository<TrafficRecord> trafficRecordRepository = new TrafficRecordDbRepository<>();

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

    private static void deleteUserWithConfirmation(TableView<User> tableView, User user) {
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultDelete();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                userRepository.delete(user.getId());

                ObservableList<User> users = observableArrayList(tableView.getItems());
                users.remove(user);
                tableView.setItems(users);

                ShowAlertUtil.showAlert(SUCCESS_STRING, "User deleted successfully.", Alert.AlertType.INFORMATION);
            } catch (RepositoryAccessException e) {
                e.printStackTrace();
                ShowAlertUtil.showAlert(ERROR_STRING, "Failed to delete user: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

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

    public static void deleteUserActionWithConfirmation(TableView<UserAction> tableView) {
        UserAction selectedAction = tableView.getSelectionModel().getSelectedItem();
        if (selectedAction == null) {
            ShowAlertUtil.showAlert(ERROR_STRING, "No action selected for deletion!", Alert.AlertType.ERROR);
            return;
        }

        deleteUserActionWithConfirmation(tableView, selectedAction);
    }

    private static void deleteUserActionWithConfirmation(TableView<UserAction> tableView, UserAction action) {
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultDelete();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                userActionRepository.delete(action.getId());

                ObservableList<UserAction> actions = observableArrayList(tableView.getItems());
                actions.remove(action);
                tableView.setItems(actions);

                ShowAlertUtil.showAlert(SUCCESS_STRING, "Action deleted successfully.", Alert.AlertType.INFORMATION);
            } catch (RepositoryAccessException e) {
                e.printStackTrace();
                ShowAlertUtil.showAlert(ERROR_STRING, "Failed to delete action: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

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

    public static void deleteSessionWithConfirmation(TableView<Session> tableView) {
        Session selectedSession = tableView.getSelectionModel().getSelectedItem();
        if (selectedSession == null) {
            ShowAlertUtil.showAlert(ERROR_STRING, "No session selected for deletion!", Alert.AlertType.ERROR);
            return;
        }

        deleteSessionWithConfirmation(tableView, selectedSession);
    }

    private static void deleteSessionWithConfirmation(TableView<Session> tableView, Session session) {
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultDelete();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                sessionRepository.delete(session.getId());

                ObservableList<Session> sessions = observableArrayList(tableView.getItems());
                sessions.remove(session);
                tableView.setItems(sessions);

                ShowAlertUtil.showAlert(SUCCESS_STRING, "Session deleted successfully.", Alert.AlertType.INFORMATION);
            } catch (RepositoryAccessException e) {
                e.printStackTrace();
                ShowAlertUtil.showAlert(ERROR_STRING, "Failed to delete session: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

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

    public static void deleteTrafficRecordWithConfirmation(TableView<TrafficRecord> tableView) {
        TrafficRecord selectedTrafficRecord = tableView.getSelectionModel().getSelectedItem();
        if (selectedTrafficRecord == null) {
            ShowAlertUtil.showAlert(ERROR_STRING, "No traffic record selected for deletion!", Alert.AlertType.ERROR);
            return;
        }

        deleteTrafficRecordWithConfirmation(tableView, selectedTrafficRecord);
    }

    private static void deleteTrafficRecordWithConfirmation(TableView<TrafficRecord> tableView, TrafficRecord trafficRecord) {
        Optional<ButtonType> result = ShowAlertUtil.getAlertResultDelete();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                trafficRecordRepository.delete(trafficRecord.getId());

                ObservableList<TrafficRecord> trafficRecords = observableArrayList(tableView.getItems());
                trafficRecords.remove(trafficRecord);
                tableView.setItems(trafficRecords);

                ShowAlertUtil.showAlert(SUCCESS_STRING, "Traffic record deleted successfully.", Alert.AlertType.INFORMATION);
            } catch (RepositoryAccessException e) {
                e.printStackTrace();
                ShowAlertUtil.showAlert(ERROR_STRING, "Failed to delete session: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

}
