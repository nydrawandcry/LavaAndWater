package View.unitView;

import Model.units.moving.Player;

import java.awt.*;

public class PlayerWidget extends UnitWidget{

    private static int SIZE = 50;

    private Color _color;

    public PlayerWidget(Player player, Color color){
        super(player);

        _color = color;

        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    //paintComponent()

    @Override
    protected Color getActiveColor() {
        return new Color(242,209,180);
    }

    @Override
    protected Color getInactiveColor() {
        return new Color(170,149,129);
    }

    @Override
    protected void refresh() {
        repaint();
    }

    @Override
    protected void changeColor(Color c) {
        refresh();
    }
}
