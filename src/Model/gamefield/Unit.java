package Model.gamefield;

import Model.events.units.ActivationListener;

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

    boolean setOwner(Cell cell){
        if(cell == null) {
            throw new NullPointerException("Ячейка не должна быть null");
        }
        boolean ok = (owner() == null) && !isDestroyed() && canBelongTo(cell);
        if(ok) {
            _owner = cell;
        }
        return ok;
    }

    void removeOwner(){
        _owner = null;
    }

    public void activate(){
        if(isDestroyed()) {
            return;
        }

        if(!_isActive){
            _isActive = true;
        }
        fireActivateChanged();
    }

    public void deactivate(){
        if(isDestroyed()) {
            return;
        }

        if(_isActive){
            _isActive = false;
        }
        fireActivateChanged();
    }

    public void destroy() {
        if (_owner != null) {
            _owner.extractUnit(this);
        }
        _listeners.clear();
        _isDestroyed = true;
        _isActive = false;
    }

    protected abstract boolean canBelongTo(Cell cell);

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

    private void fireActivateChanged() {
        for (ActivationListener listener : _listeners) {
            listener.activateChanged();
        }
    }
}
