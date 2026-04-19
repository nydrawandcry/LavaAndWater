package Model.units;

import Model.gamefield.Cell;
import Model.units.impassable.Solid;

public class Exit extends Unit {

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Exit.class) == null && cell.getUnit(Solid.class) == null;
    }
}
