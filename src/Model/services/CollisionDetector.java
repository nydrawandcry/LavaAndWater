package Model.services;

import Model.events.liquids.LiquidAppearanceInCellListener;
import Model.events.liquids.LiquidSystemCollisionListener;
import Model.gamefield.Cell;
import Model.units.solid.Wall;

public class CollisionDetector {

    private final LiquidSystemCollisionListener _liquidListener = new LiquidSystemCollisionHandler();

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

    private class LiquidSystemCollisionHandler implements LiquidSystemCollisionListener {
        @Override
        public void conflictAppeared(Cell cell) {
            resolve(cell);
        }
    }

    public LiquidSystemCollisionListener getLiquidListener() {
        return _liquidListener;
    }

}
