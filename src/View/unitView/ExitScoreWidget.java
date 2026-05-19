package View.unitView;

import Model.events.collectable.ExitScoreActionListener;
import Model.units.interactive.ExitScore;

import java.awt.*;

public class ExitScoreWidget extends UnitWidget {

    private static int SIZE = 20;

    private Color _color;

    private ExitScoreActionListener _scoreListener = new ExitScoreActionHandler();

    public ExitScoreWidget(ExitScore score, Color color) {
        super(score);
        score.addExitTokenListener(_scoreListener);

        _color = color;

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

        // обводка потемнее
        g2d.setColor(getInactiveColor());
        g2d.fillOval(margin, margin, size, size);

        //главный круг
        g2d.setColor(_color);
        g2d.fillOval(margin + 1, margin + 1, size - 2, size - 2);
    }

    @Override
    public int getRenderPriority() {
        return 100;
    }

    @Override
    protected Color getActiveColor() {
        return new Color(191,0,255);
    }

    @Override
    protected Color getInactiveColor() {
        return new Color(86, 16, 109);
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

    private class ExitScoreActionHandler implements ExitScoreActionListener{
        @Override
        public void tokenCollected (ExitScore token) {

        }
    }
}
