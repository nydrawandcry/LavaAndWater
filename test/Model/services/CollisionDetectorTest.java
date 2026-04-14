package Model.services;

import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.units.Player;
import Model.units.impassable.Wall;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CollisionDetectorTest {

    private CollisionDetector detector;
    private Gamefield field;

    @BeforeEach
    void setUp() {
        detector = new CollisionDetector();
        field = new Gamefield(3, 3);
    }

    @Test
    void resolve_cellWithLavaAndWater_replacesThemWithWall() {
        Cell cell = field.getCell(1, 1);
        cell.putUnit(new Lava());
        cell.putUnit(new Water());

        detector.resolve(field);

        assertNull(cell.getUnit(Lava.class));
        assertNull(cell.getUnit(Water.class));
        assertNotNull(cell.getUnit(Wall.class));
    }

    @Test
    void resolve_cellWithOnlyLava_doesNothing() {
        Cell cell = field.getCell(1, 1);
        Lava lava = new Lava();
        cell.putUnit(lava);

        detector.resolve(field);

        assertEquals(lava, cell.getUnit(Lava.class));
        assertNull(cell.getUnit(Water.class));
        assertNull(cell.getUnit(Wall.class));
    }

    @Test
    void resolve_cellWithOnlyWater_doesNothing() {
        Cell cell = field.getCell(1, 1);
        Water water = new Water();
        cell.putUnit(water);

        detector.resolve(field);

        assertEquals(water, cell.getUnit(Water.class));
        assertNull(cell.getUnit(Lava.class));
        assertNull(cell.getUnit(Wall.class));
    }

    @Test
    void resolve_multipleConflictCells_resolvesAllOfThem() {
        Cell first = field.getCell(0, 0);
        Cell second = field.getCell(2, 2);

        first.putUnit(new Lava());
        first.putUnit(new Water());

        second.putUnit(new Lava());
        second.putUnit(new Water());

        detector.resolve(field);

        assertNotNull(first.getUnit(Wall.class));
        assertNull(first.getUnit(Lava.class));
        assertNull(first.getUnit(Water.class));

        assertNotNull(second.getUnit(Wall.class));
        assertNull(second.getUnit(Lava.class));
        assertNull(second.getUnit(Water.class));
    }

    @Test
    void resolve_conflictInOneCell_doesNotAffectOtherCells() {
        Cell conflict = field.getCell(1, 1);
        Cell safe = field.getCell(0, 0);

        conflict.putUnit(new Lava());
        conflict.putUnit(new Water());

        Lava safeLava = new Lava();
        safe.putUnit(safeLava);

        detector.resolve(field);

        assertNotNull(conflict.getUnit(Wall.class));
        assertEquals(safeLava, safe.getUnit(Lava.class));
        assertNull(safe.getUnit(Wall.class));
    }

    @Test
    void resolve_emptyField_doesNotThrow() {
        assertDoesNotThrow(() -> detector.resolve(field));
    }

    @Test
    void resolve_playerInConflictCell_playerRemainsInCell() {
        Cell cell = field.getCell(1, 1);
        Player player = new Player();

        cell.putUnit(player);
        cell.putUnit(new Lava());
        cell.putUnit(new Water());

        detector.resolve(field);

        assertNotNull(cell.getUnit(Player.class));
        assertNotNull(cell.getUnit(Wall.class));
        assertNull(cell.getUnit(Lava.class));
        assertNull(cell.getUnit(Water.class));
    }
}
