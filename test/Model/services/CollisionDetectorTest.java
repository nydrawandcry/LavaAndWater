/*
package Model.services;

import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.units.moving.Player;
import Model.units.solid.Wall;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CollisionDetectorTest {

    private CollisionDetector detector;
    private Gamefield field;
    private Lava lava;
    private Water water;

    @BeforeEach
    void setUp() {
        detector = new CollisionDetector();
        field = new Gamefield(3, 3);
        lava = new Lava();
        water = new Water();
    }

    @Test
    void resolve_singleConflictCell_createsWall() {
        Cell cell = field.getCell(1, 1);

        lava.addSource(cell);
        water.addSource(cell);

        detector.resolve(lava, water);

        assertNotNull(cell.getUnit(Wall.class));
    }

    @Test
    void resolve_singleConflictCell_removesLavaFromSystem() {
        Cell cell = field.getCell(1, 1);

        lava.addSource(cell);
        water.addSource(cell);

        detector.resolve(lava, water);

        assertFalse(lava.contains(cell));
    }

    @Test
    void resolve_singleConflictCell_removesWaterFromSystem() {
        Cell cell = field.getCell(1, 1);

        lava.addSource(cell);
        water.addSource(cell);

        detector.resolve(lava, water);

        assertFalse(water.contains(cell));
    }

    @Test
    void resolve_onlyLava_doesNothing() {
        Cell cell = field.getCell(1, 1);

        lava.addSource(cell);

        detector.resolve(lava, water);

        assertTrue(lava.contains(cell));
        assertFalse(water.contains(cell));
        assertNull(cell.getUnit(Wall.class));
    }

    @Test
    void resolve_onlyWater_doesNothing() {
        Cell cell = field.getCell(1, 1);

        water.addSource(cell);

        detector.resolve(lava, water);

        assertFalse(lava.contains(cell));
        assertTrue(water.contains(cell));
        assertNull(cell.getUnit(Wall.class));
    }

    @Test
    void resolve_noConflicts_doesNothing() {
        Cell lavaCell = field.getCell(0, 0);
        Cell waterCell = field.getCell(2, 2);

        lava.addSource(lavaCell);
        water.addSource(waterCell);

        detector.resolve(lava, water);

        assertTrue(lava.contains(lavaCell));
        assertTrue(water.contains(waterCell));

        assertNull(lavaCell.getUnit(Wall.class));
        assertNull(waterCell.getUnit(Wall.class));
    }

    @Test
    void resolve_multipleConflictCells_resolvesAll() {
        Cell first = field.getCell(0, 0);
        Cell second = field.getCell(2, 2);

        lava.addSource(first);
        water.addSource(first);

        lava.addSource(second);
        water.addSource(second);

        detector.resolve(lava, water);

        assertNotNull(first.getUnit(Wall.class));
        assertNotNull(second.getUnit(Wall.class));

        assertFalse(lava.contains(first));
        assertFalse(water.contains(first));

        assertFalse(lava.contains(second));
        assertFalse(water.contains(second));
    }

    @Test
    void resolve_conflictInOneCell_doesNotAffectSafeCells() {
        Cell conflict = field.getCell(1, 1);
        Cell safeLava = field.getCell(0, 0);
        Cell safeWater = field.getCell(2, 2);

        lava.addSource(conflict);
        water.addSource(conflict);

        lava.addSource(safeLava);
        water.addSource(safeWater);

        detector.resolve(lava, water);

        assertNotNull(conflict.getUnit(Wall.class));

        assertFalse(lava.contains(conflict));
        assertFalse(water.contains(conflict));

        assertTrue(lava.contains(safeLava));
        assertTrue(water.contains(safeWater));

        assertNull(safeLava.getUnit(Wall.class));
        assertNull(safeWater.getUnit(Wall.class));
    }

    @Test
    void resolve_playerInConflictCell_playerRemainsInCell() {
        Cell cell = field.getCell(1, 1);
        Player player = new Player();
        cell.putUnit(player);

        lava.addSource(cell);
        water.addSource(cell);

        detector.resolve(lava, water);

        assertNotNull(cell.getUnit(Player.class));
        assertNotNull(cell.getUnit(Wall.class));
        assertFalse(lava.contains(cell));
        assertFalse(water.contains(cell));
    }

    @Test
    void resolve_emptySystems_doesNotThrow() {
        assertDoesNotThrow(() -> detector.resolve(lava, water));
    }

    @Test
    void resolve_firstSystemEmpty_doesNotThrow() {
        water.addSource(field.getCell(1, 1));

        assertDoesNotThrow(() -> detector.resolve(lava, water));

        assertTrue(water.contains(field.getCell(1, 1)));
        assertNull(field.getCell(1, 1).getUnit(Wall.class));
    }

    @Test
    void resolve_secondSystemEmpty_doesNotThrow() {
        lava.addSource(field.getCell(1, 1));

        assertDoesNotThrow(() -> detector.resolve(lava, water));

        assertTrue(lava.contains(field.getCell(1, 1)));
        assertNull(field.getCell(1, 1).getUnit(Wall.class));
    }
}
*/
