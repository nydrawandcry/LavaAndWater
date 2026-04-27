package Model.events;

import Model.gamefield.Cell;

public interface LiquidSystemActionListener {
    void conflictAppeared(Cell cell);
}
