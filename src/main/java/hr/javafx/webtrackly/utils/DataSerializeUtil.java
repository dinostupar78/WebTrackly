package hr.javafx.webtrackly.utils;

import hr.javafx.webtrackly.app.model.DataSerialization;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataSerializeUtil {
    private DataSerializeUtil() {}

    private static final String FILE_PATH = "user.dat";

    public static void serializeData(DataSerialization object) {
        List<DataSerialization> changes = deserializeData();
        changes.add(object);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(changes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<DataSerialization> deserializeData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<DataSerialization>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
