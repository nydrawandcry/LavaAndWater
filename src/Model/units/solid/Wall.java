package Model.units.solid;

import Model.gamefield.Cell;
import Model.gamefield.Unit;

public class Wall extends Unit implements Solid {

    @Override
    protected boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null; //может стоять на клетках, где еще нет барьеров и если клетка существует
    }
}
