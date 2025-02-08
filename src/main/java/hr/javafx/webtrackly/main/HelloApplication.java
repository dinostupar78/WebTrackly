package hr.javafx.webtrackly.main;

import hr.javafx.webtrackly.app.exception.RepositoryAccessException;
import hr.javafx.webtrackly.controller.FirstScreenController;
import hr.javafx.webtrackly.utils.DbConnectionUtil;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class HelloApplication extends Application {
    public static final Logger log = LoggerFactory.getLogger(HelloApplication.class);
    private Connection connection;


    @Override
    public void start(Stage stage) throws IOException {

        try{
            connection = DbConnectionUtil.getInstance();
            stage.setTitle("Hello!");
            stage.setScene(new FirstScreenController().showItemSearchScreen());
            stage.show();
        } catch (RepositoryAccessException | SQLException e) {
            throw new RepositoryAccessException(e);
        }

    }

    public static void main(String[] args) {
        launch();
    }
}