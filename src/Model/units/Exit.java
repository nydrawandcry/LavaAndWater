package Model.units;

import Model.events.collectable.ExitScoreActionListener;
import Model.gamefield.Cell;
import Model.units.interactive.ExitScore;
import Model.units.solid.Solid;

public class Exit extends Unit {

    private int _leftScores;

    private ExitScoreActionListener _tokenListener = new ExitKeyActionHandler();

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Exit.class) == null && cell.getUnit(Solid.class) == null;
    }

    public ExitScoreActionListener getTokenListener() {
        return _tokenListener;
    }

    public int getLeftScores() {
        return _leftScores;
    }

    public void setLeftScores(int amount) {
        _leftScores = amount;
    }

    public void decreaseLeftScores() {
        _leftScores--;
    }

    private class ExitKeyActionHandler implements ExitScoreActionListener {
        @Override
        public void tokenCollected(ExitScore token) {
            decreaseLeftScores();
        }
    }
}
