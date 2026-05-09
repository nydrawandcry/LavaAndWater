package View.unitView;

import Model.units.Exit;

import java.awt.*;

public class ExitWidget extends UnitWidget {

    public ExitWidget(Exit exit) {
        super(exit);
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
