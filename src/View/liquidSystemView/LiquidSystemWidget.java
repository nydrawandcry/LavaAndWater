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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(_color);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public abstract Color getColor();

    @Override
    public void liquidSpread() {
        //я честно пока не знаю нужно ли это вообще ТУТ. потому что у лавы и воды разные отображения (пускай и одинаково распространяются)
        //нужно ли им отдельное переопределение? хезе...
    }
}
