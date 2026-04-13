package Model.units;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.impassable.Impassable;
import Model.units.impassable.IronBlock;

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

    public boolean moveTo(Direction dir) {
        Cell destination = owner().getNeighbour(dir);

        if(destination == null) {
            return false;
        }

        Unit blocking = destination.getUnit(Impassable.class);

        if(blocking instanceof IronBlock) { //мне не нравится тут эта проверка на конкретный класс...
            if(!((IronBlock) blocking).moveByPlayer(dir)){
                return false;
            }
        } 
        else if(blocking != null){
            return false;
        }

        owner().extractUnit(this);
        destination.putUnit(this);

        return true;
    }
}
