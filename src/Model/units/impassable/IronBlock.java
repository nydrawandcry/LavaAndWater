package Model.units.impassable;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.Unit;

public class IronBlock extends Unit implements Impassable{

    @Override
    protected boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Impassable.class) == null;
    }

    public boolean moveByPlayer(Direction dir) {
        Cell current = owner();
        Cell target = owner().getNeighbours().get(dir);

        if(target == null || target.getUnit(Impassable.class) != null) {
            return false;
        }

        current.extractUnit(this);
        target.putUnit(this);

        return true;
    }
}
