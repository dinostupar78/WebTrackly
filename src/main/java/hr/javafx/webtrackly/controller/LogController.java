package hr.javafx.webtrackly.controller;

import hr.javafx.webtrackly.app.model.LogEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hr.javafx.webtrackly.main.HelloApplication.log;

/**
 * Kontroler za upravljanje prikazom i filtriranjem logova u aplikaciji WebTrackly.
 * Omogućuje korisnicima pregled logova, filtriranje po datumu, razini loga i ključnim riječima.
 */

public class LogController {
    @FXML
    private DatePicker logDatePickerDate;

    @FXML
    private TableView<LogEntry> logTableView;

    @FXML
    private TableColumn<LogEntry, String> logColumnTimestamp;
    @FXML
    private TableColumn<LogEntry, String> logColumnLevel;
    @FXML
    private TableColumn<LogEntry, String> logColumnMessage;

    private ObservableList<LogEntry> logEntries = FXCollections.observableArrayList();

    /**
     * Inicijalizira kolone u TableView-u i postavlja vrijednosti za prikaz logova.
     * Ova metoda se poziva prilikom učitavanja FXML datoteke.
     */

    public void initialize(){
        logColumnTimestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        logColumnLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        logColumnMessage.setCellValueFactory(new PropertyValueFactory<>("message"));

    }

    /**
     * Metoda za filtriranje logova prema ključnim riječima, razini loga i datumu.
     * Učitava logove iz datoteke i primjenjuje filtre na temelju korisničkog unosa.
     */

    public void filterLogs() {
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
                    log.info("Unmatched log line{} ", line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logTableView.setItems(logEntries);

        LocalDate selectedDate = logDatePickerDate.getValue();

        List<LogEntry> filtered = logEntries.stream()
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
