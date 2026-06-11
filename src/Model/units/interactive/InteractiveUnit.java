package Model.units.interactive;

import Model.gamefield.Direction;
import Model.gamefield.Unit;

public abstract class InteractiveUnit extends Unit {

    abstract void interact(Direction dir);
}
