package Model.units;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.impassable.IronBlock;
import Model.units.impassable.Wall;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class PlayerTest extends AbstractUnitTest<Player>{

    @Override
    protected Player createUnit() {
        return new Player();
    }

    @Test
    void moveTo_freeCell_movesPlayer() {
        Cell start = field.getCell(1, 1);
        start.putUnit(unit);

        boolean result = unit.moveTo(Direction.EAST);

        assertTrue(result);
        assertNull(start.getUnit(Player.class));
        assertEquals(unit, field.getCell(1, 2).getUnit(Player.class));
    }

    @Test
    void moveTo_wall_returnsFalse() {
        Cell start = field.getCell(1, 1);
        start.putUnit(unit);

        field.getCell(1, 2).putUnit(new Wall());

        boolean result = unit.moveTo(Direction.EAST);

        assertFalse(result);
        assertEquals(unit, start.getUnit(Player.class));
    }

    @Test
    void moveTo_pushesIronBlock_ifPossible() {
        Cell start = field.getCell(1, 0);
        start.putUnit(unit);

        IronBlock block = new IronBlock();
        field.getCell(1, 1).putUnit(block);

        boolean result = unit.moveTo(Direction.EAST);

        assertTrue(result);
        assertEquals(unit, field.getCell(1, 1).getUnit(Player.class));
        assertEquals(block, field.getCell(1, 2).getUnit(IronBlock.class));
    }

    @Test
    void moveTo_fails_ifIronBlockCannotBePushed() {
        Cell start = field.getCell(1, 0);
        start.putUnit(unit);

        IronBlock block = new IronBlock();
        field.getCell(1, 1).putUnit(block);
        field.getCell(1, 2).putUnit(new Wall());

        boolean result = unit.moveTo(Direction.EAST);

        assertFalse(result);
        assertEquals(unit, field.getCell(1, 0).getUnit(Player.class));
        assertEquals(block, field.getCell(1, 1).getUnit(IronBlock.class));
    }

    @Test
    void kill_setsPlayerDeadAndInactive() {
        unit.activate();

        unit.kill();

        assertFalse(unit.isAlive());
        assertFalse(unit.isActive());
    }
}
