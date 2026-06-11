package Model.events.collectable;

import Model.units.interactive.ExitScore;

import java.util.EventListener;

public interface ExitScoreActionListener extends EventListener {
    void scoreCollected(ExitScore score);
}
