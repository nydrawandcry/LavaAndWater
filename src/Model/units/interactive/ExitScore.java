package Model.units.interactive;

import Model.events.collectable.ExitScoreActionListener;
import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.solid.Solid;

import java.util.ArrayList;
import java.util.List;

public class ExitScore extends InteractiveUnit {

    private boolean _isCollected;

    private final ArrayList<ExitScoreActionListener> _modelListeners = new ArrayList<>();
    private final ArrayList<ExitScoreActionListener> _viewListeners = new ArrayList<>();

    @Override
    protected boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null && cell.getUnit(ExitScore.class) == null;
    }

    @Override
    void interact(Direction dir) {
        if(_isCollected) {
            return;
        }
        _isCollected = true;

        destroy();
        fireScoreCollected();
    }

    public void addModelExitScoreListener(ExitScoreActionListener l) {
        if (l != null && !_modelListeners.contains(l)) {
            _modelListeners.add(l);
        }
    }

    public void removeModelExitScoreListener(ExitScoreActionListener l) {
        if (l != null) {
            _modelListeners.remove(l);
        }
    }

    public void addViewExitScoreListener(ExitScoreActionListener l) {
        if (l != null && !_viewListeners.contains(l)) {
            _viewListeners.add(l);
        }
    }

    private void fireScoreCollected() {
        for (ExitScoreActionListener l : List.copyOf(_modelListeners)) {
            l.scoreCollected(this);
        }

        for (ExitScoreActionListener l : List.copyOf(_viewListeners)) {
            l.scoreCollected(this);
        }
    }
}
