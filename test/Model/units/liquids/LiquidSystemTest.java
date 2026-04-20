package Model.units.liquids;

import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.units.impassable.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public abstract class LiquidSystemTest<T extends LiquidSystem> {

    protected Gamefield field;
    protected T liquid;

    @BeforeEach
    void setUp() {
        field = new Gamefield(3, 3);
        liquid = createLiquid();
    }

    protected abstract T createLiquid();

    @Test
    void newLiquidSystem_isEmpty() {
        assertTrue(liquid.getCells().isEmpty());
    }

    @Test
    void addSource_addsCellToSystem() {
        Cell source = field.getCell(1, 1);

        liquid.addSource(source);

        assertTrue(liquid.contains(source));
        assertEquals(1, liquid.getCells().size());
    }

    @Test
    void addSource_nullCell_doesNothing() {
        liquid.addSource(null);

        assertTrue(liquid.getCells().isEmpty());
    }

    @Test
    void spread_spreadsToNeighbourCells() {
        Cell source = field.getCell(1, 1);
        liquid.addSource(source);

        liquid.spread();

        assertTrue(liquid.contains(field.getCell(0, 1)));
        assertTrue(liquid.contains(field.getCell(2, 1)));
        assertTrue(liquid.contains(field.getCell(1, 0)));
        assertTrue(liquid.contains(field.getCell(1, 2)));
    }

    @Test
    void spread_doesNotSpreadThroughWalls() {
        Cell source = field.getCell(1, 1);
        liquid.addSource(source);

        field.getCell(0, 1).putUnit(new Wall());

        liquid.spread();

        assertFalse(liquid.contains(field.getCell(0, 1)));
        assertTrue(liquid.contains(field.getCell(2, 1)));
        assertTrue(liquid.contains(field.getCell(1, 0)));
        assertTrue(liquid.contains(field.getCell(1, 2)));
    }

    @Test
    void spread_doesNotDuplicateCellsWhenTwoSourcesReachSameCell() {
        liquid.addSource(field.getCell(1, 0));
        liquid.addSource(field.getCell(1, 2));

        liquid.spread();

        Cell middle = field.getCell(1, 1);

        assertTrue(liquid.contains(middle));
        assertEquals(7, liquid.getCells().size());
    }

    @Test
    void spread_doesNotGoOutsideField() {
        Cell source = field.getCell(0, 0);
        liquid.addSource(source);

        liquid.spread();

        assertTrue(liquid.contains(field.getCell(0, 0)));
        assertTrue(liquid.contains(field.getCell(0, 1)));
        assertTrue(liquid.contains(field.getCell(1, 0)));
        assertEquals(3, liquid.getCells().size());
    }

    @Test
    void remove_removesCellFromSystem() {
        Cell source = field.getCell(1, 1);
        liquid.addSource(source);

        liquid.remove(source);

        assertFalse(liquid.contains(source));
        assertTrue(liquid.getCells().isEmpty());
    }

    @Test
    void contains_returnsFalseForCellOutsideSystem() {
        liquid.addSource(field.getCell(1, 1));

        assertFalse(liquid.contains(field.getCell(0, 0)));
    }

    @Test
    void addSource_cellWithWall_doesNotAdd() {
        Cell cell = field.getCell(1, 1);
        cell.putUnit(new Wall());

        liquid.addSource(cell);

        assertFalse(liquid.contains(cell));
        assertTrue(liquid.getCells().isEmpty());
    }

    @Test
    void addSource_sameCellTwice_doesNotDuplicate() {
        Cell source = field.getCell(1, 1);

        liquid.addSource(source);
        liquid.addSource(source);

        assertEquals(1, liquid.getCells().size());
    }
}
