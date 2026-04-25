package Model.units.moving;

import Model.gamefield.Cell;
import Model.units.Unit;
import Model.units.solid.Solid;

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
