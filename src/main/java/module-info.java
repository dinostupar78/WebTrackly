module hr.javafx.webtrackly {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.slf4j;
    requires java.sql;

    opens hr.javafx.webtrackly.controller to javafx.fxml;
    opens hr.javafx.webtrackly to javafx.fxml;
    exports hr.javafx.webtrackly.main;
    opens hr.javafx.webtrackly.main to javafx.fxml;
    opens hr.javafx.webtrackly.app.model to javafx.base;
}