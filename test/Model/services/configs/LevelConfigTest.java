package Model.services.configs;

import Model.services.Position;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LevelConfigTest {

    @Test
    void constructor_validSize_setsFields() {
        LevelConfig config = new LevelConfig(5, 7);

        assertEquals(5, config.getHeight());
        assertEquals(7, config.getWidth());
    }

    @Test
    void constructor_nonPositiveHeight_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new LevelConfig(0, 5));
    }

    @Test
    void constructor_nonPositiveWidth_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new LevelConfig(5, 0));
    }

    @Test
    void setPlayerPosition_savesPosition() {
        LevelConfig config = new LevelConfig(5, 5);
        Position position = new Position(1, 2);

        config.setPlayerPosition(position);

        assertEquals(position, config.getPlayerPosition());
    }

    @Test
    void setExitPosition_savesPosition() {
        LevelConfig config = new LevelConfig(5, 5);
        Position position = new Position(3, 4);

        config.setExitPosition(position);

        assertEquals(position, config.getExitPosition());
    }

    @Test
    void addWall_addsPosition() {
        LevelConfig config = new LevelConfig(5, 5);
        Position position = new Position(0, 0);

        config.addWall(position);

        assertTrue(config.getWalls().contains(position));
    }

    @Test
    void addLava_addsMultiplePositions() {
        LevelConfig config = new LevelConfig(5, 5);

        Position first = new Position(1, 1);
        Position second = new Position(2, 2);

        config.addLava(first);
        config.addLava(second);

        assertEquals(2, config.getLava().size());
        assertTrue(config.getLava().contains(first));
        assertTrue(config.getLava().contains(second));
    }

    @Test
    void getWalls_returnsUnmodifiableList() {
        LevelConfig config = new LevelConfig(5, 5);
        config.addWall(new Position(0, 0));

        assertThrows(UnsupportedOperationException.class,
                () -> config.getWalls().add(new Position(1, 1)));
    }

    @Test
    void validate_withPlayerAndExit_doesNotThrow() {
        LevelConfig config = new LevelConfig(5, 5);
        config.setPlayerPosition(new Position(1, 1));
        config.setExitPosition(new Position(2, 2));

        assertDoesNotThrow(config::validate);
    }

    @Test
    void validate_withoutPlayer_throwsException() {
        LevelConfig config = new LevelConfig(5, 5);
        config.setExitPosition(new Position(2, 2));

        assertThrows(IllegalStateException.class, config::validate);
    }

    @Test
    void validate_withoutExit_throwsException() {
        LevelConfig config = new LevelConfig(5, 5);
        config.setPlayerPosition(new Position(1, 1));

        assertThrows(IllegalStateException.class, config::validate);
    }

    @Test
    void validate_withoutPlayerAndExit_throwsException() {
        LevelConfig config = new LevelConfig(5, 5);

        assertThrows(IllegalStateException.class, config::validate);
    }
}
