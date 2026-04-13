package Model.units.liquids;

public class Water extends Liquid{

    @Override
    protected Liquid createInstance() {
        return new Water();
    }
}
