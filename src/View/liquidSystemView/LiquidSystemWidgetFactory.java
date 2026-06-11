package View.liquidSystemView;

import Model.units.liquids.Lava;
import Model.units.liquids.LiquidSystem;
import Model.units.liquids.Water;

public class LiquidSystemWidgetFactory {

    private LiquidSystemWidgetFactory(){} //приватный потому что это util класс

    public static LiquidSystemWidget create(LiquidSystem liquid) {

        if(liquid instanceof Lava) {
            return new LavaWidget();
        }
        else if(liquid instanceof Water) {
            return new WaterWidget();
        }

        return null;
    }
}
