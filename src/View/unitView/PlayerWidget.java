package View.unitView;

import Model.events.player.PlayerMovementListener;
import Model.units.interactive.Player;

import java.awt.*;

public class PlayerWidget extends UnitWidget{

    private static int SIZE = 50;

    private final PlayerMovementListener _playerListener = new PlayerMovementHandler();

    public PlayerWidget(Player player){
        super(player);
        player.addPlayerActionListener(_playerListener);

        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));

        changeColorByActivity();
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

        Color color = _unit.isActive() ? getActiveColor() : getInactiveColor();

        //главный круг
        g2d.setColor(color);
        g2d.fillOval(margin + 1, margin + 1, size - 2, size - 2);
    }

    @Override
    public int getRenderPriority() {
        return 200;
    }

    @Override
    protected Color getActiveColor() {
        return new Color(242,209,180);
    }

    @Override
    protected Color getInactiveColor() {
        return new Color(170,149,129);
    }

    private class PlayerMovementHandler implements PlayerMovementListener {
        @Override
        public void playerMoved () {
            //тут будет анимация движения игрока
        }
    }
}
