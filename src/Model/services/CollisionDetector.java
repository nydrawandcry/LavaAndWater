package Model.services;

import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.units.impassable.Wall;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;

public class CollisionDetector {

    public void resolve(Gamefield field) {
        for(Cell c : field) {
            boolean hasLava = c.getUnit(Lava.class) != null;
            boolean hasWater = c.getUnit(Water.class) != null;

            if(hasLava && hasWater) {
                createWallsInCell(c);
            }
        }
    }

    private void createWallsInCell(Cell cell) {
        cell.extractUnit(cell.getUnit(Lava.class));
        cell.extractUnit(cell.getUnit(Water.class));

        cell.putUnit(new Wall());
    }
}
