package Model.units.liquids;

import Model.gamefield.Cell;
import Model.units.Unit;
import Model.units.impassable.Impassable;

public abstract class Liquid extends Unit {

    private boolean canEnter(Cell cell) {
        if(cell == null) {
            return false;
        }
        if(cell.getUnit(Impassable.class) != null) { //переработать метод надо, чтобы с интерфейсами тоже работало
            return false;
        }
        if(cell.getUnit(this.getClass()) != null) {
            return false;
        }

        return true;
    }

    protected abstract Liquid createInstance();
}
