package Model.units.impassable;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.AbstractUnitTest;
import Model.units.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class IronBlockTest extends AbstractUnitTest<IronBlock> {

    @Override
    protected IronBlock createUnit() {
        return new IronBlock();
    }

    @Test
    void canBelongTo_emptyCell_returnsTrue() {
        assertTrue(unit.canBelongTo(cell));
    }

    @Test
    void canBelongTo_cellWithSolid_returnsFalse() {
        cell.putUnit(new Wall());

        assertFalse(unit.canBelongTo(cell));
    }

    @Test
    void canBePushedTo_freeCell_returnsTrue() {
        Cell target = field.getCell(1, 2);

        assertTrue(unit.canBePushedTo(target));
    }

    @Test
    void canBePushedTo_null_returnsFalse() {
        assertFalse(unit.canBePushedTo(null));
    }

    @Test
    void canBePushedTo_cellWithWall_returnsFalse() {
        Cell target = field.getCell(1, 2);
        target.putUnit(new Wall());

        assertFalse(unit.canBePushedTo(target));
    }

    @Test
    void canBePushedTo_cellWithAnotherIronBlock_returnsFalse() {
        Cell target = field.getCell(1, 2);
        target.putUnit(new IronBlock());

        assertFalse(unit.canBePushedTo(target));
    }
}
