package hr.javafx.webtrackly.utils;
import hr.javafx.webtrackly.app.model.DataSerialization;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Util klasa za serijalizaciju i deserializaciju podataka u aplikaciji WebTrackly.
 * Ova klasa omogućuje spremanje objekata tipa DataSerialization u datoteku
 * i njihovo kasnije učitavanje. Podaci se serijaliziraju u binarnom formatu i spremaju u datoteku "webtrackly.dat".
 * Ova klasa također pruža metode za dodavanje novih objekata i dohvaćanje svih serijaliziranih objekata.
 */

public class DataSerializeUtil {
    private DataSerializeUtil() {}

    private static final String FILE_PATH = "webtrackly.dat";

    /**
     * Serijalizira objekt tipa DataSerialization i sprema ga u datoteku.
     * Ako datoteka već postoji, novi objekt se dodaje na kraj liste postojećih objekata.
     *
     * @param object Objekt koji se serijalizira i sprema.
     */

    public static void serializeData(DataSerialization object) {

        List<DataSerialization> changes = deserializeData();
        changes.add(object);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(changes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserializira podatke iz datoteke i vraća listu objekata tipa DataSerialization.
     * Ako datoteka ne postoji ili je prazna, vraća se prazna lista.
     *
     * @return Lista objekata tipa DataSerialization.
     */

    public static List<DataSerialization> deserializeData() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
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
