package Model.events.liquids;

import Model.gamefield.Cell;

public interface LiquidSystemCollisionListener {
    void conflictAppeared(Cell cell);
}
