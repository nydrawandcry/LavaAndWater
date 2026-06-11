package Model.units;

import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.gamefield.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public abstract class AbstractUnitTest<T extends Unit> {

    protected Gamefield field;
    protected Cell cell;
    protected T unit;

    @BeforeEach
    void setUp() {
        field = new Gamefield(3, 3);
        cell = field.getCell(1, 1);
        unit = createUnit();
    }

    protected abstract T createUnit();

    @Test
    void setOwnerTest() {
        cell.putUnit(unit);

        assertEquals(cell, unit.owner());
    }

    @Test
    void removeOwnerTest() {
        cell.putUnit(unit);
        cell.extractUnit(unit);

        assertNull(unit.owner());
    }

    @Test
    void activateTest() {
        unit.activate();

        assertTrue(unit.isActive());
    }

    @Test
    void deactivateTest() {
        unit.activate();
        unit.deactivate();

        assertFalse(unit.isActive());
    }
}
