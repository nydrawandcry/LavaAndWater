package View.unitView;

import Model.units.solid.Wall;

import java.awt.*;

public class WallWidget extends UnitWidget{

    private static int SIZE = 50;

    private Color _color;

    public WallWidget(Wall wall, Color color) {
        super(wall);

        _color = color;
        //подписка на события тута

        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //квадратик
        g2d.setColor(_color);
        g2d.fillRect(0,0,getWidth(), getHeight());

        //рамка(хуямка)
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect(0,0,getWidth() - 1, getHeight() - 1);

        //узорчики внутри стены
        g2d.setColor(new Color(50, 50, 50));
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int dotSize = getWidth() / 5;
        g2d.fillOval(centerX - dotSize/2, centerY - dotSize/2, dotSize, dotSize);
    }
    @Override
    protected Color getActiveColor() {
        return new Color(76,76,76);
    }

    @Override
    protected Color getInactiveColor() {
        return new Color(Color.BLACK.getRGB());
    }

    @Override
    protected void refresh() {
        repaint();
    }

    @Override
    protected void changeColor(Color c) {
        _color = c;
        refresh();
    }
}
