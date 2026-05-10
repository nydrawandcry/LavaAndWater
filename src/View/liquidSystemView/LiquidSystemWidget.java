package View.liquidSystemView;

import Model.units.liquids.LiquidSystem;

import javax.swing.*;
import java.awt.*;

public abstract class LiquidSystemWidget extends JComponent {

    private static int SIZE = 50;

    protected LiquidSystem _liquidSystem;
    protected Color _color;

    public LiquidSystemWidget(LiquidSystem liquidSystem, Color color) {
        _liquidSystem = liquidSystem;
        _color = color;

        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    public abstract Color getColor();
}
