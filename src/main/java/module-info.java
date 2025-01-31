module hr.javafx.webtrackly {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens hr.javafx.webtrackly to javafx.fxml;
    exports hr.javafx.webtrackly;
}