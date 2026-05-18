package Model.units;

import Model.gamefield.Cell;
import Model.units.solid.Solid;

public class Exit extends Unit {

    private int _leftScores = 3;

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Exit.class) == null && cell.getUnit(Solid.class) == null;
    }

    public int getLeftScores() {
        return _leftScores;
    }

    public void decreaseLeftScores() {
        _leftScores--;
    }
}
