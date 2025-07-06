package hr.javafx.webtrackly.app.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DataSerialization implements Serializable {
    private static long serialVersionUID = 1L;
    private static long idCounter = 0L;
    private Long id;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private String changedByRole;
    private LocalDateTime changeTimestamp;

    public DataSerialization(String fieldName, String oldValue, String newValue, String changedByRole, LocalDateTime changeTimestamp) {
        this.id = ++idCounter;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedByRole = changedByRole;
        this.changeTimestamp = changeTimestamp;
    }

    public long getId() {
        return id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getChangedByRole() {
        return changedByRole;
    }

    public void setChangedByRole(String changedByRole) {
        this.changedByRole = changedByRole;
    }

    public LocalDateTime getChangeTimestamp() {
        return changeTimestamp;
    }

    public void setChangeTimestamp(LocalDateTime changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
    }
}
