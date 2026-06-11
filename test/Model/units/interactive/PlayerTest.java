package Model.units.interactive;

import Model.events.player.PlayerReachListener;
import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.gamefield.Gamefield;
import Model.services.CollisionDetector;
import Model.units.AbstractUnitTest;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;
import Model.units.solid.Wall;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class PlayerTest extends AbstractUnitTest<Player> {

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
        assertEquals(unit, field.getCell(2, 1).getUnit(Player.class));
    }

    @Test
    void moveTo_wall_returnsFalse() {
        Cell start = field.getCell(1, 1);
        start.putUnit(unit);

        field.getCell(2, 1).putUnit(new Wall());

        boolean result = unit.moveTo(Direction.EAST);

        assertFalse(result);
        assertEquals(unit, start.getUnit(Player.class));
    }

    @Test
    void moveTo_pushesIronBlock_ifPossible() {
        Cell start = field.getCell(0, 0);
        start.putUnit(unit);

        IronBlock block = new IronBlock();
        field.getCell(1, 0).putUnit(block);

        boolean result = unit.moveTo(Direction.EAST);

        assertTrue(result);

        assertNull(field.getCell(0, 0).getUnit(Player.class));

        assertEquals(unit,
                field.getCell(1, 0).getUnit(Player.class));

        assertEquals(block,
                field.getCell(2, 0).getUnit(IronBlock.class));
    }

    @Test
    void moveTo_fails_ifIronBlockCannotBePushed() {
        Cell start = field.getCell(0, 0);
        start.putUnit(unit);

        IronBlock block = new IronBlock();
        field.getCell(1, 0).putUnit(block);

        field.getCell(2, 0).putUnit(new Wall());

        boolean result = unit.moveTo(Direction.EAST);

        assertFalse(result);

        assertEquals(unit,
                field.getCell(0, 0).getUnit(Player.class));

        assertEquals(block,
                field.getCell(1, 0).getUnit(IronBlock.class));
    }

    @Test
    void moveToLavaCell_mustFirePlayerInLava_afterPlayerMovedIntoLava() {
        Cell playerCell = field.getCell(0, 0);
        Cell lavaCell = field.getCell(1, 0);

        Player player = new Player();
        Lava lava = new Lava();

        assertTrue(playerCell.putUnit(player));
        lava.addSource(lavaCell);

        List<String> events = new ArrayList<>();

        player.addViewPlayerMovementListener(() -> {
            events.add("playerMoved");

            assertSame(lavaCell, player.owner());
            assertSame(player, lavaCell.getUnit(Player.class));
            assertSame(lava, lavaCell.getLiquidSystem());
            assertTrue(lava.contains(lavaCell));
        });

        player.addPlayerReachListener(new PlayerReachListener() {
            @Override
            public void playerInLava() {
                events.add("playerInLava");

                assertSame(lavaCell, player.owner());
                assertSame(player, lavaCell.getUnit(Player.class));
                assertSame(lava, lavaCell.getLiquidSystem());
                assertTrue(lava.contains(lavaCell));
            }

            @Override
            public void playerInWall() {
                fail("Ожидалась лава, не стена");
            }

            @Override
            public void playerInExit() {
                fail("Ожидалась лава, не выход");
            }
        });

        assertSame(playerCell, player.owner());
        assertSame(lava, lavaCell.getLiquidSystem());
        assertTrue(lava.contains(lavaCell));

        assertTrue(player.moveTo(Direction.EAST));

        assertEquals(List.of("playerMoved", "playerInLava"), events);
    }

    @Test
    void moveTo_emptyCellThenLiquidConflictCreatesWall_firesPlayerMovedThenPlayerInWall() {
        Gamefield field = new Gamefield(3, 3);

        Cell playerCell = field.getCell(0, 1);
        Cell destination = field.getCell(1, 1);

        Cell lavaSource = field.getCell(1, 0);
        Cell waterSource = field.getCell(1, 2);

        Player player = new Player();
        Lava lava = new Lava();
        Water water = new Water();
        CollisionDetector detector = new CollisionDetector();

        assertTrue(playerCell.putUnit(player));

        lava.addSource(lavaSource);
        water.addSource(waterSource);

        lava.addLiquidSystemCollisionListener(detector.getLiquidListener());
        water.addLiquidSystemCollisionListener(detector.getLiquidListener());

        player.addModelPlayerMovementListener(lava.getPlayerMovementListener());
        player.addModelPlayerMovementListener(water.getPlayerMovementListener());

        List<String> events = new ArrayList<>();

        player.addViewPlayerMovementListener(() -> {
            events.add("playerMoved");

            // Это listener после lava/water, поэтому конфликт уже обработан.
            assertSame(destination, player.owner());
            assertSame(player, destination.getUnit(Player.class));
            assertNotNull(destination.getUnit(Wall.class));
            assertNull(destination.getLiquidSystem());
        });

        player.addPlayerReachListener(new PlayerReachListener() {
            @Override
            public void playerInLava() {
                fail("Ожидалась стена после конфликта жидкостей, не лава");
            }

            @Override
            public void playerInWall() {
                events.add("playerInWall");

                // Проверка В МОМЕНТ события playerInWall.
                assertSame(destination, player.owner());
                assertSame(player, destination.getUnit(Player.class));
                assertNotNull(destination.getUnit(Wall.class));
                assertNull(destination.getLiquidSystem());
            }

            @Override
            public void playerInExit() {
                fail("Ожидалась стена после конфликта жидкостей, не выход");
            }
        });

        // Снимок ДО хода.
        assertSame(playerCell, player.owner());
        assertTrue(destination.isEmpty());
        assertNull(destination.getLiquidSystem());
        assertSame(lava, lavaSource.getLiquidSystem());
        assertSame(water, waterSource.getLiquidSystem());

        assertTrue(player.moveTo(Direction.EAST));

        // Снимок ПОСЛЕ всей цепочки.
        assertEquals(List.of("playerMoved", "playerInWall"), events);
        assertSame(destination, player.owner());
        assertSame(player, destination.getUnit(Player.class));
        assertNotNull(destination.getUnit(Wall.class));
        assertNull(destination.getLiquidSystem());
        assertNull(playerCell.getUnit(Player.class));
    }

    @Test
    void moveTo_beforePlayerIsPlaced_mustThrow() {
        Player player = new Player();

        assertThrows(
                IllegalStateException.class,
                () -> player.moveTo(Direction.EAST)
        );
    }

    @Test
    void moveTo_processesModelMovementListenersBeforeViewListeners_evenIfViewWasRegisteredFirst() {
        Gamefield field = new Gamefield(1, 2);

        Cell start = field.getCell(0, 0);
        Cell destination = field.getCell(1, 0);

        Player player = new Player();

        assertTrue(start.putUnit(player));

        List<String> events = new ArrayList<>();
        AtomicBoolean modelHandled = new AtomicBoolean(false);

        // View подписан ПЕРВЫМ.
        player.addViewPlayerMovementListener(() -> {
            events.add("view:PlayerWidget");

            assertTrue(modelHandled.get());

            assertSame(destination, player.owner());
            assertSame(player, destination.getUnit(Player.class));
            assertNull(start.getUnit(Player.class));
        });

        // Model подписан ВТОРЫМ.
        player.addModelPlayerMovementListener(() -> {
            events.add("model:MovementSystem");

            assertSame(destination, player.owner());
            assertSame(player, destination.getUnit(Player.class));
            assertNull(start.getUnit(Player.class));

            modelHandled.set(true);
        });

        assertSame(start, player.owner());
        assertFalse(modelHandled.get());

        assertTrue(player.moveTo(Direction.EAST));

        assertEquals(
                List.of(
                        "model:MovementSystem",
                        "view:PlayerWidget"
                ),
                events
        );
    }

    @Test
    void moveTo_processesLiquidSystemBeforePlayerWidget_evenIfWidgetWasRegisteredFirst() {
        Gamefield field = new Gamefield(1, 3);

        Cell playerCell = field.getCell(0, 0);
        Cell destination = field.getCell(1, 0);
        Cell lavaSource = field.getCell(2, 0);

        Player player = new Player();
        Lava lava = new Lava();

        assertTrue(playerCell.putUnit(player));
        lava.addSource(lavaSource);

        List<String> events = new ArrayList<>();

        // View подписан ПЕРВЫМ.
        player.addViewPlayerMovementListener(() -> {
            events.add("view:PlayerWidget");

            assertSame(destination, player.owner());
            assertSame(player, destination.getUnit(Player.class));

            // Главное: view уже видит результат model listener-а LiquidSystem.
            assertTrue(lava.contains(destination));
            assertSame(lava, destination.getLiquidSystem());
        });

        player.addPlayerReachListener(new PlayerReachListener() {
            @Override
            public void playerInLava() {
                events.add("model:playerInLava");

                assertSame(destination, player.owner());
                assertTrue(lava.contains(destination));
                assertSame(lava, destination.getLiquidSystem());
            }

            @Override
            public void playerInWall() {
                fail("Ожидалась лава, не стена");
            }

            @Override
            public void playerInExit() {
                fail("Ожидалась лава, не выход");
            }
        });

        // LiquidSystem подписан ВТОРЫМ, но должен обработаться раньше view.
        player.addModelPlayerMovementListener(lava.getPlayerMovementListener());

        assertSame(playerCell, player.owner());
        assertFalse(lava.contains(destination));
        assertNull(destination.getLiquidSystem());

        assertTrue(player.moveTo(Direction.EAST));

        assertEquals(
                List.of(
                        "view:PlayerWidget",
                        "model:playerInLava"
                ),
                events
        );

        assertTrue(lava.contains(destination));
        assertSame(lava, destination.getLiquidSystem());
    }

    @Test
    void moveTo_liquidConflictIsResolvedBeforePlayerWidgetAndBeforePlayerInWall() {
        Gamefield field = new Gamefield(3, 3);

        Cell playerCell = field.getCell(0, 1);
        Cell destination = field.getCell(1, 1);

        Cell lavaSource = field.getCell(1, 0);
        Cell waterSource = field.getCell(1, 2);

        Player player = new Player();
        Lava lava = new Lava();
        Water water = new Water();
        CollisionDetector detector = new CollisionDetector();

        assertTrue(playerCell.putUnit(player));

        lava.addSource(lavaSource);
        water.addSource(waterSource);

        lava.addLiquidSystemCollisionListener(detector.getLiquidListener());
        water.addLiquidSystemCollisionListener(detector.getLiquidListener());

        List<String> events = new ArrayList<>();

        // View подписан раньше.
        player.addViewPlayerMovementListener(() -> {
            events.add("view:PlayerWidget");

            // View уже должен видеть результат вычислительной модели:
            // конфликт жидкостей разрешен, в клетке появилась стена.
            assertSame(destination, player.owner());
            assertSame(player, destination.getUnit(Player.class));
            assertNotNull(destination.getUnit(Wall.class));
            assertNull(destination.getLiquidSystem());
        });

        player.addPlayerReachListener(new PlayerReachListener() {
            @Override
            public void playerInLava() {
                fail("Ожидалась стена после конфликта, не лава");
            }

            @Override
            public void playerInWall() {
                events.add("model:playerInWall");

                assertSame(destination, player.owner());
                assertSame(player, destination.getUnit(Player.class));
                assertNotNull(destination.getUnit(Wall.class));
                assertNull(destination.getLiquidSystem());
            }

            @Override
            public void playerInExit() {
                fail("Ожидалась стена после конфликта, не выход");
            }
        });

        // Model подписаны позже, но должны обработаться первыми.
        player.addModelPlayerMovementListener(lava.getPlayerMovementListener());
        player.addModelPlayerMovementListener(water.getPlayerMovementListener());

        assertTrue(destination.isEmpty());
        assertNull(destination.getLiquidSystem());

        assertTrue(player.moveTo(Direction.EAST));

        assertEquals(
                List.of(
                        "view:PlayerWidget",
                        "model:playerInWall"
                ),
                events
        );

        assertSame(destination, player.owner());
        assertSame(player, destination.getUnit(Player.class));
        assertNotNull(destination.getUnit(Wall.class));
        assertNull(destination.getLiquidSystem());
    }
}
