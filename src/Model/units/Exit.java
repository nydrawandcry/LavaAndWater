package Model.units;

import Model.gamefield.Cell;

public class Exit extends Unit {

    @Override
    protected boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Exit.class) == null;
    }
}
