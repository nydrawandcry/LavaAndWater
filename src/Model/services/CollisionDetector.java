package Model.services;

import Model.gamefield.Cell;
import Model.units.solid.Wall;
import Model.units.liquids.LiquidSystem;

import java.util.HashSet;
import java.util.Set;

public class CollisionDetector {

    public void resolve(LiquidSystem first, LiquidSystem second) {
        Set<Cell> conflicts = new HashSet<>(first.getCells());
        conflicts.retainAll(second.getCells());

        for (Cell cell : conflicts) {
            first.remove(cell);
            second.remove(cell);
            cell.putUnit(new Wall());
        }
    }
}
