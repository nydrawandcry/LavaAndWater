package View.unitView;

import Model.units.solid.Wall;

import java.awt.*;

public class WallWidget extends UnitWidget{

    public WallWidget(Wall wall) {
        super(wall);
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
