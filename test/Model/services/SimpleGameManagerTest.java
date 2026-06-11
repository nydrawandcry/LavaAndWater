package Model.services;

import Model.Game;
import Model.events.cell.CellActionEvent;
import Model.events.cell.CellActionListener;
import Model.events.game.GameActionListener;
import Model.events.liquids.LiquidAppearanceInCellEvent;
import Model.events.liquids.LiquidAppearanceInCellListener;
import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.gamefield.GameManagerTest;
import Model.gamefield.Gamefield;
import Model.units.Exit;
import Model.gamefield.Unit;
import Model.units.interactive.Boat;
import Model.units.interactive.ExitScore;
import Model.units.interactive.IronBlock;
import Model.units.interactive.Player;
import Model.units.liquids.Lava;
import Model.units.liquids.LiquidSystem;
import Model.units.liquids.Water;
import Model.units.solid.Wall;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SimpleGameManagerTest extends GameManagerTest {

    @Override
    protected GameManager createManager(Gamefield field, Game game) {
        return new SimpleGameManager(field, game);
    }

    @Test
    void start_placesFullSimpleLevelSnapshot() {
        manager.start();

        assertBoundaryWalls(field);
        assertInnerWalls();
        assertSpecialUnits();
        assertLiquidSources();

        assertEquals(67, countUnits(field, Wall.class));
        assertEquals(1, countUnits(field, IronBlock.class));
        assertEquals(1, countUnits(field, Player.class));
        assertEquals(1, countUnits(field, Exit.class));
        assertEquals(2, countUnits(field, ExitScore.class));
        assertEquals(1, countUnits(field, Boat.class));
        assertEquals(73, allUnits(field).size());
    }

    @Test
    void start_firesPlacementEventsInSimpleManagerOrder_andListenersSeeCurrentState() {
        List<String> events = new ArrayList<>();

        recordSimpleManagerEvents(events);

        manager.start();

        List<String> expected = new ArrayList<>();
        expected.addAll(expectedBoundaryWallEvents(field));

        addWallEvents(expected,
                new int[][]{
                        {4, 9}, {4, 8}, {4, 7},
                        {8, 9}, {8, 8}, {8, 7},
                        {5, 7}, {7, 7},
                        {7, 2}, {8, 2}, {9, 2}, {10, 2},
                        {11, 2}, {12, 2}, {13, 2}, {14, 2},
                        {13, 3}
                }
        );

        expected.add("place:IronBlock:6:6");
        expected.add("place:Player:2:8");
        expected.add("place:Exit:14:3");
        expected.add("place:ExitScore:2:6");
        expected.add("place:ExitScore:12:8");
        expected.add("deactivate:Exit:14:3");
        expected.add("place:Boat:3:7");
        expected.add("liquidAdded:Lava:11:1");
        expected.add("liquidAdded:Water:6:8");

        assertEquals(expected, events);
    }

    @Test
    void start_connectsPlayerMovementToLavaAndWaterSpread_andStateIsCheckedDuringEvents() {
        manager.start();

        Player player = assertUnitAt(field, 2, 8, Player.class);
        Cell playerDestination = field.getCell(3, 8);

        LiquidSystem lava = field.getCell(11, 1).getLiquidSystem();
        LiquidSystem water = field.getCell(6, 8).getLiquidSystem();

        assertTrue(lava instanceof Lava);
        assertTrue(water instanceof Water);

        List<String> timeline = new ArrayList<>();

        addLiquidEventRecorder(field.getCell(10, 1), timeline, "liquidAdded:Lava:10:1", player, playerDestination);
        addLiquidEventRecorder(field.getCell(12, 1), timeline, "liquidAdded:Lava:12:1", player, playerDestination);

        addLiquidEventRecorder(field.getCell(5, 8), timeline, "liquidAdded:Water:5:8", player, playerDestination);
        addLiquidEventRecorder(field.getCell(7, 8), timeline, "liquidAdded:Water:7:8", player, playerDestination);
        addLiquidEventRecorder(field.getCell(6, 7), timeline, "liquidAdded:Water:6:7", player, playerDestination);
        addLiquidEventRecorder(field.getCell(6, 9), timeline, "liquidAdded:Water:6:9", player, playerDestination);

        // Этот listener добавлен после listener-ов лавы и воды,
        // поэтому он должен увидеть уже завершенное распространение жидкостей.
        player.addModelPlayerMovementListener(() -> {
            timeline.add("playerMovedVisual");

            assertSame(playerDestination, player.owner());

            assertTrue(lava.contains(field.getCell(10, 1)));
            assertTrue(lava.contains(field.getCell(12, 1)));

            assertTrue(water.contains(field.getCell(5, 8)));
            assertTrue(water.contains(field.getCell(7, 8)));
            assertTrue(water.contains(field.getCell(6, 7)));
            assertTrue(water.contains(field.getCell(6, 9)));
        });

        assertSame(field.getCell(2, 8), player.owner());
        assertTrue(playerDestination.isEmpty());

        assertTrue(player.moveTo(Direction.EAST));

        assertEquals("playerMovedVisual", timeline.get(timeline.size() - 1));

        Set<String> expectedLiquidEvents = Set.of(
                "liquidAdded:Lava:10:1",
                "liquidAdded:Lava:12:1",
                "liquidAdded:Water:5:8",
                "liquidAdded:Water:7:8",
                "liquidAdded:Water:6:7",
                "liquidAdded:Water:6:9"
        );

        assertEquals(expectedLiquidEvents, new HashSet<>(timeline.subList(0, timeline.size() - 1)));
        assertEquals(expectedLiquidEvents.size() + 1, timeline.size());
    }

    @Test
    void start_subscribesPlayerToGameReachListener_actualMoveIntoActiveExitWinsGame() {
        manager.start();

        Player player = assertUnitAt(field, 2, 8, Player.class);
        Cell destination = field.getCell(3, 8);
        Exit temporaryActiveExit = new Exit();

        assertTrue(destination.putUnit(temporaryActiveExit));
        assertTrue(temporaryActiveExit.isActive());

        List<String> events = new ArrayList<>();

        game.addGameActionListener(new GameActionListener() {
            @Override
            public void gameIsWon() {
                events.add("gameIsWon");

                assertTrue(game.isOver());
                assertFalse(field.isActive());

                assertSame(destination, player.owner());
                assertFalse(player.isActive());
                assertFalse(temporaryActiveExit.isActive());
            }

            @Override
            public void gameIsLost() {
                fail("Expected win, not loss");
            }
        });

        assertFalse(game.isOver());
        assertTrue(field.isActive());

        assertTrue(player.moveTo(Direction.EAST));

        assertEquals(List.of("gameIsWon"), events);
        assertTrue(game.isOver());
        assertFalse(field.isActive());
        assertSame(destination, player.owner());
    }

    private void recordSimpleManagerEvents(List<String> events) {
        for (int y = 0; y < field.getHeight(); y++) {
            for (int x = 0; x < field.getWidth(); x++) {
                final int currentX = x;
                final int currentY = y;
                Cell cell = field.getCell(currentX, currentY);

                cell.addCellActionListener(new CellActionListener() {
                    @Override
                    public void unitPlaced(CellActionEvent e) {
                        Unit unit = e.getUnit();

                        events.add(placeEvent(unit, currentX, currentY));

                        assertSame(cell, e.getSource());
                        assertSame(cell, unit.owner());
                        assertTrue(unit.isActive());
                        assertFalse(unit.isDestroyed());
                        assertSame(unit, cell.getUnit(unit.getClass()));

                        if (unit instanceof Exit exit) {
                            exit.addUnitActivationListener(() -> {
                                events.add("deactivate:Exit:" + currentX + ":" + currentY);

                                assertFalse(exit.isActive());
                                assertFalse(exit.isDestroyed());
                                assertSame(cell, exit.owner());

                                // В текущей реализации SimpleGameManager сначала кладет оба ExitScore,
                                // потом деактивирует Exit, и только после этого добавляет scores в leftScores.
                                assertNotNull(field.getCell(2, 6).getUnit(ExitScore.class));
                                assertNotNull(field.getCell(12, 8).getUnit(ExitScore.class));
                            });
                        }
                    }

                    @Override
                    public void unitExtracted(CellActionEvent e) {
                        fail("SimpleGameManager.start() should not extract units");
                    }
                });
            }
        }

        addSourceCellListener(field.getCell(11, 1), events, Lava.class, "liquidAdded:Lava:11:1");
        addSourceCellListener(field.getCell(6, 8), events, Water.class, "liquidAdded:Water:6:8");
    }

    private static void addSourceCellListener(
            Cell cell,
            List<String> events,
            Class<? extends LiquidSystem> expectedLiquidClass,
            String eventName
    ) {
        cell.addLiquidAppearanceInCellListener(new LiquidAppearanceInCellListener() {
            @Override
            public void liquidAdded(LiquidAppearanceInCellEvent e) {
                events.add(eventName);

                assertSame(cell, e.getCell());
                assertTrue(expectedLiquidClass.isInstance(e.getLiquidSystem()));
                assertSame(e.getLiquidSystem(), cell.getLiquidSystem());

                // Для addSource связь уже двусторонняя в момент события.
                assertTrue(e.getLiquidSystem().contains(cell));
            }

            @Override
            public void liquidRemoved(LiquidAppearanceInCellEvent e) {
                fail("Source liquid must not be removed during SimpleGameManager.start()");
            }
        });
    }

    private static void addLiquidEventRecorder(
            Cell cell,
            List<String> timeline,
            String eventName,
            Player player,
            Cell expectedPlayerDestination
    ) {
        cell.addLiquidAppearanceInCellListener(new LiquidAppearanceInCellListener() {
            @Override
            public void liquidAdded(LiquidAppearanceInCellEvent e) {
                timeline.add(eventName);

                assertSame(cell, e.getCell());
                assertSame(e.getLiquidSystem(), cell.getLiquidSystem());

                // Проверка состояния игры в момент события распространения жидкости:
                // игрок уже перемещен, потому что playerMoved публикуется после destination.putUnit(player).
                assertSame(expectedPlayerDestination, player.owner());
                assertSame(player, expectedPlayerDestination.getUnit(Player.class));
            }

            @Override
            public void liquidRemoved(LiquidAppearanceInCellEvent e) {
                fail("No liquid removal expected in this scenario");
            }
        });
    }

    private void assertInnerWalls() {
        int[][] walls = {
                {4, 9}, {4, 8}, {4, 7},
                {8, 9}, {8, 8}, {8, 7},
                {5, 7}, {7, 7},
                {7, 2}, {8, 2}, {9, 2}, {10, 2},
                {11, 2}, {12, 2}, {13, 2}, {14, 2},
                {13, 3}
        };

        for (int[] position : walls) {
            assertUnitAt(field, position[0], position[1], Wall.class);
        }
    }

    private void assertSpecialUnits() {
        Player player = assertUnitAt(field, 2, 8, Player.class);
        IronBlock block = assertUnitAt(field, 6, 6, IronBlock.class);
        Boat boat = assertUnitAt(field, 3, 7, Boat.class);

        Exit exit = assertUnitAt(field, 14, 3, Exit.class);
        ExitScore firstScore = assertUnitAt(field, 2, 6, ExitScore.class);
        ExitScore secondScore = assertUnitAt(field, 12, 8, ExitScore.class);

        assertTrue(player.isActive());
        assertTrue(block.isActive());
        assertTrue(boat.isActive());

        assertFalse(exit.isActive());
        assertEquals(2, exit.getLeftScores().size());
        assertTrue(exit.getLeftScores().contains(firstScore));
        assertTrue(exit.getLeftScores().contains(secondScore));

        assertTrue(firstScore.isActive());
        assertTrue(secondScore.isActive());
    }

    private void assertLiquidSources() {
        Cell lavaSource = field.getCell(11, 1);
        Cell waterSource = field.getCell(6, 8);

        assertNotNull(lavaSource.getLiquidSystem());
        assertNotNull(waterSource.getLiquidSystem());

        assertTrue(lavaSource.getLiquidSystem() instanceof Lava);
        assertTrue(waterSource.getLiquidSystem() instanceof Water);

        assertTrue(lavaSource.getLiquidSystem().contains(lavaSource));
        assertTrue(waterSource.getLiquidSystem().contains(waterSource));
    }

    private static void addWallEvents(List<String> events, int[][] positions) {
        for (int[] position : positions) {
            events.add("place:Wall:" + position[0] + ":" + position[1]);
        }
    }
}
