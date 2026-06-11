package Model.units.liquids;

import Model.events.cell.CellActionEvent;
import Model.events.cell.CellActionListener;
import Model.events.liquids.LiquidAppearanceInCellEvent;
import Model.events.liquids.LiquidAppearanceInCellListener;
import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.services.CollisionDetector;
import Model.units.solid.Wall;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LiquidSystemEventTest {

    private static final class TestLiquidSystem extends LiquidSystem {
    }

    @Test
    void addSource_firesLiquidAdded_andCallbackSeesBidirectionalSourceState() {
        Gamefield field = new Gamefield(1, 1);
        Cell source = field.getCell(0, 0);
        TestLiquidSystem liquid = new TestLiquidSystem();
        List<String> events = new ArrayList<>();

        source.addLiquidAppearanceInCellListener(new LiquidAppearanceInCellListener() {
            @Override
            public void liquidAdded(LiquidAppearanceInCellEvent e) {
                events.add("liquidAdded");

                assertSame(source, e.getCell());
                assertSame(liquid, e.getLiquidSystem());

                // Для addSource обе стороны связи уже согласованы.
                assertSame(liquid, source.getLiquidSystem());
                assertTrue(liquid.contains(source));
            }

            @Override
            public void liquidRemoved(LiquidAppearanceInCellEvent e) {
                fail("liquidRemoved не должен вызываться при addSource");
            }
        });

        assertNull(source.getLiquidSystem());
        assertFalse(liquid.contains(source));

        liquid.addSource(source);

        assertEquals(List.of("liquidAdded"), events);
        assertSame(liquid, source.getLiquidSystem());
        assertTrue(liquid.contains(source));
    }

    @Test
    void remove_firesLiquidRemoved_andCallbackSeesAlreadyRemovedState() {
        Gamefield field = new Gamefield(1, 1);
        Cell source = field.getCell(0, 0);
        TestLiquidSystem liquid = new TestLiquidSystem();

        liquid.addSource(source);

        List<String> events = new ArrayList<>();

        source.addLiquidAppearanceInCellListener(new LiquidAppearanceInCellListener() {
            @Override
            public void liquidAdded(LiquidAppearanceInCellEvent e) {
                fail("liquidAdded не должен вызываться при remove");
            }

            @Override
            public void liquidRemoved(LiquidAppearanceInCellEvent e) {
                events.add("liquidRemoved");

                assertSame(source, e.getCell());
                assertSame(liquid, e.getLiquidSystem());

                assertNull(source.getLiquidSystem());
                assertFalse(liquid.contains(source));
            }
        });

        assertSame(liquid, source.getLiquidSystem());
        assertTrue(liquid.contains(source));

        liquid.remove(source);

        assertEquals(List.of("liquidRemoved"), events);
        assertNull(source.getLiquidSystem());
        assertFalse(liquid.contains(source));
    }

    @Test
    void liquidConflict_detectorRunsBeforeVisualCollisionListener_andVisualSeesResolvedCell() {
        Gamefield field = new Gamefield(1, 2);
        Cell lavaSource = field.getCell(0, 0);
        Cell conflictCell = field.getCell(1, 0);

        TestLiquidSystem lava = new TestLiquidSystem();
        TestLiquidSystem water = new TestLiquidSystem();
        CollisionDetector detector = new CollisionDetector();

        water.addSource(conflictCell);
        lava.addSource(lavaSource);

        List<String> events = new ArrayList<>();

        conflictCell.addLiquidAppearanceInCellListener(new LiquidAppearanceInCellListener() {
            @Override
            public void liquidAdded(LiquidAppearanceInCellEvent e) {
                fail("В конфликтной клетке не должно быть нового liquidAdded");
            }

            @Override
            public void liquidRemoved(LiquidAppearanceInCellEvent e) {
                events.add("liquidRemovedByDetector");

                assertSame(water, e.getLiquidSystem());
                assertNull(conflictCell.getLiquidSystem());
                assertFalse(water.contains(conflictCell));

                // Стена появится следующим событием.
                assertNull(conflictCell.getUnit(Wall.class));
            }
        });

        conflictCell.addCellActionListener(new CellActionListener() {
            @Override
            public void unitPlaced(CellActionEvent e) {
                events.add("wallPlacedByDetector");

                assertTrue(e.getUnit() instanceof Wall);
                assertNull(conflictCell.getLiquidSystem());
                assertSame(e.getUnit(), conflictCell.getUnit(Wall.class));
                assertFalse(water.contains(conflictCell));
            }

            @Override
            public void unitExtracted(CellActionEvent e) {
                fail("В конфликтном сценарии unitExtracted не ожидается");
            }
        });

        // Сначала вычислительный listener: CollisionDetector.
        lava.addLiquidSystemCollisionListener(detector.getLiquidListener());

        // Затем визуальный listener: он должен увидеть уже разрешенный конфликт.
        lava.addLiquidSystemCollisionListener(cell -> {
            events.add("conflictVisual");

            assertSame(conflictCell, cell);
            assertNull(conflictCell.getLiquidSystem());
            assertFalse(water.contains(conflictCell));
            assertSame(conflictCell.getUnit(Wall.class), conflictCell.getUnit(Wall.class));
            assertNotNull(conflictCell.getUnit(Wall.class));
        });

        assertSame(water, conflictCell.getLiquidSystem());
        assertTrue(water.contains(conflictCell));
        assertNull(conflictCell.getUnit(Wall.class));

        lava.spread();

        assertEquals(
                List.of("liquidRemovedByDetector", "wallPlacedByDetector", "conflictVisual"),
                events
        );

        assertNull(conflictCell.getLiquidSystem());
        assertFalse(water.contains(conflictCell));
        assertFalse(lava.contains(conflictCell));
        assertNotNull(conflictCell.getUnit(Wall.class));
    }
}
