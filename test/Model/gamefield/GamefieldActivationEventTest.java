package Model.gamefield;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GamefieldActivationEventTest {

    private static final class TestUnit extends Unit {
        @Override
        public boolean canBelongTo(Cell cell) {
            return cell != null && cell.getUnit(TestUnit.class) == null;
        }
    }

    @Test
    void deactivate_firesUnitActivationEventsBeforeGamefieldEvent_andAllCallbacksCheckSnapshots() {
        Gamefield field = new Gamefield(1, 2);
        Cell firstCell = field.getCell(0, 0);
        Cell secondCell = field.getCell(1, 0);

        TestUnit first = new TestUnit();
        TestUnit second = new TestUnit();

        assertTrue(firstCell.putUnit(first));
        assertTrue(secondCell.putUnit(second));

        List<String> events = new ArrayList<>();

        first.addUnitActivationListener(() -> {
            events.add("firstUnitDeactivated");

            // Unit уже деактивирован, поле еще активно.
            assertFalse(first.isActive());
            assertTrue(second.isActive());
            assertTrue(field.isActive());
        });

        second.addUnitActivationListener(() -> {
            events.add("secondUnitDeactivated");

            // Оба юнита уже деактивированы, поле еще активно.
            assertFalse(first.isActive());
            assertFalse(second.isActive());
            assertTrue(field.isActive());
        });

        field.addGamefieldActivationListener(() -> {
            events.add("fieldDeactivated");

            // Событие поля идет последним: все юниты уже деактивированы.
            assertFalse(field.isActive());
            assertFalse(first.isActive());
            assertFalse(second.isActive());
        });

        // Снимок ДО цепочки.
        assertTrue(field.isActive());
        assertTrue(first.isActive());
        assertTrue(second.isActive());

        field.deactivate();

        assertEquals(
                List.of("firstUnitDeactivated", "secondUnitDeactivated", "fieldDeactivated"),
                events
        );

        // Снимок ПОСЛЕ цепочки.
        assertFalse(field.isActive());
        assertFalse(first.isActive());
        assertFalse(second.isActive());
    }
}
