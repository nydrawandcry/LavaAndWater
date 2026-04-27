package Model.services;

import Model.gamefield.Cell;
import Model.units.solid.Wall;

public class CollisionDetector {

    public void resolve(Cell cell) {
        cell.setLiquidSystem(null);
        cell.putUnit(new Wall());
    }
}
