package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.model.LogEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hr.javafx.webtrackly.main.HelloApplication.log;
import static javafx.collections.FXCollections.observableArrayList;

public class LogController {
    @FXML
    private DatePicker logDatePickerDate;

    @FXML
    private ComboBox<String> logComboBoxLogLevel;

    @FXML
    private TextField logTextFieldKeyword;

    @FXML
    private TableView<LogEntry> logTableView;

    @FXML
    private TableColumn<LogEntry, String> logColumnTimestamp;
    @FXML
    private TableColumn<LogEntry, String> logColumnLevel;
    @FXML
    private TableColumn<LogEntry, String> logColumnMessage;

    private ObservableList<LogEntry> logEntries = FXCollections.observableArrayList();

    public void initialize(){
        logColumnTimestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        logColumnLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        logColumnMessage.setCellValueFactory(new PropertyValueFactory<>("message"));

        logComboBoxLogLevel.setItems(observableArrayList("ALL", "INFO", "WARNING", "ERROR", "DEBUG"));
        logComboBoxLogLevel.setValue("ALL");

        Pattern pattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})\\s+(\\S+)\\s+\\[.*?\\]\\s+\\S+\\s+\\[.*?\\]\\s+(.*)$");

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Dino\\Desktop\\PROJEKT\\WebTrackly\\logs\\pogreske.log"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    String timestamp = matcher.group(1);
                    String level = matcher.group(2);
                    String message = matcher.group(3);
                    logEntries.add(new LogEntry(timestamp, level, message));
                } else {
                    log.info("Unmatched log line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logTableView.setItems(logEntries);

    }

    public void filterLogs() {
        String keyword = logTextFieldKeyword.getText().toLowerCase();
        String selectedLevel = logComboBoxLogLevel.getValue();
        LocalDate selectedDate = logDatePickerDate.getValue();

        List<LogEntry> filtered = logEntries.stream()
                .filter(log -> keyword.isEmpty() || log.getMessage().toLowerCase().contains(keyword))
                .filter(log -> selectedLevel == null || selectedLevel.equals("ALL") || log.getLevel().equalsIgnoreCase(selectedLevel))
                .filter(log -> {
                    if (selectedDate == null)
                        return true;
                    String datePart = log.getTimestamp().substring(0, 10);
                    return datePart.equals(selectedDate.toString());
                })
                .toList();

        logTableView.setItems(FXCollections.observableArrayList(filtered));
    }

}
