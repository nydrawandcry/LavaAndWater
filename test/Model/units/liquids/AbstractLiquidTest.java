package Model.units.liquids;

import Model.gamefield.Cell;
import Model.units.AbstractUnitTest;
import Model.units.impassable.Wall;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public abstract class AbstractLiquidTest<T extends Liquid> extends AbstractUnitTest<T> {

    @Test
    void canBelongTo_emptyCell_returnsTrue() {
        assertTrue(unit.canBelongTo(cell));
    }

    @Test
    void canBelongTo_cellWithWall_returnsFalse() {
        cell.putUnit(new Wall());

        assertFalse(unit.canBelongTo(cell));
    }

    @Test
    void canBelongTo_cellWithSameLiquid_returnsFalse() {
        cell.putUnit(createUnit());

        assertFalse(unit.canBelongTo(cell));
    }

    @Test
    void expand_spreadsToNeighbourCells() {
        Cell center = field.getCell(1, 1);
        center.putUnit(unit);

        unit.expand(field);

        assertNotNull(field.getCell(0, 1).getUnit(unit.getClass()));
        assertNotNull(field.getCell(2, 1).getUnit(unit.getClass()));
        assertNotNull(field.getCell(1, 0).getUnit(unit.getClass()));
        assertNotNull(field.getCell(1, 2).getUnit(unit.getClass()));
    }

    @Test
    void expand_doesNotSpreadThroughWalls() {
        Cell center = field.getCell(1, 1);
        center.putUnit(unit);

        field.getCell(0, 1).putUnit(new Wall());

        unit.expand(field);

        assertNull(field.getCell(0, 1).getUnit(unit.getClass()));
        assertNotNull(field.getCell(2, 1).getUnit(unit.getClass()));
    }
}
