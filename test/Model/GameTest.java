package Model;

import Model.gamefield.Direction;
import Model.gamefield.Gamefield;
import Model.units.Player;
import Model.units.liquids.Lava;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    void loadLevel_initializesFieldAndPlayer() throws IOException {
        Path file = createTempLevel(
                "#####",
                       "#P..#",
                       "#..E#",
                       "#####"
        );

        Game game = new Game();
        game.loadLevel(file.toString());

        assertNotNull(game.getField());
        assertNotNull(game.getPlayer());
        assertFalse(game.isOver());
        assertFalse(game.isWon());
    }

    @Test
    void makeTurn_playerMovesToFreeCell() throws IOException {
        Path file = createTempLevel(
                "#####",
                       "#P.E#",
                       "#####"
        );

        Game game = new Game();
        game.loadLevel(file.toString());

        game.makeTurn(Direction.EAST);

        Player player = game.getPlayer();
        assertEquals(1, player.owner().getOwner().getHeight() > 0 ? 1 : -1); // можно заменить на проверку клетки ниже
        assertNotNull(game.getField().getCell(1, 2).getUnit(Player.class));
    }

    @Test
    void makeTurn_playerDoesNotMoveIntoWall() throws IOException {
        Path file = createTempLevel(
                "#####",
                       "#P#E#",
                       "#####"
        );

        Game game = new Game();
        game.loadLevel(file.toString());

        game.makeTurn(Direction.EAST);

        assertNotNull(game.getField().getCell(1, 1).getUnit(Player.class));
        assertNull(game.getField().getCell(1, 2).getUnit(Player.class));
    }

    @Test
    void makeTurn_playerWinsOnExit() throws IOException {
        Path file = createTempLevel(
                "#####",
                       "#PE.#",
                       "#####"
        );

        Game game = new Game();
        game.loadLevel(file.toString());

        game.makeTurn(Direction.EAST);

        assertTrue(game.isOver());
        assertTrue(game.isWon());
    }

    @Test
    void makeTurn_lavaExpandsAfterPlayerMove() throws IOException {
        Path file = createTempLevel(
                "#####",
                       "#P..#",
                       "#.L.#",
                       "#..E#",
                       "#####"
        );

        Game game = new Game();
        game.loadLevel(file.toString());

        game.makeTurn(Direction.EAST);

        Gamefield field = game.getField();

        assertNotNull(field.getCell(1, 2).getUnit(Player.class));
        assertNotNull(field.getCell(1, 2).getUnit(Lava.class)); // если лава дошла туда по правилам
    }

    @Test
    void makeTurn_playerLosesWhenStandingInLava() throws IOException {
        Path file = createTempLevel(
                "#####",
                       "#P..#",
                       "#.L.#",
                       "#..E#",
                       "#####"
        );

        Game game = new Game();
        game.loadLevel(file.toString());

        game.makeTurn(Direction.EAST);

        assertTrue(game.isOver());
        assertFalse(game.isWon());
        assertFalse(game.getPlayer().isAlive());
    }

    @Test
    void makeTurn_afterGameOver_stateDoesNotChange() throws IOException {
        Path file = createTempLevel(
                "#####",
                       "#PE.#",
                       "#####"
        );

        Game game = new Game();
        game.loadLevel(file.toString());

        game.makeTurn(Direction.EAST);
        assertTrue(game.isOver());
        assertTrue(game.isWon());

        game.makeTurn(Direction.EAST);

        assertNotNull(game.getField().getCell(1, 2).getUnit(Player.class));
        assertTrue(game.isOver());
        assertTrue(game.isWon());
    }

    private Path createTempLevel(String... lines) throws IOException {
        Path file = Files.createTempFile("level-", ".txt");
        Files.write(file, java.util.List.of(lines));
        file.toFile().deleteOnExit();
        return file;
    }
}
