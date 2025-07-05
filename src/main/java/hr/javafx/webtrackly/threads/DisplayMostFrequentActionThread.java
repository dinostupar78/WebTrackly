package hr.javafx.webtrackly.threads;

import hr.javafx.webtrackly.app.db.UserActionDbRepository3;
import hr.javafx.webtrackly.app.enums.BehaviourType;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Optional;

public class DisplayMostFrequentActionThread implements Runnable {

    private final UserActionDbRepository3 userActionRepository;
    private final Label actionLabel;
    private final Label countLabel;

    public DisplayMostFrequentActionThread(UserActionDbRepository3 userActionRepository, Label actionLabel, Label countLabel) {
        this.userActionRepository = userActionRepository;
        this.actionLabel = actionLabel;
        this.countLabel = countLabel;
    }


    @Override
    public void run() {
        Optional<BehaviourType> opt = userActionRepository.findMostFrequentAction();

        if (opt.isPresent()) {
            BehaviourType bt = opt.get();
            Integer count = userActionRepository.countByAction(bt);
            Platform.runLater(() -> {
                actionLabel.setText(bt.name());
                countLabel .setText("Number of Actions: " + count);
            });
        } else {
            Platform.runLater(() -> {
                actionLabel.setText("N/A");
                countLabel .setText("Number of Actions: N/A");
            });
        }

    }
}
