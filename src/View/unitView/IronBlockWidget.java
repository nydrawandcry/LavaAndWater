package View.unitView;

import Model.events.units.IronBlockActionListener;
import Model.units.moving.IronBlock;

import java.awt.*;

public class IronBlockWidget extends UnitWidget implements IronBlockActionListener {

    private static int SIZE = 50;

    private Color _color;

    public IronBlockWidget(IronBlock block, Color color) {
        super(block);
        //тут подписка на события

        _color = color;

        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    //todo потом наверное стоит добавить метод paintComponent для красивой отрисовки блока с округленными углами

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
