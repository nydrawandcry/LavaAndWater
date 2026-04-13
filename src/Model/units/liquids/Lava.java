package Model.units.liquids;

public class Lava extends Liquid{
    
    @Override
    protected Liquid createInstance() {
        return new Lava();
    }
}
