package Model.services;

import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.units.Exit;
import Model.units.Player;
import Model.units.impassable.IronBlock;
import Model.units.impassable.Wall;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class GameFactoryTest {

    @Test
    void constructor_nullConfig_throwsException() {
        assertThrows(NullPointerException.class, () -> new GameFactory());
    }

    @Test
    void buildField_createsFieldWithCorrectSize() {
        GameFactory gameFactory = new GameFactory();
        Gamefield field = gameFactory.buildField();

        assertEquals(4, field.getHeight());
        assertEquals(5, field.getWidth());
    }

    @Test
    void buildField_placesPlayerToCorrectCell() {
        GameFactory gameFactory = new GameFactory();
        Gamefield field = gameFactory.buildField();

        Cell playerCell = field.getCell(1, 1);

        assertNotNull(playerCell.getUnit(Player.class));
    }

    @Test
    void buildField_placesExitToCorrectCell() {
        GameFactory gameFactory = new GameFactory();
        Gamefield field = gameFactory.buildField();

        Cell exitCell = field.getCell(2, 3);

        assertNotNull(exitCell.getUnit(Exit.class));
    }

    @Test
    void buildField_placesWallsToCorrectCells() {
        GameFactory gameFactory = new GameFactory();
        Gamefield field = gameFactory.buildField();

        assertNotNull(field.getCell(0, 0).getUnit(Wall.class));
        assertNotNull(field.getCell(0, 1).getUnit(Wall.class));
    }

    @Test
    void buildField_placesIronBlocksToCorrectCells() {
        GameFactory gameFactory = new GameFactory();
        Gamefield field = gameFactory.buildField();

        assertNotNull(field.getCell(1, 3).getUnit(IronBlock.class));
    }

    @Test
    void buildField_placesLavaToCorrectCells() {
        GameFactory gameFactory = new GameFactory();
        Gamefield field = gameFactory.buildField();

        assertNotNull(field.getCell(2, 1).getUnit(Lava.class));
    }

    @Test
    void buildField_placesWaterToCorrectCells() {
        GameFactory gameFactory = new GameFactory();
        Gamefield field = gameFactory.buildField();

        assertNotNull(field.getCell(2, 2).getUnit(Water.class));
    }

    @Test
    void buildField_emptyCellsRemainEmpty() {
        GameFactory gameFactory = new GameFactory();
        Gamefield field = gameFactory.buildField();

        Cell emptyCell = field.getCell(3, 4);

        assertTrue(emptyCell.isEmpty());
    }

    @Test
    void buildField_doesNotMixUpUnitTypes() {
        GameFactory gameFactory = new GameFactory();
        Gamefield field = gameFactory.buildField();

        Cell lavaCell = field.getCell(2, 1);
        Cell wallCell = field.getCell(0, 0);
        Cell playerCell = field.getCell(1, 1);

        assertNotNull(lavaCell.getUnit(Lava.class));
        assertNull(lavaCell.getUnit(Wall.class));
        assertNull(lavaCell.getUnit(Player.class));

        assertNotNull(wallCell.getUnit(Wall.class));
        assertNull(wallCell.getUnit(Lava.class));

        assertNotNull(playerCell.getUnit(Player.class));
        assertNull(playerCell.getUnit(Wall.class));
    }
}
