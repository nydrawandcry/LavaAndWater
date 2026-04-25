package Model.units.moving;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.Unit;
import Model.units.solid.Solid;

public class Player extends Unit {

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
