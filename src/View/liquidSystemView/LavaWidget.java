package View.liquidSystemView;

import Model.units.liquids.LiquidSystem;

import java.awt.*;

public class LavaWidget extends LiquidSystemWidget{


    public LavaWidget(LiquidSystem liquidSystem, Color color) {
        super(liquidSystem, color);
    }

    @Override
    public Color getColor() {
        return new Color(255, 100, 0);
    }
}
