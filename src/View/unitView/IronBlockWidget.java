package View.unitView;

import Model.events.units.IronBlockActionListener;
import Model.units.moving.IronBlock;

import java.awt.*;

public class IronBlockWidget extends UnitWidget implements IronBlockActionListener {

    private static int SIZE = 50;

    private Color _color;

    public IronBlockWidget(IronBlock block, Color color) {
        super(block);
        block.addIronBlockActionListener(this);

        _color = color;

        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //главное тело
        g2d.setColor(_color);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        //рамка потемнее
        g2d.setColor(new Color(80, 80, 80));
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        // имитация гвоздиков по углам (маленькие кружочки)
        g2d.setColor(new Color(80, 80, 80));
        int nailSize = getWidth() / 8;
        int offset = nailSize / 2;

        //гвоздник верхний левый
        g2d.fillOval(offset, offset, nailSize, nailSize);
        //гвоздтк верхний правый
        g2d.fillOval(getWidth() - offset - nailSize, offset, nailSize, nailSize);
        //гвоздик нижний левый
        g2d.fillOval(offset, getHeight() - offset - nailSize, nailSize, nailSize);
        //гвоздик нижний правый
        g2d.fillOval(getWidth() - offset - nailSize, getHeight() - offset - nailSize, nailSize, nailSize);

        // имитация металлического блеск (легкая линия сверху)
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawLine(2, 2, getWidth() - 3, 2);
    }

    @Override
    public int getRenderPriority() {
        return 100;
    }

    @Override
    protected Color getActiveColor() {
        return Color.GRAY;
    }

    @Override
    protected Color getInactiveColor() {
        return Color.RED;
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

    @Override
    public void ironBlockMoved() {
        //анимация как он двигается
    }
}
