/*
package Model.services;

import Model.Game;
import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.units.Exit;
import Model.units.interactive.Player;
import Model.units.interactive.IronBlock;
import Model.units.solid.Wall;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class GameFactoryTest {

    @Test
    void createGame_returnsInitializedGame() {
        GameFactory factory = new GameFactory();

        Game game = factory.createGame();

        assertNotNull(game);
        assertNotNull(game.getField());
        assertNotNull(game.getPlayer());
        assertNotNull(game.getLava());
        assertNotNull(game.getWater());
        assertFalse(game.isOver());
        assertFalse(game.isWon());
    }

    @Test
    void createGame_createsFieldWithCorrectSize() {
        GameFactory factory = new GameFactory();

        Game game = factory.createGame();
        Gamefield field = game.getField();

        assertEquals(11, field.getHeight());
        assertEquals(16, field.getWidth());
    }

    @Test
    void createGame_placesPlayerToCorrectCell() {
        GameFactory factory = new GameFactory();

        Game game = factory.createGame();

        Cell playerCell = game.getField().getCell(2, 8);

        assertNotNull(playerCell.getUnit(Player.class));

        assertEquals(playerCell,
                game.getPlayer().owner());
    }

    @Test
    void createGame_placesExitToCorrectCell() {
        GameFactory factory = new GameFactory();

        Game game = factory.createGame();

        Cell exitCell = game.getField().getCell(14, 3);

        assertNotNull(exitCell.getUnit(Exit.class));
    }

    @Test
    void createGame_placesWallsToCorrectCells() {
        GameFactory factory = new GameFactory();

        Game game = factory.createGame();
        Gamefield field = game.getField();

        assertNotNull(field.getCell(0, 0).getUnit(Wall.class));
        assertNotNull(field.getCell(15, 0).getUnit(Wall.class));

        assertNotNull(field.getCell(0, 10).getUnit(Wall.class));
        assertNotNull(field.getCell(15, 10).getUnit(Wall.class));

        assertNotNull(field.getCell(4, 9).getUnit(Wall.class));
        assertNotNull(field.getCell(8, 8).getUnit(Wall.class));

        assertNotNull(field.getCell(13, 3).getUnit(Wall.class));
    }

    @Test
    void createGame_placesIronBlockToCorrectCell() {
        GameFactory factory = new GameFactory();

        Game game = factory.createGame();

        Cell blockCell = game.getField().getCell(6, 6);

        assertNotNull(blockCell.getUnit(IronBlock.class));
    }

    @Test
    void createGame_registersLavaSourcesCorrectly() {
        GameFactory factory = new GameFactory();

        Game game = factory.createGame();

        assertTrue(
                game.getLava().contains(
                        game.getField().getCell(11, 1)
                )
        );
    }

    @Test
    void createGame_registersWaterSourcesCorrectly() {
        GameFactory factory = new GameFactory();

        Game game = factory.createGame();

        assertTrue(
                game.getWater().contains(
                        game.getField().getCell(6, 8)
                )
        );
    }

    @Test
    void createGame_emptyCellRemainsEmptyAndWithoutLiquids() {
        GameFactory factory = new GameFactory();

        Game game = factory.createGame();
        Cell emptyCell = game.getField().getCell(3, 2);

        assertTrue(emptyCell.isEmpty());
        assertFalse(game.getLava().contains(emptyCell));
        assertFalse(game.getWater().contains(emptyCell));
    }

    @Test
    void createGame_doesNotMixUpUnitsAndLiquidSystems() {
        GameFactory factory = new GameFactory();

        Game game = factory.createGame();
        Gamefield field = game.getField();

        Cell lavaSource = field.getCell(11, 1);
        Cell wallCell = field.getCell(0, 0);
        Cell playerCell = field.getCell(2, 8);

        assertTrue(game.getLava().contains(lavaSource));

        assertNull(lavaSource.getUnit(Wall.class));
        assertNull(lavaSource.getUnit(Player.class));

        assertNotNull(wallCell.getUnit(Wall.class));
        assertFalse(game.getLava().contains(wallCell));

        assertNotNull(playerCell.getUnit(Player.class));
        assertFalse(game.getLava().contains(playerCell));
    }
}
*/
