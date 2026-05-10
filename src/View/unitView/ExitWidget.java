package View.unitView;

import Model.units.Exit;

import java.awt.*;

public class ExitWidget extends UnitWidget {

    private static int SIZE = 50;

    private Color _color;

    public ExitWidget(Exit exit, Color color) {
        super(exit);

        _color = color;
        //паписка на событяя

        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));
    }
    
    @Override
    protected Color getActiveColor() {
        return new Color(185,128, 229);
    }

    @Override
    protected Color getInactiveColor() {
        return new Color(185,30, 229);
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
