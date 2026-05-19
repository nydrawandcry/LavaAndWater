package View.unitView;

import Model.units.interactive.ExitScore;

import java.awt.*;

public class ExitScoreWidget extends UnitWidget {

    private static int SIZE = 20;

    private Color _color;

    public ExitScoreWidget(ExitScore score, Color color) {
        super(score);

        _color = color;

        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {

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
}
