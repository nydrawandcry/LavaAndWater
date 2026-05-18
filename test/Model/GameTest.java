package Model;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.gamefield.Gamefield;
import Model.units.Exit;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;
import Model.units.interactive.Player;
import Model.units.solid.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class GameTest {

    private Gamefield field;
    private Player player;
    private Lava lava;
    private Water water;
    private Game game;

    @BeforeEach
    void setUp() {
        field = new Gamefield(5, 5);
        player = new Player();
        lava = new Lava();
        water = new Water();

        field.getCell(2, 2).putUnit(player);

        game = new Game(field, player, lava, water);
    }

    @Test
    void constructor_initializesGameState() {
        assertSame(field, game.getField());
        assertSame(player, game.getPlayer());
        assertSame(lava, game.getLava());
        assertSame(water, game.getWater());

        assertFalse(game.isOver());
        assertFalse(game.isWon());
    }

    @Test
    void constructor_nullField_throwsNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new Game(null, player, lava, water)
        );
    }

    @Test
    void constructor_nullPlayer_throwsNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new Game(field, null, lava, water)
        );
    }

    @Test
    void constructor_nullLava_throwsNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new Game(field, player, null, water)
        );
    }

    @Test
    void constructor_nullWater_throwsNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> new Game(field, player, lava, null)
        );
    }

    @Test
    void playerMoveToFreeCell_movesPlayer() {
        Cell start = player.owner();
        Cell destination = start.getNeighbour(Direction.EAST);

        boolean result = player.moveTo(Direction.EAST);

        assertTrue(result);

        assertNull(start.getUnit(Player.class));

        assertSame(player,
                destination.getUnit(Player.class));

        assertSame(destination,
                player.owner());
    }

    @Test
    void playerMoveToWall_failsAndGameStateDoesNotChange() {
        Cell start = player.owner();

        start.getNeighbour(Direction.EAST)
                .putUnit(new Wall());

        boolean result = player.moveTo(Direction.EAST);

        assertFalse(result);

        assertSame(player, start.getUnit(Player.class));

        assertSame(start, player.owner());

        assertFalse(game.isOver());
        assertFalse(game.isWon());
    }

    @Test
    void successfulPlayerMove_triggersLavaSpread() {
        Cell lavaSource = field.getCell(0, 0);
        Cell expectedLavaCell = field.getCell(0, 1);

        lava.addSource(lavaSource);

        assertFalse(lava.contains(expectedLavaCell));

        player.moveTo(Direction.EAST);

        assertTrue(lava.contains(expectedLavaCell));
        assertSame(lava, expectedLavaCell.getLiquidSystem());
    }

    @Test
    void successfulPlayerMove_triggersWaterSpread() {
        Cell waterSource = field.getCell(4, 4);
        Cell expectedWaterCell = field.getCell(4, 3);

        water.addSource(waterSource);

        assertFalse(water.contains(expectedWaterCell));

        player.moveTo(Direction.EAST);

        assertTrue(water.contains(expectedWaterCell));
        assertSame(water, expectedWaterCell.getLiquidSystem());
    }

    @Test
    void failedPlayerMove_doesNotTriggerLiquidSpread() {
        Cell lavaSource = field.getCell(0, 0);

        lava.addSource(lavaSource);

        int before = lava.getCells().size();

        player.owner()
                .getNeighbour(Direction.EAST)
                .putUnit(new Wall());

        player.moveTo(Direction.EAST);

        int after = lava.getCells().size();

        assertEquals(before, after);
    }

    @Test
    void playerLosesIfLavaReachesPlayerAfterMove() {
        Cell destination =
                player.owner().getNeighbour(Direction.EAST);

        lava.addSource(destination.getNeighbour(Direction.SOUTH));

        boolean moved = player.moveTo(Direction.EAST);

        assertTrue(moved);

        assertSame(destination,
                player.owner());

        assertTrue(lava.contains(player.owner()));

        assertTrue(game.isOver());
        assertFalse(game.isWon());

        assertFalse(player.isActive());
    }

    @Test
    void playerDoesNotLoseIfLavaDoesNotReachPlayer() {
        lava.addSource(field.getCell(0, 0));

        player.moveTo(Direction.EAST);

        assertFalse(lava.contains(player.owner()));
        assertFalse(game.isOver());
        assertFalse(game.isWon());
        assertTrue(player.isActive());
    }

    @Test
    void playerWinsIfMovesOntoExit() {
        Cell exitCell =
                player.owner().getNeighbour(Direction.EAST);

        exitCell.putUnit(new Exit());

        boolean moved = player.moveTo(Direction.EAST);

        assertTrue(moved);

        assertSame(exitCell,
                player.owner());

        assertTrue(game.isOver());
        assertTrue(game.isWon());
    }

    @Test
    void ExitWinHasPriorityOverLavaDeath() {
        Cell exitCell =
                player.owner().getNeighbour(Direction.EAST);

        exitCell.putUnit(new Exit());

        lava.addSource(exitCell.getNeighbour(Direction.SOUTH));

        player.moveTo(Direction.EAST);

        assertSame(exitCell,
                player.owner());

        assertTrue(lava.contains(exitCell));

        assertTrue(game.isOver());
        assertTrue(game.isWon());

        assertTrue(player.isActive());
    }

    @Test
    void liquidsConflictAfterPlayerMove_createsWall() {
        water.addSource(field.getCell(0, 0));
        lava.addSource(field.getCell(0, 2));

        Cell conflictCell = field.getCell(0, 1);

        player.moveTo(Direction.EAST);

        assertNull(conflictCell.getLiquidSystem());
        assertNotNull(conflictCell.getUnit(Wall.class));
    }

    @Test
    void gameSubscribesToPlayerOnlyOnceEvenIfSameListenerAddedAgain() {
        player.addPlayerActionListener(game.getPlayerListener());

        lava.addSource(field.getCell(0, 0));

        player.moveTo(Direction.EAST);

        assertTrue(lava.contains(field.getCell(0, 1)));
        assertTrue(lava.contains(field.getCell(1, 0)));

        assertFalse(lava.contains(field.getCell(0, 2)));
        assertFalse(lava.contains(field.getCell(1, 1)));
        assertFalse(lava.contains(field.getCell(2, 0)));
    }
}
