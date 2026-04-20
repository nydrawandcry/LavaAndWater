package Model.units.liquids;

public class WaterTest extends LiquidSystemTest<Water> {
    @Override
    protected Water createLiquid() {
        return new Water();
    }
}
