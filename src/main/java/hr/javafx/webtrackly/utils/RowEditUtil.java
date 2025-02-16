package hr.javafx.webtrackly.utils;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.util.function.Consumer;

public class RowEditUtil<T> {
    public RowEditUtil() {
    }

    public void addRowEditHandler(TableView<T> tableView, Consumer<T> editHandler) {
        tableView.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    T item = row.getItem();
                    editHandler.accept(item);
                }
            });
            return row;
        });
    }
}
