package hr.javafx.webtrackly.utils;

import hr.javafx.webtrackly.app.model.*;
import hr.javafx.webtrackly.controller.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenChangeButtonUtil {
    private ScreenChangeButtonUtil(){}

    public static void openUserEditScreen(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/userEdit.fxml"));
            Scene scene = new Scene(loader.load());

            UserEditController controller = loader.getController();
            controller.setUser(user);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit User");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openUserActionAddScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/userActionAdd.fxml"));
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
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/userActionEdit.fxml"));
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
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/sessionAdd.fxml"));
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
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/sessionEdit.fxml"));
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
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/trafficRecordAdd.fxml"));
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
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/trafficRecordEdit.fxml"));
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
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/websiteAdd.fxml"));
            Scene scene = new Scene(loader.load());

            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Website");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openWebsiteEditScreen(Website website) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/websiteEdit.fxml"));
            Scene scene = new Scene(loader.load());

            WebsiteEditController controller = loader.getController();
            controller.setWebsite(website);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Website");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
