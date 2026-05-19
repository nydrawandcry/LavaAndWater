package Model.units.interactive;

import Model.events.collectable.ExitScoreActionListener;
import Model.gamefield.Cell;
import Model.units.Unit;
import Model.units.solid.Solid;

import java.util.ArrayList;

public class ExitToken extends Unit implements Collectable {

    private ArrayList<ExitScoreActionListener> _listeners = new ArrayList<>();

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null && cell.getUnit(ExitToken.class) == null;
    }

    void collect() {
        destroy();
        fireTokenCollected();
    }

    public void addExitTokenListener(ExitScoreActionListener l) {
        if (l != null && !_listeners.contains(l)) {
            _listeners.add(l);
        }
    }

    public void removeExitTokenListener(ExitScoreActionListener l) {
        _listeners.remove(l);
    }

    private void fireTokenCollected() {
        for (ExitScoreActionListener l : new ArrayList<>(_listeners)) {
            l.tokenCollected(this);
        }
    }
}
