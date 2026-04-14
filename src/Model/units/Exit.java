package Model.units;

import Model.gamefield.Cell;
import Model.units.impassable.Impassable;

public class Exit extends Unit {

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Exit.class) == null && cell.getUnit(Impassable.class) == null;
    }
}
