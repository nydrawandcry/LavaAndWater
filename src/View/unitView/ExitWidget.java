package View.unitView;

import Model.units.Exit;

import java.awt.*;

public class ExitWidget extends UnitWidget {

    private static int SIZE = 50;

    public ExitWidget(Exit exit) {
        super(exit);

        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));

        changeColorByActivity();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color color = _unit.isActive() ? getActiveColor() : getInactiveColor();

        // фон выхода
        g2d.setColor(color);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        //рамка(...)
        g2d.setColor(new Color(185, 80, 229));
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        //мне очень захотелось сделать узор как в оригинальной игре
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int maxRadius = getWidth() / 2 - 4;

        for (int radius = maxRadius; radius > 5; radius -= (maxRadius / 3)) {
            g2d.setColor(new Color(185 - radius, 128, 229));
            g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        }
    }

    @Override
    public int getRenderPriority() {
        return 100;
    }
    
    @Override
    protected Color getActiveColor() {
        return new Color(185,128, 229);
    }

    @Override
    protected Color getInactiveColor() {
        return new Color(185,30, 229);
    }
}
