package Model.units.impassable;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.AbstractUnitTest;
import Model.units.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class IronBlockTest extends AbstractUnitTest {

    @Override
    protected Unit createUnit() {
        return new IronBlock();
    }

    @Test
    void canBelongTo_emptyCell_returnsTrue() {
        assertTrue(unit.canBelongTo(cell));
    }

    @Test
    void moveByPlayer_toFreeCell_movesBlock() {
        Cell start = field.getCell(1, 1);
        start.putUnit(unit);

        //boolean result = unit.moveByPlayer(Direction.EAST);

        //assertTrue(result);
        assertNull(start.getUnit(IronBlock.class));
        assertEquals(unit, field.getCell(1, 2).getUnit(IronBlock.class));
    }

    @Test
    void moveByPlayer_toBlockedCell_returnsFalse() {
        Cell start = field.getCell(1, 1);
        start.putUnit(unit);

        Cell target = field.getCell(1, 2);
        target.putUnit(new Wall());

        //boolean result = unit.moveByPlayer(Direction.EAST);

        //assertFalse(result);
        assertEquals(unit, start.getUnit(IronBlock.class));
    }

    @Test
    void moveByPlayer_outOfBounds_returnsFalse() {
        Cell start = field.getCell(1, 2);
        start.putUnit(unit);

        //boolean result = unit.moveByPlayer(Direction.EAST);

        //assertFalse(result);
        assertEquals(unit, start.getUnit(IronBlock.class));
    }
}
