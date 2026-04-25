package Model.units.moving;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.Unit;
import Model.units.solid.Solid;

public class IronBlock extends Unit implements Solid, Pushable {

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null;
    }

    boolean push(Direction dir) {
        if(dir == null) {
            throw new NullPointerException("Направление не может быть null!");
        }

        Cell destination = this.owner().getNeighbour(dir);

        if(canBelongTo(destination)) {

            owner().extractUnit(this);
            return destination.putUnit(this);
        }
        return false;
    }
}
