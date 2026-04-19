package Model.units;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.impassable.Pushable;
import Model.units.impassable.Solid;

public class Player extends Unit {

    private boolean _isAlive = true;

    public boolean isAlive() {
        return _isAlive;
    }

    public void kill() {
        _isAlive = false;
        deactivate();
    }

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null;
    }

    public boolean moveTo(Direction dir) {
        Cell destination = owner().getNeighbour(dir);

        if(destination == null) {
            return false;
        }

        Unit blocking = destination.getUnit(Solid.class);

        if(blocking instanceof Pushable) {
            Cell next = destination.getNeighbour(dir);

            if(!((Pushable) blocking).canBePushedTo(next)){
                return false;
            }

            destination.extractUnit(blocking);
            next.putUnit(blocking);
        } 
        else if(blocking != null){
            return false;
        }

        owner().extractUnit(this);
        destination.putUnit(this);

        return true;
    }
}
