package hr.javafx.webtrackly.threads;
import hr.javafx.webtrackly.app.db.SessionDbRepository3;
import hr.javafx.webtrackly.app.enums.DeviceType;
import javafx.application.Platform;
import javafx.scene.control.Label;
import java.util.Optional;

/**
 * Thread koji dohvaća i prikazuje najčešće korišteni tip uređaja iz baze podataka.
 * Koristi SessionDbRepository3 za pristup podacima i ažurira UI elemente Label.
 */

public class DisplayMostFrequentDeviceThread implements Runnable{
    private final SessionDbRepository3 sessionRepository;
    private final Label deviceLabel;
    private final Label countLabel;

    public DisplayMostFrequentDeviceThread(SessionDbRepository3 sessionRepository, Label deviceLabel, Label countLabel) {
        this.sessionRepository = sessionRepository;
        this.deviceLabel = deviceLabel;
        this.countLabel = countLabel;

    }

    /**
     * Metoda koja se izvršava u threadu.
     * Dohvaća najčešće korišteni tip uređaja i ažurira UI elemente.
     */

    @Override
    public void run() {
        Optional<DeviceType> opt = sessionRepository.findMostFrequentDeviceType();

        if (opt.isPresent()) {
            DeviceType dt = opt.get();
            Integer count = sessionRepository.countByDeviceType(dt);
            Platform.runLater(() -> {
                deviceLabel.setText(dt.name());
                countLabel .setText("Number of Devices: " + count);
            });
        } else {
            Platform.runLater(() -> {
                deviceLabel.setText("N/A");
                countLabel .setText("Number of Devices: N/A");
            });
        }

    }
}
