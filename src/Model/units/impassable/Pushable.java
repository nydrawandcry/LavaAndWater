package Model.units.impassable;

import Model.gamefield.Cell;

public interface Pushable {
    boolean canBePushedTo(Cell target);
}
