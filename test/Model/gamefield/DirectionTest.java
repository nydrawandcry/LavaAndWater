package Model.gamefield;

import Model.gamefield.Direction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    void clockwise_rotatesDirectionCorrectly() {
        assertEquals(Direction.EAST, Direction.NORTH.clockwise());
        assertEquals(Direction.SOUTH, Direction.EAST.clockwise());
        assertEquals(Direction.WEST, Direction.SOUTH.clockwise());
        assertEquals(Direction.NORTH, Direction.WEST.clockwise());
    }

    @Test
    void anticlockwise_rotatesDirectionCorrectly() {
        assertEquals(Direction.WEST, Direction.NORTH.anticlockwise());
        assertEquals(Direction.SOUTH, Direction.WEST.anticlockwise());
        assertEquals(Direction.EAST, Direction.SOUTH.anticlockwise());
        assertEquals(Direction.NORTH, Direction.EAST.anticlockwise());
    }

    @Test
    void opposite_returnsCorrectDirection() {
        assertEquals(Direction.SOUTH, Direction.NORTH.opposite());
        assertEquals(Direction.NORTH, Direction.SOUTH.opposite());
        assertEquals(Direction.WEST, Direction.EAST.opposite());
        assertEquals(Direction.EAST, Direction.WEST.opposite());
    }

    @Test
    void isOpposite_returnsTrueForOppositeDirections() {
        assertTrue(Direction.NORTH.isOpposite(Direction.SOUTH));
        assertTrue(Direction.EAST.isOpposite(Direction.WEST));
    }

    @Test
    void isOpposite_returnsFalseForNonOppositeDirections() {
        assertFalse(Direction.NORTH.isOpposite(Direction.EAST));
        assertFalse(Direction.SOUTH.isOpposite(Direction.WEST));
    }

    @Test
    void onRight_returnsRightDirection() {
        assertEquals(Direction.EAST, Direction.NORTH.onRight());
    }

    @Test
    void onLeft_returnsLeftDirection() {
        assertEquals(Direction.WEST, Direction.NORTH.onLeft());
    }

    @Test
    void toString_returnsCorrectLetter() {
        assertEquals("N", Direction.NORTH.toString());
        assertEquals("S", Direction.SOUTH.toString());
        assertEquals("E", Direction.EAST.toString());
        assertEquals("W", Direction.WEST.toString());
    }
}
