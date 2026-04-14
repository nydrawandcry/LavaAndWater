package Model.services.configs;

import Model.services.Position;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class LevelConfigReaderTest {

    @Test
    void read_validLevel_parsesAllDataCorrectly() throws IOException {
        Path file = createTempLevel(
                "#####",
                "#P.L#",
                "#WI.#",
                "#..E#",
                "#####"
        );

        LevelConfigReader reader = new LevelConfigReader();
        LevelConfig config = reader.read(file.toString());

        assertEquals(5, config.getHeight());
        assertEquals(5, config.getWidth());

        assertEquals(new Position(1, 1), config.getPlayerPosition());
        assertEquals(new Position(3, 3), config.getExitPosition());

        assertTrue(config.getWalls().contains(new Position(0, 0)));
        assertTrue(config.getLava().contains(new Position(1, 3)));
        assertTrue(config.getWater().contains(new Position(2, 1)));
        assertTrue(config.getIronBlocks().contains(new Position(2, 2)));
    }

    @Test
    void read_validLevelWithMultipleLavaSources_parsesAllLavaPositions() throws IOException {
        Path file = createTempLevel(
                "#######",
                "#P.LL.#",
                "#..L..#",
                "#...E.#",
                "#######"
        );

        LevelConfigReader reader = new LevelConfigReader();
        LevelConfig config = reader.read(file.toString());

        assertEquals(3, config.getLava().size());
        assertTrue(config.getLava().contains(new Position(1, 3)));
        assertTrue(config.getLava().contains(new Position(1, 4)));
        assertTrue(config.getLava().contains(new Position(2, 3)));
    }

    @Test
    void read_validLevelWithMultipleWaterSources_parsesAllWaterPositions() throws IOException {
        Path file = createTempLevel(
                "#######",
                "#P.WW.#",
                "#..W.E#",
                "#######"
        );

        LevelConfigReader reader = new LevelConfigReader();
        LevelConfig config = reader.read(file.toString());

        assertEquals(3, config.getWater().size());
        assertTrue(config.getWater().contains(new Position(1, 3)));
        assertTrue(config.getWater().contains(new Position(1, 4)));
        assertTrue(config.getWater().contains(new Position(2, 3)));
    }

    @Test
    void read_emptyFile_throwsException() throws IOException {
        Path file = createTempLevel();

        LevelConfigReader reader = new LevelConfigReader();

        assertThrows(IllegalArgumentException.class, () -> reader.read(file.toString()));
    }

    @Test
    void read_nonRectangularLevel_throwsException() throws IOException {
        Path file = createTempLevel(
                "#####",
                "#P.#",
                "#..E#"
        );

        LevelConfigReader reader = new LevelConfigReader();

        assertThrows(IllegalArgumentException.class, () -> reader.read(file.toString()));
    }

    @Test
    void read_unknownSymbol_throwsException() throws IOException {
        Path file = createTempLevel(
                "#####",
                "#P@.#",
                "#..E#",
                "#####"
        );

        LevelConfigReader reader = new LevelConfigReader();

        assertThrows(IllegalArgumentException.class, () -> reader.read(file.toString()));
    }

    @Test
    void read_withoutPlayer_throwsException() throws IOException {
        Path file = createTempLevel(
                "#####",
                "#...#",
                "#..E#",
                "#####"
        );

        LevelConfigReader reader = new LevelConfigReader();

        assertThrows(IllegalStateException.class, () -> reader.read(file.toString()));
    }

    @Test
    void read_withoutExit_throwsException() throws IOException {
        Path file = createTempLevel(
                "#####",
                "#P..#",
                "#...#",
                "#####"
        );

        LevelConfigReader reader = new LevelConfigReader();

        assertThrows(IllegalStateException.class, () -> reader.read(file.toString()));
    }

    private Path createTempLevel(String... lines) throws IOException {
        Path file = Files.createTempFile("level-", ".txt");
        Files.write(file, java.util.List.of(lines));
        file.toFile().deleteOnExit();
        return file;
    }
}
