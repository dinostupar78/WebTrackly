module hr.javafx.webtrackly {
    requires javafx.controls;
    requires javafx.fxml;

    exports hr.javafx.webtrackly.app.model;
    exports hr.javafx.webtrackly.app.enums;
    exports hr.javafx.webtrackly.app.db;
    exports hr.javafx.webtrackly.app.exception;
    exports hr.javafx.webtrackly.app.generics;
    exports hr.javafx.webtrackly.app.files;
    exports hr.javafx.webtrackly.main;
    exports hr.javafx.webtrackly.controller;
    exports hr.javafx.webtrackly.threads;
    exports hr.javafx.webtrackly.utils;


    requires org.kordamp.bootstrapfx.core;
    requires org.slf4j;
    requires java.sql;
    requires com.h2database;
    requires java.management;

    opens hr.javafx.webtrackly.controller to javafx.fxml;
    opens hr.javafx.webtrackly to javafx.fxml;
    opens hr.javafx.webtrackly.main to javafx.fxml;
    opens hr.javafx.webtrackly.app.model to javafx.base;
}