package Model.units.liquids;

import Model.gamefield.Cell;
import Model.units.impassable.Solid;

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
                if(canOccupy(neighbour)) {
                    next.add(neighbour);
                }
            }
        }
    }


    public boolean canOccupy(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null;
    }

    public void remove(Cell cell) {
        _cells.remove(cell);
    }
}
