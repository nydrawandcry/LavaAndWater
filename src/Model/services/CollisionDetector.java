package Model.services;

import Model.gamefield.Cell;
import Model.units.impassable.Wall;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;

public class CollisionDetector {

    private void createWallsInCell(Cell cell) {
        cell.extractUnit(cell.getUnit(Lava.class));
        cell.extractUnit(cell.getUnit(Water.class));

        cell.putUnit(new Wall());
    }
}
