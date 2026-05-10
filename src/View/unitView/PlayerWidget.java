package View.unitView;

import Model.events.player.PlayerActionListener;
import Model.units.moving.Player;

import java.awt.*;

public class PlayerWidget extends UnitWidget implements PlayerActionListener {

    private static int SIZE = 50;

    private Color _color;

    public PlayerWidget(Player player, Color color){
        super(player);

        _color = color;
        player.addPlayerActionListener(this);

        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int margin = 2;
        int size = getWidth() - margin * 2;

        // обводка у игрока потемнее, шоб выделялся
        g2d.setColor(getInactiveColor());
        g2d.fillOval(margin, margin, size, size);

        //главный круг
        g2d.setColor(_color);
        g2d.fillOval(margin + 1, margin + 1, size - 2, size - 2);
    }

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

    @Override
    public void playerMoved() {
        //тут будет анимация движения игрока
    }
}
