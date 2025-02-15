package hr.javafx.webtrackly.main;

import hr.javafx.webtrackly.controller.FirstScreenController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HelloApplication extends Application {
    public static final Logger log = LoggerFactory.getLogger(HelloApplication.class);

    @Override
    public void start(Stage stage) throws IOException {
        log.info("Application started!");
        stage.setTitle("Hello!");
        stage.setScene(new FirstScreenController().showLoginPanel());
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}