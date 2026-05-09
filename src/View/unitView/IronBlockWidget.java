package View.unitView;

import Model.units.moving.IronBlock;

import java.awt.*;

public class IronBlockWidget extends UnitWidget {

    public IronBlockWidget(IronBlock block) {
        super(block);
        //четотам еще попозже добавлю
    }

    @Override
    protected Color getActiveColor() {
        return null;
    }

    @Override
    protected Color getInactiveColor() {
        return null;
    }

    @Override
    protected void refresh() {

    }

    @Override
    protected void changeColor(Color c) {

    }
}
