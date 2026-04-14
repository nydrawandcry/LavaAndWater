package Model.services;

import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.services.configs.LevelConfig;
import Model.units.Exit;
import Model.units.Player;
import Model.units.impassable.IronBlock;
import Model.units.impassable.Wall;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class MazeTest {

    @Test
    void constructor_nullConfig_throwsException() {
        assertThrows(NullPointerException.class, () -> new Maze(null));
    }

    @Test
    void buildField_createsFieldWithCorrectSize() {
        LevelConfig config = createSimpleConfig();

        Maze maze = new Maze(config);
        Gamefield field = maze.buildField();

        assertEquals(4, field.getHeight());
        assertEquals(5, field.getWidth());
    }

    @Test
    void buildField_placesPlayerToCorrectCell() {
        LevelConfig config = createSimpleConfig();

        Maze maze = new Maze(config);
        Gamefield field = maze.buildField();

        Cell playerCell = field.getCell(1, 1);

        assertNotNull(playerCell.getUnit(Player.class));
    }

    @Test
    void buildField_placesExitToCorrectCell() {
        LevelConfig config = createSimpleConfig();

        Maze maze = new Maze(config);
        Gamefield field = maze.buildField();

        Cell exitCell = field.getCell(2, 3);

        assertNotNull(exitCell.getUnit(Exit.class));
    }

    @Test
    void buildField_placesWallsToCorrectCells() {
        LevelConfig config = createSimpleConfig();

        Maze maze = new Maze(config);
        Gamefield field = maze.buildField();

        assertNotNull(field.getCell(0, 0).getUnit(Wall.class));
        assertNotNull(field.getCell(0, 1).getUnit(Wall.class));
    }

    @Test
    void buildField_placesIronBlocksToCorrectCells() {
        LevelConfig config = createSimpleConfig();

        Maze maze = new Maze(config);
        Gamefield field = maze.buildField();

        assertNotNull(field.getCell(1, 3).getUnit(IronBlock.class));
    }

    @Test
    void buildField_placesLavaToCorrectCells() {
        LevelConfig config = createSimpleConfig();

        Maze maze = new Maze(config);
        Gamefield field = maze.buildField();

        assertNotNull(field.getCell(2, 1).getUnit(Lava.class));
    }

    @Test
    void buildField_placesWaterToCorrectCells() {
        LevelConfig config = createSimpleConfig();

        Maze maze = new Maze(config);
        Gamefield field = maze.buildField();

        assertNotNull(field.getCell(2, 2).getUnit(Water.class));
    }

    @Test
    void buildField_emptyCellsRemainEmpty() {
        LevelConfig config = createSimpleConfig();

        Maze maze = new Maze(config);
        Gamefield field = maze.buildField();

        Cell emptyCell = field.getCell(3, 4);

        assertTrue(emptyCell.isEmpty());
    }

    @Test
    void buildField_doesNotMixUpUnitTypes() {
        LevelConfig config = createSimpleConfig();

        Maze maze = new Maze(config);
        Gamefield field = maze.buildField();

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

    private LevelConfig createSimpleConfig() {
        LevelConfig config = new LevelConfig(4, 5);

        config.setPlayerPosition(new Position(1, 1));
        config.setExitPosition(new Position(2, 3));

        config.addWall(new Position(0, 0));
        config.addWall(new Position(0, 1));

        config.addIronBlock(new Position(1, 3));

        config.addLava(new Position(2, 1));
        config.addWater(new Position(2, 2));

        return config;
    }
}
