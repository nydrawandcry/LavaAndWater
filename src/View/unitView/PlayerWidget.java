package View.unitView;

import Model.units.moving.Player;

import java.awt.*;

public class PlayerWidget extends UnitWidget{

    public PlayerWidget(Player player){
        super(player);
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
