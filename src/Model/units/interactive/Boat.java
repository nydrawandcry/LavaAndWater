package Model.units.interactive;

import Model.events.units.BoatActionListener;
import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.liquids.Water;
import Model.units.solid.Solid;

import java.util.ArrayList;

public class Boat extends InteractiveUnit {

    private ArrayList<BoatActionListener> _listeners = new ArrayList<>();

    @Override
    public boolean canBelongTo(Cell cell) {  //нельзя двигать на твердые объекты и на воду
        return cell != null
                && cell.getUnit(Solid.class) == null
                && cell.getUnit(Boat.class) == null
                && (cell.getLiquidSystem() == null || cell.getLiquidSystem().getClass() != Water.class);
    }

    @Override
    void interact(Direction dir) {
        if(dir == null) {
            throw new NullPointerException("Направление не может быть null!");
        }

        Cell destination = this.owner().getNeighbour(dir);

        if(canBelongTo(destination)) {
            owner().extractUnit(this);
            if(destination.putUnit(this)) {
                fireBoatMoved();
            }
        }
    }

    // ------------- EVENTS -------------

    public void addBoatActionListener(BoatActionListener l) {
        if(l != null && !_listeners.contains(l)){
            _listeners.add(l);
        }
    }

    public void removeBoatActionListener(BoatActionListener l) {
        if(l != null){
            _listeners.remove(l);
        }
    }

    private void fireBoatMoved() {
        for(BoatActionListener l : _listeners) {
            l.boatMoved();
        }
    }
}
