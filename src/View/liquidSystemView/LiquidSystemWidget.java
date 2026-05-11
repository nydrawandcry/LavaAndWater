package View.liquidSystemView;


import javax.swing.*;
import java.awt.*;

public abstract class LiquidSystemWidget extends JComponent {

    private static int SIZE = 50;

    protected Color _color;
    protected boolean _visible;

    public LiquidSystemWidget(Color color) {
        _color = color;

        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    public void setVisibleLiquid(boolean visible) {
        _visible = visible;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!_visible) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(_color);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
