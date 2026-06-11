package Model.units.interactive;

import Model.events.collectable.ExitScoreActionListener;
import Model.gamefield.Direction;
import Model.units.AbstractUnitTest;
import Model.units.Exit;
import Model.units.solid.Wall;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/*
 ----- аспекты тестировния по которым я проверяла -----
- корректное порождение (корректное порождение в пустой клетке, некорректное в стене, некорректное с другим жетоном на одной клетке)
- корректный сбор жетонов игроком (рядом создать игрока и жетон и проверить, как игрок его собирает (жетон должен удалиться с клетки и в целом надо проверить, взялся ли жетон)
- проверка ситуации где несколько жетонов (корректно ли собираются)
- проверка активации выхода с одним жетоном (проверяем выход (должен быть заблокирован), игрок берет жетон и проверяем выход снова (выход должен быть разблокирован))
- проверка активации выхода с тремя жетонами (то же самое, но надо проверять разблокирован ли выход после каждого забора жетона (надо чтобы он разблокировался только когда будут собраны все жетоны)
 */

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

        unit.interact(Direction.EAST);

        assertNull(cell.getUnit(ExitScore.class));
        assertNull(unit.owner());
    }

    @Test
    void collect_firesEvent() {
        AtomicBoolean fired = new AtomicBoolean(false);

        unit.addModelExitScoreListener(token -> fired.set(true));

        cell.putUnit(unit);

        unit.interact(Direction.EAST);

        assertTrue(fired.get());
    }

    @Test
    void removeListener_listenerDoesNotReceiveEvent() {
        AtomicBoolean fired = new AtomicBoolean(false);

        ExitScoreActionListener listener =
                token -> fired.set(true);

        unit.addModelExitScoreListener(listener);
        unit.removeModelExitScoreListener(listener);

        cell.putUnit(unit);

        unit.interact(Direction.EAST);

        assertFalse(fired.get());
    }

    @Test
    void addSameListenerTwice_eventFiresOnlyOnce() {
        AtomicInteger calls = new AtomicInteger();

        ExitScoreActionListener listener =
                token -> calls.incrementAndGet();

        unit.addModelExitScoreListener(listener);
        unit.addModelExitScoreListener(listener);

        cell.putUnit(unit);

        unit.interact(Direction.EAST);

        assertEquals(1, calls.get());
    }

    @Test
    void exitUnlocksAfterCollectingSingleScore() {
        Exit exit = new Exit();
        exit.addExitScore(unit);
        unit.addModelExitScoreListener(exit.getExitScoreListener());

        cell.putUnit(unit);

        unit.interact(Direction.EAST);

        assertEquals(0, exit.getLeftScores().size());
    }

    @Test
    void exitUnlocksOnlyAfterAllScoresCollected() {
        Exit exit = new Exit();

        ExitScore score1 = new ExitScore();
        ExitScore score2 = new ExitScore();
        ExitScore score3 = new ExitScore();

        exit.addExitScore(score1);
        exit.addExitScore(score2);
        exit.addExitScore(score3);

        score1.addModelExitScoreListener(exit.getExitScoreListener());
        score2.addModelExitScoreListener(exit.getExitScoreListener());
        score3.addModelExitScoreListener(exit.getExitScoreListener());

        score1.interact(Direction.EAST);

        assertEquals(2, exit.getLeftScores().size());

        score2.interact(Direction.EAST);

        assertEquals(1, exit.getLeftScores().size());

        score3.interact(Direction.EAST);

        assertEquals(0, exit.getLeftScores().size());
    }

    @Test
    void collect_twice_firesEventOnlyOnce() {
        AtomicInteger calls = new AtomicInteger();

        unit.addModelExitScoreListener(
                token -> calls.incrementAndGet()
        );

        cell.putUnit(unit);

        unit.interact(Direction.EAST);
        unit.interact(Direction.EAST);

        assertEquals(1, calls.get());
    }

    @Test
    void collect_twice_doesNotDecreaseExitTwice() {
        Exit exit = new Exit();

        exit.addExitScore(unit);
        unit.addModelExitScoreListener(exit.getExitScoreListener());

        cell.putUnit(unit);

        unit.interact(Direction.EAST);
        unit.interact(Direction.EAST);

        assertEquals(0, exit.getLeftScores().size());
    }

    @Test
    void collect_processesModelExitListenerBeforeViewListener_evenIfViewWasRegisteredFirst() {
        Exit exit = new Exit();
        ExitScore score = new ExitScore();

        cell = field.getCell(0, 0);

        assertTrue(cell.putUnit(score));

        exit.deactivate();
        exit.addExitScore(score);

        List<String> events = new ArrayList<>();

        // View подписан ПЕРВЫМ, но должен вызваться ПОСЛЕ модели.
        score.addViewExitScoreListener(collectedScore -> {
            events.add("view:ExitWidget");

            assertSame(score, collectedScore);

            // View должен видеть уже обработанную вычислительную модель.
            assertTrue(exit.isActive());
            assertTrue(exit.getLeftScores().isEmpty());

            assertTrue(score.isDestroyed());
            assertNull(score.owner());
            assertNull(cell.getUnit(ExitScore.class));
        });

        exit.addUnitActivationListener(() -> {
            events.add("model:ExitActivated");

            assertTrue(exit.isActive());
            assertTrue(exit.getLeftScores().isEmpty());
            assertTrue(score.isDestroyed());
            assertNull(score.owner());
        });

        // Model подписан ВТОРЫМ, но должен обработаться ПЕРВЫМ.
        score.addModelExitScoreListener(exit.getExitScoreListener());

        assertFalse(exit.isActive());
        assertEquals(List.of(score), exit.getLeftScores());
        assertSame(cell, score.owner());

        score.interact(Direction.EAST);

        assertEquals(
                List.of(
                        "model:ExitActivated",
                        "view:ExitWidget"
                ),
                events
        );

        assertTrue(exit.isActive());
        assertTrue(exit.getLeftScores().isEmpty());
        assertTrue(score.isDestroyed());
        assertNull(score.owner());
    }
}
