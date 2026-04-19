package Model.units.impassable;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.Unit;

public class IronBlock extends Unit implements Solid {

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null;
    }

    public boolean moveByPlayer(Direction dir) {
        Cell current = owner();
        Cell target = owner().getNeighbours().get(dir);

        if(target == null || target.getUnit(Solid.class) != null) {
            return false;
        }

        current.extractUnit(this);
        target.putUnit(this);

        return true;
    }
}
