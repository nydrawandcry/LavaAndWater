package Model;

import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.gamefield.Gamefield;
import Model.services.GameFactory;
import Model.units.Player;
import Model.units.liquids.Lava;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    void createGame_initializesFieldAndPlayer() {
        Game game = new GameFactory().createGame();

        assertNotNull(game.getField());
        assertNotNull(game.getPlayer());
        assertNotNull(game.getLava());
        assertNotNull(game.getWater());
        assertFalse(game.isOver());
        assertFalse(game.isWon());
    }

    @Test
    void makeTurn_playerMovesToFreeCell() {
        Game game = new GameFactory().createGame();

        Cell start = game.getPlayer().owner();
        game.makeTurn(Direction.SOUTH);

        assertNull(start.getUnit(Player.class));
        assertNotNull(game.getField().getCell(2, 1).getUnit(Player.class));
    }

    @Test
    void makeTurn_playerDoesNotMoveIntoWall() {
        Game game = new GameFactory().createGame();

        Cell start = game.getPlayer().owner();
        game.makeTurn(Direction.NORTH);

        assertNotNull(start.getUnit(Player.class));
        assertEquals(start, game.getPlayer().owner());
    }

    @Test
    void makeTurn_lavaSpreadsAfterPlayerMove() {
        Game game = new GameFactory().createGame();

        Cell targetCell = game.getField().getCell(2, 1); // игрок сюда идет вниз
        assertFalse(game.getLava().contains(targetCell));

        game.makeTurn(Direction.SOUTH);

        assertTrue(game.getLava().contains(targetCell));
    }

    @Test
    void makeTurn_waterSpreadsAfterPlayerMove_FailsCauseNoSuitableCells() {
        Game game = new GameFactory().createGame();

        // источник воды в (2,3), после spread вода должна пойти на соседей
        Cell expectedWaterCell = game.getField().getCell(1, 3);

        assertFalse(game.getWater().contains(expectedWaterCell));

        game.makeTurn(Direction.SOUTH);

        assertFalse(game.getWater().contains(expectedWaterCell));
    }

    @Test
    void makeTurn_playerLosesWhenStandingInLava() {
        Game game = new GameFactory().createGame();

        // игрок идет вниз в клетку (2,1), после spread туда доходит лава из источника (3,1)
        game.makeTurn(Direction.SOUTH);

        assertTrue(game.isOver());
        assertFalse(game.isWon());
        assertFalse(game.getPlayer().isActive());
    }

    @Test
    void makeTurn_afterGameOver_stateDoesNotChange() {
        Game game = new GameFactory().createGame();

        game.makeTurn(Direction.SOUTH); // игрок погибает
        assertTrue(game.isOver());
        assertFalse(game.isWon());

        Cell playerCellAfterLose = game.getPlayer().owner();

        game.makeTurn(Direction.EAST);

        assertEquals(playerCellAfterLose, game.getPlayer().owner());
        assertTrue(game.isOver());
        assertFalse(game.isWon());
    }
}
