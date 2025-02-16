package hr.javafx.webtrackly.utils;

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

    public static void openTrafficRecordAddScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenChangeButtonUtil.class.getResource("/hr/javafx/webtrackly/trafficRecordAdd.fxml"));
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
