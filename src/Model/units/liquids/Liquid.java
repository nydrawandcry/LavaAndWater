package Model.units.liquids;

import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.units.Unit;
import Model.units.impassable.Impassable;

import java.util.ArrayList;

public abstract class Liquid extends Unit {

    @Override
    protected boolean canBelongTo(Cell cell) {
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

    public void expand(Gamefield field) {
        ArrayList<Cell> newCellsForExpand = new ArrayList();
        //отбор клеток, где будет происходить распространение жидкости
        for(Cell c : field) {
            if(c.getUnit(this.getClass()) != null) {
                for(Cell neighbour : c.getNeighbours().values()) {
                    if(canBelongTo(neighbour)) {
                        newCellsForExpand.add(neighbour);
                    }
                }
            }
        }

        for(Cell c : newCellsForExpand) {
            Liquid l = createInstance();
            l.setOwner(c);
            l.activate();
            //c.putUnit(l); //todo реализовать метод putUnit в классе Cell
        }
    }

    protected abstract Liquid createInstance();
}
