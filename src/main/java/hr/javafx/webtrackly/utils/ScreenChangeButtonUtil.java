package hr.javafx.webtrackly.utils;

import hr.javafx.webtrackly.app.model.Session;
import hr.javafx.webtrackly.app.model.TrafficRecord;
import hr.javafx.webtrackly.app.model.UserAction;
import hr.javafx.webtrackly.controller.SessionEditController;
import hr.javafx.webtrackly.controller.TrafficRecordEditController;
import hr.javafx.webtrackly.controller.UserActionEditController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenChangeButtonUtil {
    private ScreenChangeButtonUtil(){}

    public static void openUserActionAddScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/userActionAddPanel.fxml"));
            Scene scene = new Scene(loader.load());

            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add User Action");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openUserActionEditScreen(UserAction userAction) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/userActionEditPanel.fxml"));
            Scene scene = new Scene(loader.load());

            UserActionEditController controller = loader.getController();
            controller.setUserActionData(userAction);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit User Action");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openSessionAddScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/sessionAddPanel.fxml"));
            Scene scene = new Scene(loader.load());

            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Session");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openSessionEditScreen(Session session) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/sessionEditPanel.fxml"));
            Scene scene = new Scene(loader.load());

            SessionEditController controller = loader.getController();
            controller.setSessionData(session);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Session");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openTrafficRecordAddScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/trafficRecordAddPanel.fxml"));
            Scene scene = new Scene(loader.load());

            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Traffic Record");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openTrafficRecordEditScreen(TrafficRecord trafficRecord) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/trafficRecordEditPanel.fxml"));
            Scene scene = new Scene(loader.load());

            TrafficRecordEditController controller = loader.getController();
            controller.setTrafficRecordData(trafficRecord);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Traffic Record");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openWebsiteAddScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/websiteAddPanel.fxml"));
            Scene scene = new Scene(loader.load());

            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Session");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
