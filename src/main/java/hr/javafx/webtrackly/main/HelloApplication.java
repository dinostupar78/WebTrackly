package hr.javafx.webtrackly.main;

import hr.javafx.webtrackly.controller.FirstScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HelloApplication extends Application {
    public static final Logger log = LoggerFactory.getLogger(HelloApplication.class);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/hr/javafx/webtrackly/loginPanel.fxml"));
        stage.setTitle("Hello!");
        stage.setScene(new FirstScreenController().showItemSearchScreen());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}