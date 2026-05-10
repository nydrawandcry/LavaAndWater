package View.liquidSystemView;

import Model.events.liquids.LiquidSystemSpreadListener;
import Model.units.liquids.LiquidSystem;

import javax.swing.*;
import java.awt.*;

public abstract class LiquidSystemWidget extends JComponent implements LiquidSystemSpreadListener {

    private static int SIZE = 50;

    protected LiquidSystem _liquidSystem;
    protected Color _color;

    public LiquidSystemWidget(LiquidSystem liquidSystem, Color color) {
        _liquidSystem = liquidSystem;
        _color = color;

        _liquidSystem.addLiquidSystemSpreadListener(this);
        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    public abstract Color getColor();

    @Override
    public void liquidSpread() {
        //я честно пока не знаю нужно ли это вообще ТУТ. потому что у лавы и воды разные отображения (пускай и одинаково распространяются)
        //нужно ли им отдельное переопределение? хезе...
    }
}
