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

    //тут потом paintComponent()

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
