package Model.services;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PositionTest {

    @Test
    void row_returnsCorrectValue() {
        Position position = new Position(3, 7);

        assertEquals(3, position.row());
    }

    @Test
    void col_returnsCorrectValue() {
        Position position = new Position(3, 7);

        assertEquals(7, position.col());
    }

    @Test
    void equals_sameReference_returnsTrue() {
        Position position = new Position(1, 2);

        assertEquals(position, position);
    }

    @Test
    void equals_sameCoordinates_returnsTrue() {
        Position first = new Position(4, 5);
        Position second = new Position(4, 5);

        assertEquals(first, second);
    }

    @Test
    void equals_null_returnsFalse() {
        Position position = new Position(1, 2);

        assertNotEquals(null, position);
    }

    @Test
    void equals_otherType_returnsFalse() {
        Position position = new Position(1, 2);

        assertNotEquals("not a position", position);
    }

    @Test
    void equals_differentRow_returnsFalse() {
        Position first = new Position(1, 2);
        Position second = new Position(9, 2);

        assertNotEquals(first, second);
    }

    @Test
    void equals_differentCol_returnsFalse() {
        Position first = new Position(1, 2);
        Position second = new Position(1, 9);

        assertNotEquals(first, second);
    }

    @Test
    void hashCode_sameCoordinates_isEqual() {
        Position first = new Position(4, 5);
        Position second = new Position(4, 5);

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void hashCode_differentCoordinates_usuallyDifferent() {
        Position first = new Position(1, 2);
        Position second = new Position(2, 1);

        assertNotEquals(first.hashCode(), second.hashCode());
    }
}
