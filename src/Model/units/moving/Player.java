package Model.units.moving;

import Model.events.PlayerActionListener;
import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.Unit;
import Model.units.solid.Solid;

import java.util.ArrayList;

public class Player extends Unit {

    private ArrayList<PlayerActionListener> _listeners = new ArrayList<>();

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
            ((IronBlock) blocking).push(dir);
        } 
        else if(blocking != null){
            return false;
        }

        owner().extractUnit(this);
        destination.putUnit(this);
        firePlayerMoved();

        return true;
    }

    public void addPlayerActionListener(PlayerActionListener l) {
        if(l != null && !_listeners.contains(l)){
            _listeners.add(l);
        }
    }

    public void removePlayerActionListener(PlayerActionListener l) {
        if(l != null){
            _listeners.remove(l);
        }
    }

    public void firePlayerMoved() {
        for(PlayerActionListener l : _listeners) {
            l.playerMoved();
        }
    }
}
