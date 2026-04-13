package Model.units;

import Model.gamefield.Cell;
import Model.units.impassable.Impassable;

public class Player extends Unit {

    private boolean _isAlive;

    public boolean isAlive() {
        return _isAlive;
    }

    public void kill() {
        _isAlive = false;
        deactivate();
    }

    @Override
    protected boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Impassable.class) == null;
    }
}
