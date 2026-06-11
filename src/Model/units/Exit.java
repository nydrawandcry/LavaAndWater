package Model.units;

import Model.events.collectable.ExitScoreActionListener;
import Model.gamefield.Cell;
import Model.units.interactive.ExitScore;
import Model.units.solid.Solid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Exit extends Unit {

    private ArrayList<ExitScore> _leftScores = new ArrayList<>();

    private ExitScoreActionListener _scoreListener = new ExitScoreActionHandler();

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Exit.class) == null && cell.getUnit(Solid.class) == null;
    }

    public ExitScoreActionListener getExitScoreListener() {
        return _scoreListener;
    }

    public List<ExitScore> getLeftScores() {
        return Collections.unmodifiableList(_leftScores);
    }

    public void addExitScore(ExitScore score) {
        if(score == null) {
            return;
        }
        _leftScores.add(score);
    }

    public void decreaseLeftScores(ExitScore score) {
        if(score == null) {
            return;
        }

        _leftScores.remove(score);
        score.removeExitScoreListener(this.getExitScoreListener());

        if(_leftScores.isEmpty()) {
            this.activate();
        }
    }

    private class ExitScoreActionHandler implements ExitScoreActionListener {
        @Override
        public void scoreCollected(ExitScore score) {
            decreaseLeftScores(score);
        }
    }
}
