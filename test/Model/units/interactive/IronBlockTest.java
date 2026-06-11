package Model.units.interactive;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.AbstractUnitTest;
import Model.units.solid.Wall;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

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

        assertTrue(unit.canBelongTo(target));
    }

    @Test
    void canBePushedTo_null_returnsFalse() {
        assertFalse(unit.canBelongTo(null));
    }

    @Test
    void canBePushedTo_cellWithWall_returnsFalse() {
        Cell target = field.getCell(1, 2);
        target.putUnit(new Wall());

        assertFalse(unit.canBelongTo(target));
    }

    @Test
    void canBePushedTo_cellWithAnotherIronBlock_returnsFalse() {
        Cell target = field.getCell(1, 2);
        target.putUnit(new IronBlock());

        assertFalse(unit.canBelongTo(target));
    }

    @Test
    void push_beforeBlockIsPlaced_mustThrow() {
        IronBlock block = new IronBlock();

        assertThrows(
                IllegalStateException.class,
                () -> block.interact(Direction.EAST)
        );
    }

    @Test
    void push_success_firesIronBlockMovedOnce_andListenerSeesMovedState() {
        Cell start = field.getCell(0, 0);
        Cell destination = field.getCell(1, 0);

        IronBlock block = new IronBlock();

        assertTrue(start.putUnit(block));

        AtomicInteger calls = new AtomicInteger();

        block.addIronBlockActionListener(() -> {
            calls.incrementAndGet();

            // Проверка В МОМЕНТ события.
            assertSame(destination, block.owner());
            assertSame(block, destination.getUnit(IronBlock.class));
            assertNull(start.getUnit(IronBlock.class));
            assertTrue(block.isActive());
            assertFalse(block.isDestroyed());
        });

        // Снимок ДО события.
        assertSame(start, block.owner());
        assertSame(block, start.getUnit(IronBlock.class));
        assertNull(destination.getUnit(IronBlock.class));
        assertEquals(0, calls.get());

        block.interact(Direction.EAST);

        // Снимок ПОСЛЕ события.
        assertEquals(1, calls.get());
        assertSame(destination, block.owner());
        assertSame(block, destination.getUnit(IronBlock.class));
        assertNull(start.getUnit(IronBlock.class));
    }

    @Test
    void push_whenDestinationBlocked_doesNotFireIronBlockMoved() {
        Cell start = field.getCell(0, 0);
        Cell destination = field.getCell(1, 0);

        IronBlock block = new IronBlock();
        Wall wall = new Wall();

        assertTrue(start.putUnit(block));
        assertTrue(destination.putUnit(wall));

        AtomicInteger calls = new AtomicInteger();

        block.addIronBlockActionListener(calls::incrementAndGet);

        block.interact(Direction.EAST);

        assertEquals(0, calls.get());
        assertSame(start, block.owner());
        assertSame(block, start.getUnit(IronBlock.class));
        assertSame(wall, destination.getUnit(Wall.class));
    }
}
