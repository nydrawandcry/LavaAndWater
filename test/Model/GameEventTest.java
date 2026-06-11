package Model;

import Model.events.game.GameActionListener;
import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.units.Exit;
import Model.gamefield.Unit;
import Model.units.interactive.Boat;
import Model.units.interactive.ExitScore;
import Model.units.interactive.IronBlock;
import Model.units.interactive.Player;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;
import Model.units.solid.Wall;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class GameEventTest {

    @Test
    void start_buildsInitialWholeGameSnapshot_andDoesNotFireGameEvents() {
        Game game = new Game();
        List<String> gameEvents = new ArrayList<>();

        game.addGameActionListener(new GameActionListener() {
            @Override
            public void gameIsWon() {
                gameEvents.add("gameIsWon");
            }

            @Override
            public void gameIsLost() {
                gameEvents.add("gameIsLost");
            }
        });

        assertFalse(readBoolean(game, "_isWon"));
        assertFalse(readBoolean(game, "_isLost"));
        assertFalse(game.isOver());
        assertNull(game.getField());

        game.start();

        assertTrue(gameEvents.isEmpty());
        assertInitialGameSnapshot(game);
    }

    @Test
    void playerInExit_winsGame_deactivatesEveryUnitAndFieldBeforeGameWinEvent() {
        Game game = new Game();
        game.start();

        assertInitialGameSnapshot(game);
        assertGameEndEventSnapshot(game, true, () -> game.getPlayerListener().playerInExit());
    }

    @Test
    void playerInLava_losesGame_deactivatesEveryUnitAndFieldBeforeGameLostEvent() {
        Game game = new Game();
        game.start();

        assertInitialGameSnapshot(game);
        assertGameEndEventSnapshot(game, false, () -> game.getPlayerListener().playerInLava());
    }

    @Test
    void playerInWall_losesGame_deactivatesEveryUnitAndFieldBeforeGameLostEvent() {
        Game game = new Game();
        game.start();

        assertInitialGameSnapshot(game);
        assertGameEndEventSnapshot(game, false, () -> game.getPlayerListener().playerInWall());
    }

    @Test
    void startAfterFinishedGame_resetsFlagsAndCreatesNewActiveField() {
        Game game = new Game();
        game.start();

        Gamefield firstField = game.getField();

        game.getPlayerListener().playerInLava();

        assertSame(firstField, game.getField());
        assertTrue(game.isOver());
        assertFalse(firstField.isActive());
        assertTrue(readBoolean(game, "_isLost"));
        assertFalse(readBoolean(game, "_isWon"));

        game.start();

        assertNotSame(firstField, game.getField());
        assertInitialGameSnapshot(game);
        assertFalse(readBoolean(game, "_isLost"));
        assertFalse(readBoolean(game, "_isWon"));
        assertFalse(game.isOver());

        // Старое поле остается завершенным снимком предыдущей партии.
        assertFalse(firstField.isActive());
    }

    @Test
    void playerReachEventBeforeStart_mustThrowControlledIllegalStateException() {
        Game game = new Game();

        assertNull(game.getField());
        assertFalse(game.isOver());

        assertThrows(IllegalStateException.class, () -> game.getPlayerListener().playerInExit());
        assertThrows(IllegalStateException.class, () -> game.getPlayerListener().playerInLava());
        assertThrows(IllegalStateException.class, () -> game.getPlayerListener().playerInWall());
    }

    @Test
    void alreadyLostGame_mustIgnoreLaterWinReachEvent_andMustNotPublishGameIsWon() {
        Game game = new Game();
        List<String> events = new ArrayList<>();

        game.start();

        game.addGameActionListener(new GameActionListener() {
            @Override
            public void gameIsWon() {
                events.add("gameIsWon");
            }

            @Override
            public void gameIsLost() {
                events.add("gameIsLost");
            }
        });

        game.getPlayerListener().playerInLava();

        assertEquals(List.of("gameIsLost"), events);
        assertTrue(game.isOver());
        assertTrue(readBoolean(game, "_isLost"));
        assertFalse(readBoolean(game, "_isWon"));

        game.getPlayerListener().playerInExit();

        assertEquals(List.of("gameIsLost"), events);
        assertTrue(game.isOver());
        assertTrue(readBoolean(game, "_isLost"));
        assertFalse(readBoolean(game, "_isWon"));
    }

    @Test
    void alreadyWonGame_mustIgnoreLaterLoseReachEvent_andMustNotPublishGameIsLost() {
        Game game = new Game();
        List<String> events = new ArrayList<>();

        game.start();

        game.addGameActionListener(new GameActionListener() {
            @Override
            public void gameIsWon() {
                events.add("gameIsWon");
            }

            @Override
            public void gameIsLost() {
                events.add("gameIsLost");
            }
        });

        game.getPlayerListener().playerInExit();

        assertEquals(List.of("gameIsWon"), events);
        assertTrue(game.isOver());
        assertFalse(readBoolean(game, "_isLost"));
        assertTrue(readBoolean(game, "_isWon"));

        game.getPlayerListener().playerInLava();

        assertEquals(List.of("gameIsWon"), events);
        assertTrue(game.isOver());
        assertFalse(readBoolean(game, "_isLost"));
        assertTrue(readBoolean(game, "_isWon"));
    }

    private static void assertGameEndEventSnapshot(Game game, boolean expectedWin, Runnable trigger) {
        Gamefield field = game.getField();
        List<Unit> units = allUnits(field);
        List<String> events = new ArrayList<>();
        AtomicInteger unitActivationEvents = new AtomicInteger();

        assertFalse(game.isOver());
        assertFalse(readBoolean(game, "_isWon"));
        assertFalse(readBoolean(game, "_isLost"));
        assertTrue(field.isActive());
        assertFalse(units.isEmpty());

        for (Unit unit : units) {
            unit.addUnitActivationListener(() -> {
                int index = unitActivationEvents.incrementAndGet();
                events.add("unitDeactivated#" + index);

                // winTheGame/loseTheGame сначала меняет флаги Game,
                // и только потом деактивирует поле.
                assertEquals(expectedWin, readBoolean(game, "_isWon"));
                assertEquals(!expectedWin, readBoolean(game, "_isLost"));
                assertTrue(game.isOver());

                // Во время событий юнитов поле еще активно:
                // Gamefield.deactivate() ставит _isActive=false после цикла по юнитам.
                assertTrue(field.isActive());

                assertFalse(unit.isActive());
                assertFalse(unit.isDestroyed());
                assertNotNull(unit.owner());
                assertSame(field, unit.owner().getOwner());
            });
        }

        field.addGamefieldActivationListener(() -> {
            events.add("fieldDeactivated");

            // Событие поля должно идти после всех событий юнитов.
            assertEquals(units.size(), unitActivationEvents.get());
            assertFalse(field.isActive());
            assertAllUnitsHaveActiveState(units, false);

            assertEquals(expectedWin, readBoolean(game, "_isWon"));
            assertEquals(!expectedWin, readBoolean(game, "_isLost"));
            assertTrue(game.isOver());
        });

        game.addGameActionListener(new GameActionListener() {
            @Override
            public void gameIsWon() {
                events.add("gameIsWon");

                assertTrue(expectedWin);
                assertEquals(units.size(), unitActivationEvents.get());
                assertEquals("fieldDeactivated", events.get(events.size() - 2));

                // GameActionListener вызывается после полной деактивации модели.
                assertFalse(field.isActive());
                assertAllUnitsHaveActiveState(units, false);
                assertTrue(readBoolean(game, "_isWon"));
                assertFalse(readBoolean(game, "_isLost"));
                assertTrue(game.isOver());
            }

            @Override
            public void gameIsLost() {
                events.add("gameIsLost");

                assertFalse(expectedWin);
                assertEquals(units.size(), unitActivationEvents.get());
                assertEquals("fieldDeactivated", events.get(events.size() - 2));

                // GameActionListener вызывается после полной деактивации модели.
                assertFalse(field.isActive());
                assertAllUnitsHaveActiveState(units, false);
                assertFalse(readBoolean(game, "_isWon"));
                assertTrue(readBoolean(game, "_isLost"));
                assertTrue(game.isOver());
            }
        });

        trigger.run();

        assertEquals(units.size(), unitActivationEvents.get());
        assertEquals(units.size() + 2, events.size());
        assertEquals("fieldDeactivated", events.get(events.size() - 2));
        assertEquals(expectedWin ? "gameIsWon" : "gameIsLost", events.get(events.size() - 1));

        assertFalse(field.isActive());
        assertAllUnitsHaveActiveState(units, false);
        assertEquals(expectedWin, readBoolean(game, "_isWon"));
        assertEquals(!expectedWin, readBoolean(game, "_isLost"));
        assertTrue(game.isOver());
    }

    private static void assertInitialGameSnapshot(Game game) {
        assertFalse(readBoolean(game, "_isWon"));
        assertFalse(readBoolean(game, "_isLost"));
        assertFalse(game.isOver());

        Gamefield field = game.getField();

        assertNotNull(field);
        assertEquals(11, field.getHeight());
        assertEquals(16, field.getWidth());
        assertTrue(field.isActive());
        assertFalse(field.isDestroyed());

        assertBoundaryWalls(field);

        int[][] innerWalls = {
                {4, 9}, {4, 8}, {4, 7},
                {8, 9}, {8, 8}, {8, 7},
                {5, 7}, {7, 7},
                {7, 2}, {8, 2}, {9, 2}, {10, 2},
                {11, 2}, {12, 2}, {13, 2}, {14, 2},
                {13, 3}
        };

        for (int[] position : innerWalls) {
            assertUnitAt(field, position[0], position[1], Wall.class, true);
        }

        Player player = assertUnitAt(field, 2, 8, Player.class, true);
        IronBlock ironBlock = assertUnitAt(field, 6, 6, IronBlock.class, true);
        Boat boat = assertUnitAt(field, 3, 7, Boat.class, true);

        Exit exit = assertUnitAt(field, 14, 3, Exit.class, false);
        ExitScore firstScore = assertUnitAt(field, 2, 6, ExitScore.class, true);
        ExitScore secondScore = assertUnitAt(field, 12, 8, ExitScore.class, true);

        assertFalse(player.isDestroyed());
        assertFalse(ironBlock.isDestroyed());
        assertFalse(boat.isDestroyed());
        assertFalse(exit.isDestroyed());
        assertFalse(firstScore.isDestroyed());
        assertFalse(secondScore.isDestroyed());

        assertEquals(2, exit.getLeftScores().size());
        assertTrue(exit.getLeftScores().contains(firstScore));
        assertTrue(exit.getLeftScores().contains(secondScore));

        Cell lavaSource = field.getCell(11, 1);
        assertNotNull(lavaSource.getLiquidSystem());
        assertTrue(lavaSource.getLiquidSystem() instanceof Lava);
        assertTrue(lavaSource.getLiquidSystem().contains(lavaSource));

        Cell waterSource = field.getCell(6, 8);
        assertNotNull(waterSource.getLiquidSystem());
        assertTrue(waterSource.getLiquidSystem() instanceof Water);
        assertTrue(waterSource.getLiquidSystem().contains(waterSource));

        assertSame(field, player.owner().getOwner());
        assertSame(field, ironBlock.owner().getOwner());
        assertSame(field, boat.owner().getOwner());
        assertSame(field, exit.owner().getOwner());
        assertSame(field, firstScore.owner().getOwner());
        assertSame(field, secondScore.owner().getOwner());
    }

    private static void assertBoundaryWalls(Gamefield field) {
        for (int x = 0; x < field.getWidth(); x++) {
            assertUnitAt(field, x, 0, Wall.class, true);
            assertUnitAt(field, x, field.getHeight() - 1, Wall.class, true);
        }

        for (int y = 1; y < field.getHeight() - 1; y++) {
            assertUnitAt(field, 0, y, Wall.class, true);
            assertUnitAt(field, field.getWidth() - 1, y, Wall.class, true);
        }
    }

    private static <T extends Unit> T assertUnitAt(
            Gamefield field,
            int x,
            int y,
            Class<T> type,
            boolean expectedActive
    ) {
        Cell cell = field.getCell(x, y);
        Unit unit = cell.getUnit(type);

        assertNotNull(unit, "Expected " + type.getSimpleName() + " at (" + x + ", " + y + ")");
        assertTrue(type.isInstance(unit), "Wrong unit type at (" + x + ", " + y + ")");
        assertSame(cell, unit.owner());
        assertEquals(expectedActive, unit.isActive(), "Wrong active state at (" + x + ", " + y + ")");
        assertFalse(unit.isDestroyed(), "Unit must not be destroyed at (" + x + ", " + y + ")");

        return type.cast(unit);
    }

    private static List<Unit> allUnits(Gamefield field) {
        List<Unit> result = new ArrayList<>();

        for (Cell cell : field) {
            result.addAll(cell.getUnits());
        }

        return result;
    }

    private static void assertAllUnitsHaveActiveState(List<Unit> units, boolean expectedActive) {
        for (Unit unit : units) {
            assertEquals(expectedActive, unit.isActive());
            assertFalse(unit.isDestroyed());
            assertNotNull(unit.owner());
        }
    }

    private static boolean readBoolean(Game game, String fieldName) {
        try {
            Field field = Game.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getBoolean(game);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Cannot read Game." + fieldName, e);
        }
    }
}
