package Model.gamefield;

import Model.units.Exit;
import Model.units.interactive.Player;
import Model.units.solid.Wall;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GamefieldTest {

    @Test
    void constructor_setsCorrectSize() {
        Gamefield field = new Gamefield(3,3);

        assertEquals(3, field.getHeight());
        assertEquals(3, field.getWidth());
    }

    @Test
    void constructor_createsAllCells() {
        Gamefield field = new Gamefield(3,3);

        int count = 0;
        for(Cell c : field){
            count++;
        }

        assertEquals(9, count);
    }

    @Test
    void cornerCell_hasCorrectNeighbours() {
        Gamefield field = new Gamefield(3,3);

        Cell c = field.getCell(0,0);

        assertNull(c.getNeighbour(Direction.NORTH));
        assertNull(c.getNeighbour(Direction.WEST));
        assertNotNull(c.getNeighbour(Direction.SOUTH));
        assertNotNull(c.getNeighbour(Direction.EAST));
    }

    @Test
    void getCell_returnsCorrectCell() {
        Gamefield field = new Gamefield(3,3);

        Cell c1 = field.getCell(0,0);
        Cell c2 = field.getCell(0,0);

        assertSame(c1, c2);
    }

    @Test
    void getCell_throwsIfOutOfBounds() {
        Gamefield field = new Gamefield(3,3);

        assertThrows(IndexOutOfBoundsException.class,
                () -> field.getCell(3,0));

        assertThrows(IndexOutOfBoundsException.class,
                () -> field.getCell(0,3));
    }

    @Test
    void centerCell_hasFourNeighbours() {
        Gamefield field = new Gamefield(3,3);

        Cell c = field.getCell(1,1);

        assertNotNull(c.getNeighbour(Direction.NORTH));
        assertNotNull(c.getNeighbour(Direction.SOUTH));
        assertNotNull(c.getNeighbour(Direction.EAST));
        assertNotNull(c.getNeighbour(Direction.WEST));
    }

    @Test
    void iterator_iteratesOverAllCells() {
        Gamefield field = new Gamefield(2,2);

        int count = 0;

        for(Cell c : field){
            count++;
        }

        assertEquals(4, count);
    }

    //проверка связи cell-----<#>gamefield
    @Test
    void cell_knowsItsOwnerField() {
        Gamefield field = new Gamefield(2,2);

        Cell c = field.getCell(0,0);

        assertEquals(field, c.getOwner());
    }

    @Test
    void deactivate_deactivatesUnitsInDifferentCells() {
        Gamefield field = new Gamefield(3,3);

        Player p = new Player();
        Wall w = new Wall();

        field.getCell(0,0).putUnit(p);
        field.getCell(1,1).putUnit(w);

        field.deactivate();

        assertFalse(p.isActive());
        assertFalse(w.isActive());
    }

    @Test
    void deactivate_deactivatesAllUnits() {
        Gamefield field = new Gamefield(3,3);

        Exit ex = new Exit();
        field.getCell(0,0).putUnit(ex);

        field.deactivate();

        assertFalse(ex.isActive());
    }
}
