package Model.gamefield;

import Model.Game;
import Model.events.cell.CellActionEvent;
import Model.events.cell.CellActionListener;
import Model.services.GameManager;
import Model.services.SimpleGameManager;
import Model.units.Exit;
import Model.units.interactive.Player;
import Model.units.solid.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class GameManagerTest {

    protected Gamefield field;
    protected Game game;
    protected GameManager manager;

    protected abstract GameManager createManager(Gamefield field, Game game);

    protected int fieldHeight() {
        return 11;
    }

    protected int fieldWidth() {
        return 16;
    }

    @BeforeEach
    void setUpGameManagerContract() {
        field = new Gamefield(fieldHeight(), fieldWidth());
        game = new Game();

        // В реальной игре это делает Game.start() до создания SimpleGameManager.
        injectGameField(game, field);

        manager = createManager(field, game);
    }

    @Test
    void constructor_storesFieldGameAndCreatesDetector() {
        assertSame(field, manager.getGamefield());
        assertSame(game, manager.getGame());
        assertNotNull(manager.getDetector());
        assertNotNull(manager.getDetector().getLiquidListener());
    }

    @Test
    void constructor_withNullField_mustThrowControlledIllegalStateException() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> createManager(null, game)
        );

        assertTrue(exception.getMessage().contains("Поле"));
    }

    @Test
    void start_withDestroyedField_doesNotPlaceUnitsAndDoesNotFireCellEvents() {
        List<String> events = new ArrayList<>();
        recordCellEvents(field, events, true);

        field.destroy();

        assertTrue(field.isDestroyed());
        assertTrue(allUnits(field).isEmpty());

        manager.start();

        assertTrue(events.isEmpty());
        assertTrue(allUnits(field).isEmpty());

        for (Cell cell : field) {
            assertNull(cell.getLiquidSystem());
        }
    }

    @Test
    void start_placesBoundaryWallsFirst_andEveryBoundaryEventSeesPlacedActiveWall() {
        List<String> events = new ArrayList<>();
        recordCellEvents(field, events, true);

        manager.start();

        List<String> expectedBoundaryEvents = expectedBoundaryWallEvents(field);

        assertTrue(
                events.size() >= expectedBoundaryEvents.size(),
                "start() must publish at least boundary wall placement events"
        );

        assertEquals(expectedBoundaryEvents, events.subList(0, expectedBoundaryEvents.size()));
        assertBoundaryWalls(field);
    }

    @Test
    void start_placesBasePlayerAndExitCoordinates() {
        manager.start();

        Player player = assertUnitAt(field, 2, 8, Player.class);
        Exit exit = assertUnitAt(field, 14, 3, Exit.class);

        assertSame(field.getCell(2, 8), player.owner());
        assertSame(field.getCell(14, 3), exit.owner());

        assertTrue(player.isActive());
        assertFalse(player.isDestroyed());
        assertFalse(exit.isDestroyed());
    }

    @Test
    void constructor_withNullGame_mustThrowControlledIllegalStateException() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> createManager(field, null)
        );

        assertTrue(exception.getMessage().contains("Игра"));
    }

    @Test
    void start_calledTwice_mustNotDuplicatePlayerOrOtherUniqueObjects() {
        Gamefield field = new Gamefield(11, 16);
        Game game = new Game();
        injectGameField(game, field);

        manager = new SimpleGameManager(field, game);

        manager.start();

        List<Unit> afterFirstStart = allUnits(field);
        long playersAfterFirstStart = countUnits(field, Player.class);

        manager.start();

        assertEquals(afterFirstStart.size(), allUnits(field).size());
        assertEquals(playersAfterFirstStart, countUnits(field, Player.class));
        assertEquals(1, countUnits(field, Player.class));
    }

    protected void recordCellEvents(Gamefield field, List<String> events, boolean assertStateInsideEvent) {
        for (int y = 0; y < field.getHeight(); y++) {
            for (int x = 0; x < field.getWidth(); x++) {
                final int currentX = x;
                final int currentY = y;
                Cell cell = field.getCell(currentX, currentY);

                cell.addCellActionListener(new CellActionListener() {
                    @Override
                    public void unitPlaced(CellActionEvent e) {
                        Unit unit = e.getUnit();
                        events.add(placeEvent(unit, currentX, currentY));

                        if (assertStateInsideEvent) {
                            assertSame(cell, e.getSource());
                            assertSame(cell, unit.owner());
                            assertTrue(unit.isActive());
                            assertFalse(unit.isDestroyed());
                            assertTrue(cell.getUnits().contains(unit));
                            assertSame(unit, cell.getUnit(unit.getClass()));
                        }
                    }

                    @Override
                    public void unitExtracted(CellActionEvent e) {
                        events.add("extract:" + e.getUnit().getClass().getSimpleName()
                                + ":" + currentX + ":" + currentY);

                        if (assertStateInsideEvent) {
                            assertNull(e.getUnit().owner());
                            assertFalse(cell.getUnits().contains(e.getUnit()));
                        }
                    }
                });
            }
        }
    }

    protected static List<String> expectedBoundaryWallEvents(Gamefield field) {
        List<String> expected = new ArrayList<>();

        int width = field.getWidth();
        int height = field.getHeight();

        for (int x = 0; x < width; x++) {
            expected.add("place:Wall:" + x + ":0");
            expected.add("place:Wall:" + x + ":" + (height - 1));
        }

        for (int y = 1; y < height - 1; y++) {
            expected.add("place:Wall:0:" + y);
            expected.add("place:Wall:" + (width - 1) + ":" + y);
        }

        return expected;
    }

    protected static String placeEvent(Unit unit, int x, int y) {
        return "place:" + unit.getClass().getSimpleName() + ":" + x + ":" + y;
    }

    protected static void assertBoundaryWalls(Gamefield field) {
        for (int x = 0; x < field.getWidth(); x++) {
            assertUnitAt(field, x, 0, Wall.class);
            assertUnitAt(field, x, field.getHeight() - 1, Wall.class);
        }

        for (int y = 1; y < field.getHeight() - 1; y++) {
            assertUnitAt(field, 0, y, Wall.class);
            assertUnitAt(field, field.getWidth() - 1, y, Wall.class);
        }
    }

    protected static <T extends Unit> T assertUnitAt(Gamefield field, int x, int y, Class<T> type) {
        Cell cell = field.getCell(x, y);
        Unit unit = cell.getUnit(type);

        assertNotNull(unit, "Expected " + type.getSimpleName() + " at (" + x + ", " + y + ")");
        assertTrue(type.isInstance(unit));
        assertSame(cell, unit.owner());
        assertFalse(unit.isDestroyed());

        return type.cast(unit);
    }

    protected static List<Unit> allUnits(Gamefield field) {
        List<Unit> result = new ArrayList<>();

        for (Cell cell : field) {
            result.addAll(cell.getUnits());
        }

        return result;
    }

    protected static long countUnits(Gamefield field, Class<?> type) {
        return allUnits(field).stream()
                .filter(type::isInstance)
                .count();
    }

    protected static void injectGameField(Game game, Gamefield field) {
        try {
            Field gameField = Game.class.getDeclaredField("_field");
            gameField.setAccessible(true);
            gameField.set(game, field);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Cannot inject Game._field for GameManager test", e);
        }
    }
}
