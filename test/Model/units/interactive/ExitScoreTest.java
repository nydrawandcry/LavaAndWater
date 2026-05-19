package Model.units.interactive;

import Model.events.collectable.ExitScoreActionListener;
import Model.units.AbstractUnitTest;
import Model.units.Exit;
import Model.units.solid.Wall;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ExitScoreTest extends AbstractUnitTest<ExitScore> {
    @Override
    protected ExitScore createUnit() {
        return new ExitScore();
    }

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
    void canBelongTo_cellWithAnotherToken_returnsFalse() {
        cell.putUnit(new ExitScore());

        assertFalse(unit.canBelongTo(cell));
    }

    @Test
    void canBelongTo_null_returnsFalse() {
        assertFalse(unit.canBelongTo(null));
    }

    @Test
    void collect_removesTokenFromCell() {
        cell.putUnit(unit);

        unit.collect();

        assertNull(cell.getUnit(ExitScore.class));
        assertNull(unit.owner());
    }

    @Test
    void collect_firesEvent() {
        AtomicBoolean fired = new AtomicBoolean(false);

        unit.addExitTokenListener(token -> fired.set(true));

        cell.putUnit(unit);

        unit.collect();

        assertTrue(fired.get());
    }

    @Test
    void removeListener_listenerDoesNotReceiveEvent() {
        AtomicBoolean fired = new AtomicBoolean(false);

        ExitScoreActionListener listener =
                token -> fired.set(true);

        unit.addExitTokenListener(listener);
        unit.removeExitTokenListener(listener);

        cell.putUnit(unit);

        unit.collect();

        assertFalse(fired.get());
    }

    @Test
    void addSameListenerTwice_eventFiresOnlyOnce() {
        AtomicInteger calls = new AtomicInteger();

        ExitScoreActionListener listener =
                token -> calls.incrementAndGet();

        unit.addExitTokenListener(listener);
        unit.addExitTokenListener(listener);

        cell.putUnit(unit);

        unit.collect();

        assertEquals(1, calls.get());
    }

    @Test
    void exitUnlocksAfterCollectingSingleToken() {
        Exit exit = new Exit();

        exit.setLeftScores(1);

        unit.addExitTokenListener(exit.getTokenListener());

        cell.putUnit(unit);

        unit.collect();

        assertEquals(0, exit.getLeftScores());
    }

    @Test
    void exitUnlocksOnlyAfterAllTokensCollected() {
        Exit exit = new Exit();

        exit.setLeftScores(3);

        ExitScore token1 = new ExitScore();
        ExitScore token2 = new ExitScore();
        ExitScore token3 = new ExitScore();

        token1.addExitTokenListener(exit.getTokenListener());
        token2.addExitTokenListener(exit.getTokenListener());
        token3.addExitTokenListener(exit.getTokenListener());

        token1.collect();

        assertEquals(2, exit.getLeftScores());

        token2.collect();

        assertEquals(1, exit.getLeftScores());

        token3.collect();

        assertEquals(0, exit.getLeftScores());
    }

    @Test
    void collect_twice_firesEventOnlyOnce() {
        AtomicInteger calls = new AtomicInteger();

        unit.addExitTokenListener(
                token -> calls.incrementAndGet()
        );

        cell.putUnit(unit);

        unit.collect();
        unit.collect();

        assertEquals(1, calls.get());
    }
}
