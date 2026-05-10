package View.liquidSystemView;

import Model.units.liquids.LiquidSystem;

import java.awt.*;

public class WaterWidget extends LiquidSystemWidget{


    public WaterWidget(LiquidSystem liquidSystem, Color color) {
        super(liquidSystem, color);
    }

    @Override
    public Color getColor() {
        return null;
    }
}
