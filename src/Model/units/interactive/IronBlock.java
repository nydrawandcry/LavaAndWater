package Model.units.interactive;

import Model.events.units.IronBlockActionListener;
import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.solid.Solid;

import java.util.ArrayList;

public class IronBlock extends InteractiveUnit implements Solid {

    private ArrayList<IronBlockActionListener> _listeners = new ArrayList<>();

    @Override
    protected boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null;
    }

    @Override
    void interact(Direction dir) {
        if(dir == null) {
            throw new NullPointerException("Направление не может быть null!");
        }
        if(owner() == null) {
            throw new IllegalStateException("Нельзя толкнуть блок, который не находится на поле");
        }

        Cell destination = this.owner().getNeighbour(dir);

        if(canBelongTo(destination)) {

            if(owner().extractUnit(this)){
                if (destination.putUnit(this)) {
                    fireIronBlockMoved();
                }
            }
        }
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
