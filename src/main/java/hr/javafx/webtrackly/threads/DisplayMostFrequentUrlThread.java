package hr.javafx.webtrackly.threads;
import hr.javafx.webtrackly.app.db.WebsiteDbRepository3;
import javafx.application.Platform;
import javafx.scene.control.Label;
import java.util.Optional;
import static hr.javafx.webtrackly.utils.ExtractDomainUtil.extractDomain;

/**
 * Thread koji dohvaća i prikazuje najčešće korištenu URL adresu iz baze podataka.
 * Ažurira GUI s nazivom domene i brojem pojavljivanja.
 */

public class DisplayMostFrequentUrlThread implements Runnable{

    private final WebsiteDbRepository3 wbRepository;
    private final Label mostFrequentDomainLabel;
    private final Label mostFrequentDomainCountLabel;

    public DisplayMostFrequentUrlThread(WebsiteDbRepository3 wbRepository, Label mostFrequentDomainLabel, Label mostFrequentDomainCountLabel) {
        this.wbRepository = wbRepository;
        this.mostFrequentDomainLabel = mostFrequentDomainLabel;
        this.mostFrequentDomainCountLabel = mostFrequentDomainCountLabel;
    }

    /**
     * Metoda koja se izvršava u threadu.
     * Dohvaća najčešće korištenu URL adresu iz baze podataka i ažurira GUI s nazivom domene i brojem pojavljivanja.
     */

    @Override
    public void run() {
        Optional<String> topUrlOpt = wbRepository.findMostFrequentUrl();

        if (topUrlOpt.isPresent()) {
            String topUrl = topUrlOpt.get();
            String domain = extractDomain(topUrl);
            Integer count = wbRepository.countByUrl(topUrl);

            Platform.runLater(() -> {
                mostFrequentDomainCountLabel.setText(domain);
                mostFrequentDomainLabel.setText("Number Of Domains: " + count);

            });
        } else {
            Platform.runLater(() -> {
                mostFrequentDomainLabel.setText("N/A");
                mostFrequentDomainCountLabel.setText("Number Of Domains: N/A");
            });
        }

    }
}
