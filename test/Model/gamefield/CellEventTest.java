package Model.gamefield;

import Model.events.cell.CellActionEvent;
import Model.events.cell.CellActionListener;
import Model.events.liquids.LiquidAppearanceInCellEvent;
import Model.events.liquids.LiquidAppearanceInCellListener;
import Model.units.liquids.LiquidSystem;
import Model.units.solid.Wall;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CellEventTest {

    private static final class TestUnit extends Unit {
        @Override
        public boolean canBelongTo(Cell cell) {
            return cell != null && cell.getUnit(TestUnit.class) == null;
        }
    }

    private static final class TestLiquidSystem extends LiquidSystem {
    }

    @Test
    void putUnit_firesUnitPlacedOnce_andListenerSeesAlreadyPlacedState() {
        Gamefield field = new Gamefield(1, 1);
        Cell cell = field.getCell(0, 0);
        TestUnit unit = new TestUnit();
        List<String> events = new ArrayList<>();

        cell.addCellActionListener(new CellActionListener() {
            @Override
            public void unitPlaced(CellActionEvent e) {
                events.add("unitPlaced");

                assertSame(cell, e.getSource());
                assertSame(unit, e.getUnit());

                // Состояние проверяется ВО ВРЕМЯ события.
                assertSame(cell, unit.owner());
                assertTrue(unit.isActive());
                assertFalse(cell.isEmpty());
                assertTrue(cell.getUnits().contains(unit));
                assertSame(unit, cell.getUnit(TestUnit.class));
            }

            @Override
            public void unitExtracted(CellActionEvent e) {
                fail("unitExtracted не должен вызываться при putUnit");
            }
        });

        // Снимок ДО события.
        assertTrue(cell.isEmpty());
        assertNull(unit.owner());
        assertFalse(unit.isActive());

        assertTrue(cell.putUnit(unit));

        // Снимок ПОСЛЕ события.
        assertEquals(List.of("unitPlaced"), events);
        assertSame(cell, unit.owner());
        assertTrue(unit.isActive());
        assertSame(unit, cell.getUnit(TestUnit.class));
    }

    @Test
    void putUnit_whenUnitCannotBelong_doesNotFireEvent_andStateIsUnchanged() {
        Gamefield field = new Gamefield(1, 1);
        Cell cell = field.getCell(0, 0);
        Wall firstWall = new Wall();
        Wall secondWall = new Wall();
        List<String> events = new ArrayList<>();

        assertTrue(cell.putUnit(firstWall));

        cell.addCellActionListener(new CellActionListener() {
            @Override
            public void unitPlaced(CellActionEvent e) {
                events.add("unitPlaced");
            }

            @Override
            public void unitExtracted(CellActionEvent e) {
                events.add("unitExtracted");
            }
        });

        assertSame(firstWall, cell.getUnit(Wall.class));
        assertNull(secondWall.owner());

        assertFalse(cell.putUnit(secondWall));

        assertTrue(events.isEmpty());
        assertSame(firstWall, cell.getUnit(Wall.class));
        assertNull(secondWall.owner());
    }

    @Test
    void extractUnit_firesUnitExtractedOnce_andListenerSeesAlreadyExtractedState() {
        Gamefield field = new Gamefield(1, 1);
        Cell cell = field.getCell(0, 0);
        TestUnit unit = new TestUnit();
        List<String> events = new ArrayList<>();

        assertTrue(cell.putUnit(unit));

        cell.addCellActionListener(new CellActionListener() {
            @Override
            public void unitPlaced(CellActionEvent e) {
                fail("unitPlaced не должен вызываться при extractUnit");
            }

            @Override
            public void unitExtracted(CellActionEvent e) {
                events.add("unitExtracted");

                assertSame(cell, e.getSource());
                assertSame(unit, e.getUnit());

                // Состояние проверяется ВО ВРЕМЯ события.
                assertNull(unit.owner());
                assertTrue(cell.isEmpty());
                assertFalse(cell.getUnits().contains(unit));
                assertNull(cell.getUnit(TestUnit.class));
            }
        });

        // Снимок ДО события.
        assertSame(cell, unit.owner());
        assertTrue(cell.getUnits().contains(unit));

        assertTrue(cell.extractUnit(unit));

        // Снимок ПОСЛЕ события.
        assertEquals(List.of("unitExtracted"), events);
        assertNull(unit.owner());
        assertTrue(cell.isEmpty());
    }

    @Test
    void setLiquidSystem_firesAddAndRemoveEvents_withStateCheckedInsideCallbacks() {
        Gamefield field = new Gamefield(1, 1);
        Cell cell = field.getCell(0, 0);
        TestLiquidSystem liquid = new TestLiquidSystem();
        List<String> events = new ArrayList<>();

        cell.addLiquidAppearanceInCellListener(new LiquidAppearanceInCellListener() {
            @Override
            public void liquidAdded(LiquidAppearanceInCellEvent e) {
                events.add("liquidAdded");

                assertSame(cell, e.getSource());
                assertSame(cell, e.getCell());
                assertSame(liquid, e.getLiquidSystem());

                // В момент добавления клетка уже указывает на жидкость.
                assertSame(liquid, cell.getLiquidSystem());
            }

            @Override
            public void liquidRemoved(LiquidAppearanceInCellEvent e) {
                events.add("liquidRemoved");

                assertSame(cell, e.getSource());
                assertSame(cell, e.getCell());
                assertSame(liquid, e.getLiquidSystem());

                // В момент удаления клетка уже очищена, но event хранит старую жидкость.
                assertNull(cell.getLiquidSystem());
            }
        });

        assertNull(cell.getLiquidSystem());

        cell.setLiquidSystem(liquid);
        assertEquals(List.of("liquidAdded"), events);
        assertSame(liquid, cell.getLiquidSystem());

        cell.setLiquidSystem(null);
        assertEquals(List.of("liquidAdded", "liquidRemoved"), events);
        assertNull(cell.getLiquidSystem());
    }
}
