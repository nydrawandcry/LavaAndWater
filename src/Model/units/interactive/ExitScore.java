package Model.units.interactive;

import Model.events.collectable.ExitScoreActionListener;
import Model.gamefield.Cell;
import Model.units.Unit;
import Model.units.solid.Solid;

import java.util.ArrayList;

public class ExitScore extends Unit implements Collectable {

    private boolean _isCollected;

    private ArrayList<ExitScoreActionListener> _listeners = new ArrayList<>();

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null && cell.getUnit(ExitScore.class) == null;
    }

    void collect() {
        if(_isCollected) {
            return;
        }
        _isCollected = true;

        destroy();
        fireScoreCollected();
    }

    public void addExitScoreListener(ExitScoreActionListener l) {
        if (l != null && !_listeners.contains(l)) {
            _listeners.add(l);
        }
    }

    public void removeExitScoreListener(ExitScoreActionListener l) {
        _listeners.remove(l);
    }

    private void fireScoreCollected() {
        for (ExitScoreActionListener l : new ArrayList<>(_listeners)) {
            l.scoreCollected(this);
        }
    }
}
