package Model.units.moving;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.Unit;
import Model.units.solid.Solid;

public class Player extends Unit {

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null;
    }

    public boolean moveTo(Direction dir) {
        Cell destination = owner().getNeighbour(dir);

        if(destination == null) {
            return false;
        }

        Unit blocking = destination.getUnit(Solid.class);

        if(blocking instanceof IronBlock) { //тут все равно был уже instanceof, убрала в целом Pushable (есть ли теперь вообще в нем смысл?)
            ((IronBlock) blocking).push(dir); //в будущем при неудачной попытке сдвинуть блок будет посылаться событие, что игрок ход не сделал
        } 
        else if(blocking != null){
            return false;
        }

        owner().extractUnit(this);
        destination.putUnit(this);

        return true;
    }
}
