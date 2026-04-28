package Model.units.liquids;

import Model.events.LiquidSystemActionListener;
import Model.events.PlayerActionListener;
import Model.gamefield.Cell;
import Model.units.solid.Solid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class LiquidSystem {

    protected final Set<Cell> _cells = new HashSet<>();

    private ArrayList<LiquidSystemActionListener> _listeners = new ArrayList<>();

    public boolean contains(Cell cell) {
        return _cells.contains(cell);
    }

    public void spread() {
        Set<Cell> next = new HashSet<>();

        for(Cell c : _cells) {
            for(Cell neighbour : c.getNeighbours().values()) {
                if(canOccupy(neighbour) && !_cells.contains(neighbour)) {
                    if(neighbour.getLiquidSystem() != this) {
                        fireConflictAppeared(neighbour);
                    }
                    else {
                        next.add(neighbour);
                        neighbour.setLiquidSystem(this);
                    }
                }
            }
        }

        _cells.addAll(next);
    }

    public boolean canOccupy(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null;
    }

    public void remove(Cell cell) {
        _cells.remove(cell);
    }

    public void addSource(Cell cell) {
        if (canOccupy(cell)) {
            _cells.add(cell);
        }
        cell.setLiquidSystem(this);
    }

    public Set<Cell> getCells() {
        return Set.copyOf(_cells);
    }

    public void addLiquidSystemActionListener(LiquidSystemActionListener l) {
        if(l != null && !_listeners.contains(l)){
            _listeners.add(l);
        }
    }

    public void removeLiquidSystemActionListener(LiquidSystemActionListener l) {
        if(l != null){
            _listeners.remove(l);
        }
    }

    public void fireConflictAppeared(Cell cell) {
        for(LiquidSystemActionListener l : _listeners) {
            l.conflictAppeared(cell);
        }
    }
}
