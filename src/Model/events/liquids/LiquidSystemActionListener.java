package Model.events.liquids;

import Model.gamefield.Cell;

public interface LiquidSystemActionListener {
    void conflictAppeared(Cell cell);
}
