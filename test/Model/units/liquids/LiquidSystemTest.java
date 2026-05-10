package Model.units.liquids;

import Model.events.liquids.LiquidSystemActionListener;
import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.units.solid.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
    void addSource_addsCellAndMarksCellWithThisLiquidSystem() {
        Cell source = field.getCell(1, 1);

        liquid.addSource(source);

        assertTrue(liquid.contains(source));
        assertSame(liquid, source.getLiquidSystem());
        assertEquals(Set.of(source), liquid.getCells());
    }

    @Test
    void addSource_nullCell_doesNothing() {
        assertDoesNotThrow(() -> liquid.addSource(null));

        assertTrue(liquid.getCells().isEmpty());
    }

    @Test
    void addSource_cellWithWall_doesNotAddAndDoesNotMarkCell() {
        Cell cell = field.getCell(1, 1);
        cell.putUnit(new Wall());

        liquid.addSource(cell);

        assertFalse(liquid.contains(cell));
        assertNull(cell.getLiquidSystem());
        assertTrue(liquid.getCells().isEmpty());
    }

    @Test
    void addSource_sameCellTwice_doesNotDuplicate() {
        Cell source = field.getCell(1, 1);

        liquid.addSource(source);
        liquid.addSource(source);

        assertEquals(1, liquid.getCells().size());
        assertTrue(liquid.contains(source));
    }

    @Test
    void spread_fromCenter_spreadsToFourOrthogonalNeighbours() {
        Cell source = field.getCell(1, 1);
        liquid.addSource(source);

        liquid.spread();

        assertTrue(liquid.contains(field.getCell(1, 1)));
        assertTrue(liquid.contains(field.getCell(0, 1)));
        assertTrue(liquid.contains(field.getCell(2, 1)));
        assertTrue(liquid.contains(field.getCell(1, 0)));
        assertTrue(liquid.contains(field.getCell(1, 2)));

        assertEquals(5, liquid.getCells().size());

        assertSame(liquid, field.getCell(0, 1).getLiquidSystem());
        assertSame(liquid, field.getCell(2, 1).getLiquidSystem());
        assertSame(liquid, field.getCell(1, 0).getLiquidSystem());
        assertSame(liquid, field.getCell(1, 2).getLiquidSystem());
    }

    @Test
    void spread_doesNotSpreadThroughWalls() {
        Cell source = field.getCell(1, 1);
        Cell blocked = field.getCell(0, 1);

        liquid.addSource(source);
        blocked.putUnit(new Wall());

        liquid.spread();

        assertFalse(liquid.contains(blocked));
        assertNull(blocked.getLiquidSystem());

        assertTrue(liquid.contains(field.getCell(2, 1)));
        assertTrue(liquid.contains(field.getCell(1, 0)));
        assertTrue(liquid.contains(field.getCell(1, 2)));

        assertEquals(4, liquid.getCells().size());
    }

    @Test
    void spread_fromCorner_doesNotGoOutsideField() {
        Cell source = field.getCell(0, 0);
        liquid.addSource(source);

        liquid.spread();

        assertTrue(liquid.contains(field.getCell(0, 0)));
        assertTrue(liquid.contains(field.getCell(0, 1)));
        assertTrue(liquid.contains(field.getCell(1, 0)));

        assertEquals(3, liquid.getCells().size());
    }

    @Test
    void spread_doesNotDuplicateCellReachedFromTwoSources() {
        liquid.addSource(field.getCell(1, 0));
        liquid.addSource(field.getCell(1, 2));

        liquid.spread();

        Cell middle = field.getCell(1, 1);

        assertTrue(liquid.contains(middle));
        assertEquals(7, liquid.getCells().size());
    }

    @Test
    void spread_doesNotNotifyConflictForEmptyCells() {
        RecordingLiquidListener listener = new RecordingLiquidListener();
        liquid.addLiquidSystemActionListener(listener);

        liquid.addSource(field.getCell(1, 1));

        liquid.spread();

        assertTrue(listener.conflicts.isEmpty());
    }

    @Test
    void spread_whenNeighbourBelongsToOtherLiquid_firesConflictAndDoesNotOccupyCell() {
        LiquidSystem other = createAnotherLiquid();
        Cell source = field.getCell(1, 1);
        Cell conflictCell = field.getCell(1, 2);

        liquid.addSource(source);
        other.addSource(conflictCell);

        RecordingLiquidListener listener = new RecordingLiquidListener();
        liquid.addLiquidSystemActionListener(listener);

        liquid.spread();

        assertTrue(listener.conflicts.contains(conflictCell));
        assertFalse(liquid.contains(conflictCell));
        assertSame(other, conflictCell.getLiquidSystem());
    }

    @Test
    void spread_whenConflictAppears_listenerCanResolveIt() {
        LiquidSystem other = createAnotherLiquid();

        Cell source = field.getCell(1, 1);
        Cell conflictCell = field.getCell(1, 2);

        liquid.addSource(source);
        other.addSource(conflictCell);

        liquid.addLiquidSystemActionListener(cell -> {
            cell.setLiquidSystem(null);
            cell.putUnit(new Wall());
        });

        liquid.spread();

        assertFalse(liquid.contains(conflictCell));
        assertNull(conflictCell.getLiquidSystem());
        assertNotNull(conflictCell.getUnit(Wall.class));
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
    void getCells_returnsUnmodifiableCopy() {
        Cell source = field.getCell(1, 1);
        liquid.addSource(source);

        Set<Cell> cells = liquid.getCells();

        assertThrows(UnsupportedOperationException.class, () -> cells.add(field.getCell(0, 0)));
        assertEquals(1, liquid.getCells().size());
    }

    @Test
    void contains_returnsFalseForCellOutsideSystem() {
        liquid.addSource(field.getCell(1, 1));

        assertFalse(liquid.contains(field.getCell(0, 0)));
    }

    private LiquidSystem createAnotherLiquid() {
        if (liquid instanceof Water) {
            return new Lava();
        }
        return new Water();
    }

    private static class RecordingLiquidListener implements LiquidSystemActionListener {
        private final ArrayList<Cell> conflicts = new ArrayList<>();

        @Override
        public void conflictAppeared(Cell cell) {
            conflicts.add(cell);
        }
    }
}
