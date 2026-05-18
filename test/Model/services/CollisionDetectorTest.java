package Model.services;

import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.units.interactive.Player;
import Model.units.solid.Wall;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class CollisionDetectorTest {

    private CollisionDetector detector;
    private Gamefield field;

    @BeforeEach
    void setUp() {
        detector = new CollisionDetector();
        field = new Gamefield(3, 3);
    }

    @Test
    void conflictAppeared_clearsLiquidSystemFromCell() {
        Cell cell = field.getCell(1, 1);
        Water water = new Water();

        water.addSource(cell);

        detector.getLiquidListener().conflictAppeared(cell);

        assertNull(cell.getLiquidSystem());
    }

    @Test
    void conflictAppeared_createsWallInCell() {
        Cell cell = field.getCell(1, 1);
        Water water = new Water();

        water.addSource(cell);

        detector.getLiquidListener().conflictAppeared(cell);

        assertNotNull(cell.getUnit(Wall.class));
    }

    @Test
    void conflictAppeared_doesNotRemovePlayerFromCell() {
        Cell cell = field.getCell(1, 1);
        Player player = new Player();

        cell.putUnit(player);

        detector.getLiquidListener().conflictAppeared(cell);

        assertSame(player, cell.getUnit(Player.class));
        assertNull(cell.getUnit(Wall.class));
    }

    @Test
    void conflictAppeared_calledTwice_doesNotCreateWallsAtAll() {
        Cell cell = field.getCell(1, 1);

        detector.getLiquidListener().conflictAppeared(cell);
        detector.getLiquidListener().conflictAppeared(cell);

        assertEquals(0, cell.getUnits(Wall.class).size());
    }

    @Test
    void conflictAppeared_nullCell_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> detector.getLiquidListener().conflictAppeared(null));
    }

    @Test
    void integration_whenLiquidsBothLavaAndWaterSpreadIntoLavaCell_detectorCreatesWall() {
        Water water = new Water();
        Lava lava = new Lava();

        Cell waterSource = field.getCell(1, 1);
        Cell lavaCell = field.getCell(1, 2);

        water.addSource(waterSource);
        lava.addSource(lavaCell);

        water.addLiquidSystemCollisionListener(detector.getLiquidListener());

        water.spread();
        lava.spread();

        assertNull(lavaCell.getLiquidSystem());
        assertNotNull(lavaCell.getUnit(Wall.class));
        assertFalse(water.contains(lavaCell));
        assertFalse(lava.contains(waterSource));
    }

    @Test
    void integration_conflictResolutionDoesNotAffectSourceCell() {
        Water water = new Water();
        Lava lava = new Lava();

        Cell waterSource = field.getCell(1, 1);
        Cell lavaCell = field.getCell(1, 2);

        water.addSource(waterSource);
        lava.addSource(lavaCell);

        water.addLiquidSystemCollisionListener(detector.getLiquidListener());

        water.spread();

        assertTrue(water.contains(waterSource));
        assertSame(water, waterSource.getLiquidSystem());
        assertNull(waterSource.getUnit(Wall.class));
    }

    @Test
    void integration_conflictCellDoesNotBecomePartOfSpreadingLiquid() {
        Water water = new Water();
        Lava lava = new Lava();

        Cell waterSource = field.getCell(1, 1);
        Cell lavaCell = field.getCell(1, 2);

        water.addSource(waterSource);
        lava.addSource(lavaCell);

        water.addLiquidSystemCollisionListener(detector.getLiquidListener());

        water.spread();

        assertFalse(water.contains(lavaCell));
    }

    @Test
    void integration_withoutDetector_conflictDoesNotCreateWall() {
        Water water = new Water();
        Lava lava = new Lava();

        Cell waterSource = field.getCell(1, 1);
        Cell lavaCell = field.getCell(1, 2);

        water.addSource(waterSource);
        lava.addSource(lavaCell);

        water.spread();

        assertSame(lava, lavaCell.getLiquidSystem());
        assertNull(lavaCell.getUnit(Wall.class));
    }

    @Test
    void integration_detectorCanBeRemoved() {
        Water water = new Water();
        Lava lava = new Lava();

        Cell waterSource = field.getCell(1, 1);
        Cell lavaCell = field.getCell(1, 2);

        water.addSource(waterSource);
        lava.addSource(lavaCell);

        water.addLiquidSystemCollisionListener(detector.getLiquidListener());
        water.removeLiquidSystemCollisionListener(detector.getLiquidListener());

        water.spread();

        assertSame(lava, lavaCell.getLiquidSystem());
        assertNull(lavaCell.getUnit(Wall.class));
    }
}
