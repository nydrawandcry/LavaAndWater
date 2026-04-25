package Model.units.moving;

import Model.gamefield.Cell;

public interface Pushable {
    boolean canBePushedTo(Cell target);
}
