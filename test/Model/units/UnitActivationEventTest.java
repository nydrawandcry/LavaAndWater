package Model.units;

import Model.events.cell.CellActionEvent;
import Model.events.cell.CellActionListener;
import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.gamefield.Unit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnitActivationEventTest {

    private static final class TestUnit extends Unit {
        @Override
        public boolean canBelongTo(Cell cell) {
            return cell != null && cell.getUnit(TestUnit.class) == null;
        }
    }

    @Test
    void activateAndDeactivate_fireActivationEvents_afterStateChange() {
        TestUnit unit = new TestUnit();
        List<String> events = new ArrayList<>();

        unit.addUnitActivationListener(() -> {
            if (unit.isActive()) {
                events.add("activated");
            } else {
                events.add("deactivated");
            }
        });

        assertFalse(unit.isActive());

        unit.activate();
        assertEquals(List.of("activated"), events);
        assertTrue(unit.isActive());

        unit.deactivate();
        assertEquals(List.of("activated", "deactivated"), events);
        assertFalse(unit.isActive());
    }

    @Test
    void destroy_extractsUnitFromCellBeforeDestroyedFlagIsSet_thenFinalStateIsDestroyed() {
        Gamefield field = new Gamefield(1, 1);
        Cell cell = field.getCell(0, 0);
        TestUnit unit = new TestUnit();
        List<String> events = new ArrayList<>();

        assertTrue(cell.putUnit(unit));

        cell.addCellActionListener(new CellActionListener() {
            @Override
            public void unitPlaced(CellActionEvent e) {
                fail("unitPlaced не должен вызываться при destroy");
            }

            @Override
            public void unitExtracted(CellActionEvent e) {
                events.add("unitExtractedDuringDestroy");

                assertSame(unit, e.getUnit());
                assertSame(cell, e.getSource());

                // Это именно промежуточный снимок destroy():
                // из клетки уже извлечен, но флаг destroyed еще не выставлен.
                assertNull(unit.owner());
                assertTrue(cell.isEmpty());
                assertFalse(unit.isDestroyed());
                assertTrue(unit.isActive());
            }
        });

        assertSame(cell, unit.owner());
        assertTrue(unit.isActive());
        assertFalse(unit.isDestroyed());

        unit.destroy();

        assertEquals(List.of("unitExtractedDuringDestroy"), events);
        assertNull(unit.owner());
        assertTrue(cell.isEmpty());
        assertTrue(unit.isDestroyed());
        assertFalse(unit.isActive());
    }
}
