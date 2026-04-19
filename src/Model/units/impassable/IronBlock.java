package Model.units.impassable;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.Unit;

public class IronBlock extends Unit implements Solid, Pushable {

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null;
    }

    @Override
    public boolean canBePushedTo(Cell target) {
        return target != null && target.getUnit(Solid.class) == null;
    }
}
