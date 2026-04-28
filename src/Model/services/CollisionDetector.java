package Model.services;

import Model.events.LiquidSystemActionListener;
import Model.gamefield.Cell;
import Model.units.solid.Wall;

public class CollisionDetector implements LiquidSystemActionListener {

    private void resolve(Cell cell) {
        if(cell.getLiquidSystem() != null){
            cell.getLiquidSystem().remove(cell);
        }
        else {
            return;
        }
        cell.setLiquidSystem(null);
        cell.putUnit(new Wall());
    }

    @Override
    public void conflictAppeared(Cell cell) {
        resolve(cell);
    }
}
