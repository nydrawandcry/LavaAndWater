package Model.units;

import Model.events.units.ActivationListener;
import Model.gamefield.Cell;

import java.util.ArrayList;

public abstract class Unit {

    private Cell _owner;
    private boolean _isActive;
    private boolean _isDestroyed;

    private ArrayList<ActivationListener> _listeners = new ArrayList<>();

    public boolean isActive(){
        return _isActive;
    }

    public boolean isDestroyed(){
        return _isDestroyed;
    }

    public Cell owner(){
        return _owner;
    }

    public void setOwner(Cell cell){
        if(cell == null) {
            throw new NullPointerException("Ячейка не должна быть null");
        }

        _owner = cell;
    }

    public void removeOwner(){
        _owner = null;
    }

    public void activate(){
        _isActive = true;
        fireActivateChanged();
    }

    public void deactivate(){
        _isActive = false;
        fireActivateChanged();
    }

    public void destroy() {
        if (_owner != null) {
            _owner.extractUnit(this);
        }
        _isDestroyed = true;
        _isActive = false;
    }

    public abstract boolean canBelongTo(Cell cell);

    public void addUnitActivationListener(ActivationListener l) {
        if (l != null && !_listeners.contains(l)) {
            _listeners.add(l);
        }
    }

    public void removeUnitActivationListener(ActivationListener l) {
        if (l != null && _listeners.contains(l)) {
            _listeners.remove(l);
        }
    }

    protected void fireActivateChanged() {
        for (ActivationListener listener : _listeners) {
            listener.activateChanged();
        }
    }
}
