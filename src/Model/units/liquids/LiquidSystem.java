package Model.units.liquids;

import Model.gamefield.Cell;
import Model.units.solid.Solid;

import java.util.HashSet;
import java.util.Set;

public abstract class LiquidSystem {

    protected final Set<Cell> _cells = new HashSet<>();

    public boolean contains(Cell cell) {
        return _cells.contains(cell);
    }

    public void spread() {
        Set<Cell> next = new HashSet<>();

        for(Cell c : _cells) {
            for(Cell neighbour : c.getNeighbours().values()) {
                if(canOccupy(neighbour) && !_cells.contains(neighbour)) {
                    next.add(neighbour);
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
    }

    public Set<Cell> getCells() {
        return Set.copyOf(_cells);
    }
}
