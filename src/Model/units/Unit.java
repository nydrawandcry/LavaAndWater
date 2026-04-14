package Model.units;

import Model.gamefield.Cell;

public abstract class Unit {

    private Cell _owner;
    private boolean _isActive;
    private boolean _isDestroyed;

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
    }

    public void deactivate(){
        _isActive = false;
    }

    public void destroy() {
        if (_owner != null) {
            _owner.extractUnit(this);
        }
        _isDestroyed = true;
        _isActive = false;
    }

    public abstract boolean canBelongTo(Cell cell);
}
