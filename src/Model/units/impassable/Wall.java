package Model.units.impassable;

import Model.gamefield.Cell;
import Model.units.Unit;

public class Wall extends Unit implements Impassable {

    @Override
    protected boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Impassable.class) == null; //может стоять на клетках, где еще нет барьеров и если клетка существует
    }
}
