package Model.units.liquids;

import Model.events.liquids.LiquidSystemCollisionListener;
import Model.events.player.PlayerMovementListener;
import Model.gamefield.Cell;
import Model.units.solid.Solid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class LiquidSystem {

    protected final Set<Cell> _cells = new HashSet<>();

    private ArrayList<LiquidSystemCollisionListener> _listeners = new ArrayList<>();

    private PlayerMovementListener _listener = new PlayerMovementHandler();

    public boolean contains(Cell cell) {
        return _cells.contains(cell);
    }

    public PlayerMovementListener getPlayerMovementListener() {
        return _listener;
    }

    public void spread() {
        Set<Cell> next = new HashSet<>();

        for(Cell c : _cells) {
            for(Cell neighbour : c.getNeighbours().values()) {
                if(canOccupy(neighbour) && !_cells.contains(neighbour)) {
                    if( neighbour.getLiquidSystem() != null && neighbour.getLiquidSystem() != this) {
                        fireConflictAppeared(neighbour);
                    }
                    else {
                        next.add(neighbour);
                    }
                }
            }
        }

        for(Cell c : next) {
            _cells.add(c);
            c.setLiquidSystem(this); //на момент наступления события клетка и жидкость уже связаны
            //т.е. cell.getLiquidSystem == liquid и liquid.contains(cell) == true
        }

    }

    protected boolean canOccupy(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null;
    }

    public void remove(Cell cell) {
        _cells.remove(cell);
        cell.setLiquidSystem(null);
    }

    public void addSource(Cell cell) {
        if (canOccupy(cell)) {
            _cells.add(cell);
            cell.setLiquidSystem(this);
        }
    }

    public Set<Cell> getCells() {
        return Collections.unmodifiableSet(_cells);
    }

    public void addLiquidSystemCollisionListener(LiquidSystemCollisionListener l) {
        if(l != null && !_listeners.contains(l)){
            _listeners.add(l);
        }
    }

    public void removeLiquidSystemCollisionListener(LiquidSystemCollisionListener l) {
        if(l != null){
            _listeners.remove(l);
        }
    }

    private void fireConflictAppeared(Cell cell) {
        for(LiquidSystemCollisionListener l : _listeners) {
            l.conflictAppeared(cell);
        }
    }

    private class PlayerMovementHandler implements PlayerMovementListener{
        @Override
        public void playerMoved () {
            spread();
        }
    }
}
