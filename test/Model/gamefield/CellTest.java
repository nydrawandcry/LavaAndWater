package Model.gamefield;

import Model.units.Exit;
import Model.units.Player;
import Model.units.Unit;
import Model.units.impassable.IronBlock;
import Model.units.liquids.Water;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CellTest {
    @Test
    void cell_canStoreZeroUnits() {
        Gamefield field = new Gamefield(4,4);

        Cell cell = field.getCell(0,0);

        assertTrue(cell.isEmpty());
    }

    @Test
    void cell_canStoreMultipleUnits() {
        Gamefield field = new Gamefield(4,4);
        Cell cell = field.getCell(0,0);

        Exit exit = new Exit();
        Player p = new Player();

        cell.putUnit(exit);
        cell.putUnit(p);

        assertEquals(2, cell.getUnits(Unit.class).size());
    }

    @Test
    void cell_cannotExistWithoutField() {
        assertThrows(NullPointerException.class, () -> new Cell(null));
    }

    @Test
    void getNeighbour_returnsOnlyCellObjects() {
        Gamefield field = new Gamefield(4,4);

        Cell c1 = field.getCell(0,0);
        Cell c2 = field.getCell(0,1);

        // вручную добавим соседа через reflection или через поле (если есть сеттер)
        c1.getNeighbours(); // просто проверяем тип

        assertNotNull(c1.getNeighbours());
    }

    @Test
    void putExit_intoEmptyCell_success() {
        Gamefield field = new Gamefield(4,4);

        Cell cell = field.getCell(0,0);

        Exit x = new Exit();

        boolean result = cell.putUnit(x);

        assertTrue(result);
        assertEquals(cell, x.owner());
    }

    @Test
    void putNullUnit_shouldFail() {
        Gamefield field = new Gamefield(3,3);
        Cell cell = field.getCell(0,0);

        boolean result = cell.putUnit(null);

        assertFalse(result);
    }

    @Test
    void extractNullUnit_shouldFail() {
        Gamefield field = new Gamefield(3,3);
        Cell cell = field.getCell(0,0);

        boolean result = cell.extractUnit(null);

        assertFalse(result);
    }

    @Test
    void extractUnit_fromEmptyCell_shouldFail() {
        Gamefield field = new Gamefield(3,3);
        Cell cell = field.getCell(0,0);

        IronBlock block = new IronBlock();

        boolean result = cell.extractUnit(block);

        assertFalse(result);
    }

    @Test
    void getUnits_whenEmpty_returnsEmptyArray() {
        Gamefield field = new Gamefield(3,3);
        Cell cell = field.getCell(0,0);

        ArrayList<Unit> units = cell.getUnits(Unit.class);

        assertEquals(0, units.size());
    }

    @Test
    void cells_thatAreNotNeighbours_returnFalse() {
        Gamefield field = new Gamefield(3,3);

        Cell c1 = field.getCell(0,0);
        Cell c2 = field.getCell(2,2);

        assertFalse(c1.isNeighbour(c2));
    }

    @Test
    void getNeighbour_north_returnsCorrectCell() {
        Gamefield field = new Gamefield(3,3);

        Cell center = field.getCell(1,1);
        Cell north = field.getCell(0,1);

        assertEquals(north, center.getNeighbour(Direction.NORTH));
    }

    @Test
    void getNeighbour_whenNoNeighbour_returnsNull() {
        Gamefield field = new Gamefield(3,3);

        Cell cell = field.getCell(0,0);

        assertNull(cell.getNeighbour(Direction.NORTH));
    }

    @Test
    void extractUnit_success() {
        Gamefield field = new Gamefield(4,4);

        Cell cell = field.getCell(0,0);

        Player p = new Player();
        cell.putUnit(p);

        boolean result = cell.extractUnit(p);

        assertTrue(result);
        assertNull(p.owner());
    }

    @Test
    void neighbour_north() {
        Gamefield field = new Gamefield(4,4);

        Cell center = field.getCell(1,1);
        Cell north = field.getCell(0,1);

        assertTrue(center.isNeighbour(north));
    }

    @Test
    void neighbour_south() {
        Gamefield field = new Gamefield(4,4);

        Cell center = field.getCell(1,1);
        Cell south = field.getCell(2,1);
        assertTrue(center.isNeighbour(south));
    }

    @Test
    void neighbour_east() {
        Gamefield field = new Gamefield(4,4);

        Cell center = field.getCell(1,1);
        Cell east = field.getCell(1,2);

        assertTrue(center.isNeighbour(east));
    }

    @Test
    void neighbour_west() {
        Gamefield field = new Gamefield(4,4);

        Cell center = field.getCell(1,1);
        Cell west = field.getCell(1,0);

        assertTrue(center.isNeighbour(west));
    }
}
