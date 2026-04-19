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

public class MazeTest {

    @Test
    void constructor_nullConfig_throwsException() {
        assertThrows(NullPointerException.class, () -> new Maze());
    }

    @Test
    void buildField_createsFieldWithCorrectSize() {
        Maze maze = new Maze();
        Gamefield field = maze.buildField();

        assertEquals(4, field.getHeight());
        assertEquals(5, field.getWidth());
    }

    @Test
    void buildField_placesPlayerToCorrectCell() {
        Maze maze = new Maze();
        Gamefield field = maze.buildField();

        Cell playerCell = field.getCell(1, 1);

        assertNotNull(playerCell.getUnit(Player.class));
    }

    @Test
    void buildField_placesExitToCorrectCell() {
        Maze maze = new Maze();
        Gamefield field = maze.buildField();

        Cell exitCell = field.getCell(2, 3);

        assertNotNull(exitCell.getUnit(Exit.class));
    }

    @Test
    void buildField_placesWallsToCorrectCells() {
        Maze maze = new Maze();
        Gamefield field = maze.buildField();

        assertNotNull(field.getCell(0, 0).getUnit(Wall.class));
        assertNotNull(field.getCell(0, 1).getUnit(Wall.class));
    }

    @Test
    void buildField_placesIronBlocksToCorrectCells() {
        Maze maze = new Maze();
        Gamefield field = maze.buildField();

        assertNotNull(field.getCell(1, 3).getUnit(IronBlock.class));
    }

    @Test
    void buildField_placesLavaToCorrectCells() {
        Maze maze = new Maze();
        Gamefield field = maze.buildField();

        assertNotNull(field.getCell(2, 1).getUnit(Lava.class));
    }

    @Test
    void buildField_placesWaterToCorrectCells() {
        Maze maze = new Maze();
        Gamefield field = maze.buildField();

        assertNotNull(field.getCell(2, 2).getUnit(Water.class));
    }

    @Test
    void buildField_emptyCellsRemainEmpty() {
        Maze maze = new Maze();
        Gamefield field = maze.buildField();

        Cell emptyCell = field.getCell(3, 4);

        assertTrue(emptyCell.isEmpty());
    }

    @Test
    void buildField_doesNotMixUpUnitTypes() {
        Maze maze = new Maze();
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
}
