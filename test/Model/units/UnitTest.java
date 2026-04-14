package Model.units;

import org.junit.jupiter.api.Test;

public abstract class UnitTest {

    @Test
    public abstract void setOwnerTest();

    @Test
    public abstract void removeOwnerTest();

    @Test
    public abstract void activateTest();

    @Test
    public abstract void deactivateTest();

    @Test
    public abstract void destroy_removesUnitFromCell();
}
