package Model.units.liquids;

public class LavaTest extends LiquidSystemTest<Lava> {

    @Override
    protected Lava createLiquid() {
        return new Lava();
    }
}
