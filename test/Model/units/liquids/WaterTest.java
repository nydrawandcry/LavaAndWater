package Model.units.liquids;

public class WaterTest extends AbstractLiquidTest<Water> {
    @Override
    protected Water createUnit() {
        return new Water();
    }
}
