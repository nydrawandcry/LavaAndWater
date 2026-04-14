package Model.units;

import Model.units.impassable.Wall;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class ExitTest extends AbstractUnitTest<Exit> {

    @Override
    protected Exit createUnit() {
        return new Exit();
    }

    @Test
    void canBelongTo_emptyCell_returnsTrue() {
        assertTrue(unit.canBelongTo(cell));
    }

    @Test
    void canBelongTo_cellWithWall_returnsFalse() {
        cell.putUnit(new Wall());

        assertFalse(unit.canBelongTo(cell));
    }

    @Test
    void canShareCellWithPlayer() {
        cell.putUnit(unit);
        Player player = new Player();

        boolean result = cell.putUnit(player);

        assertTrue(result);
        assertNotNull(cell.getUnit(Exit.class));
        assertNotNull(cell.getUnit(Player.class));
    }
}
