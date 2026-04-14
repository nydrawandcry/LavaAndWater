package Model.units.impassable;

import Model.units.AbstractUnitTest;
import Model.units.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WallTest extends AbstractUnitTest {

    @Override
    protected Unit createUnit() {
        return new Wall();
    }

    @Test
    void canBelongTo_emptyCell_returnsTrue() {
        assertTrue(unit.canBelongTo(cell));
    }

    @Test
    void canBelongTo_cellWithImpassable_returnsFalse() {
        cell.putUnit(new Wall());

        assertFalse(unit.canBelongTo(cell));
    }
}
