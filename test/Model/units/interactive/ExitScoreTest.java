package Model.units.interactive;

import Model.events.collectable.ExitScoreActionListener;
import Model.units.AbstractUnitTest;
import Model.units.Exit;
import Model.units.solid.Wall;
import org.junit.jupiter.api.Test;

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

        unit.collect();

        assertNull(cell.getUnit(ExitScore.class));
        assertNull(unit.owner());
    }

    @Test
    void collect_firesEvent() {
        AtomicBoolean fired = new AtomicBoolean(false);

        unit.addExitScoreListener(token -> fired.set(true));

        cell.putUnit(unit);

        unit.collect();

        assertTrue(fired.get());
    }

    @Test
    void removeListener_listenerDoesNotReceiveEvent() {
        AtomicBoolean fired = new AtomicBoolean(false);

        ExitScoreActionListener listener =
                token -> fired.set(true);

        unit.addExitScoreListener(listener);
        unit.removeExitScoreListener(listener);

        cell.putUnit(unit);

        unit.collect();

        assertFalse(fired.get());
    }

    @Test
    void addSameListenerTwice_eventFiresOnlyOnce() {
        AtomicInteger calls = new AtomicInteger();

        ExitScoreActionListener listener =
                token -> calls.incrementAndGet();

        unit.addExitScoreListener(listener);
        unit.addExitScoreListener(listener);

        cell.putUnit(unit);

        unit.collect();

        assertEquals(1, calls.get());
    }

    @Test
    void exitUnlocksAfterCollectingSingleToken() {
        Exit exit = new Exit();

        exit.setLeftScores(1);

        unit.addExitScoreListener(exit.getTokenListener());

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

        token1.addExitScoreListener(exit.getTokenListener());
        token2.addExitScoreListener(exit.getTokenListener());
        token3.addExitScoreListener(exit.getTokenListener());

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

        unit.addExitScoreListener(
                token -> calls.incrementAndGet()
        );

        cell.putUnit(unit);

        unit.collect();
        unit.collect();

        assertEquals(1, calls.get());
    }

    @Test
    void collect_twice_doesNotDecreaseExitTwice() {
        Exit exit = new Exit();

        exit.setLeftScores(1);

        unit.addExitScoreListener(exit.getTokenListener());

        cell.putUnit(unit);

        unit.collect();
        unit.collect();

        assertEquals(0, exit.getLeftScores());
    }
}
