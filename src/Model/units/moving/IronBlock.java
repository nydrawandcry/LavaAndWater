package Model.units.moving;

import Model.events.player.PlayerActionListener;
import Model.events.units.IronBlockActionListener;
import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.Unit;
import Model.units.solid.Solid;
import View.unitView.IronBlockWidget;

import java.util.ArrayList;

public class IronBlock extends Unit implements Solid, Pushable {

    private ArrayList<IronBlockActionListener> _listeners = new ArrayList<>();

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null;
    }

    boolean push(Direction dir) {
        if(dir == null) {
            throw new NullPointerException("Направление не может быть null!");
        }

        Cell destination = this.owner().getNeighbour(dir);

        if(canBelongTo(destination)) {

            owner().extractUnit(this);
            if(destination.putUnit(this)) {
                fireIronBlockMoved();
                return true;
            }
        }
        return false;
    }

    public void addIronBlockActionListener(IronBlockActionListener l) {
        if(l != null && !_listeners.contains(l)){
            _listeners.add(l);
        }
    }

    public void removePlayerActionListener(IronBlockActionListener l) {
        if(l != null){
            _listeners.remove(l);
        }
    }

    public void fireIronBlockMoved() {
        for(IronBlockActionListener l : _listeners) {
            l.ironBlockMoved();
        }
    }
}
