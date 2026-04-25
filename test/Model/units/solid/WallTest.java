package Model.units.solid;

import Model.units.AbstractUnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WallTest extends AbstractUnitTest<Wall> {

    @Override
    protected Wall createUnit() {
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
