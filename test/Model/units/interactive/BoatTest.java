package Model.units.interactive;

import Model.gamefield.Cell;
import Model.units.AbstractUnitTest;
import Model.units.liquids.Water;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;

/** Тестовый класс класса Boat
 * Аспекты тестирования
 * Ситуации, в которых происходят попытки движения: вокруг пусто, вокруг вода
 * Прохождение игрока "сквозь" лодку: проходит, толкает
 *
 */
public class BoatTest extends AbstractUnitTest<Boat> {
    @Override
    protected Boat createUnit() {
        return new Boat();
    }

    @Test
    void push_noWater() {
        Cell target = field.getCell(1,2);

        assertTrue(unit.canBelongTo(target));
    }

    @Test
    void push_water(){
        Cell target = field.getCell(1,2);
        target.setLiquidSystem(new Water());

        assertFalse(unit.canBelongTo(target));
    }

    @Test
    void playerMoveTo_getThrough() {
        Cell target = field.getCell(1, 2);
        target.putUnit(new Boat());

        Player player = new Player();

        assertTrue(player.canBelongTo(target));
    }

    @Test
    void pushTo_cellWithAnotherBoat() {
        Cell target = field.getCell(1, 2);
        target.putUnit(new Boat());

        assertFalse(unit.canBelongTo(target));
    }
}
