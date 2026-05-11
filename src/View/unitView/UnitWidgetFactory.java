package View.unitView;

import Model.units.Exit;
import Model.units.Unit;
import Model.units.moving.IronBlock;
import Model.units.moving.Player;
import Model.units.solid.Wall;

import java.awt.*;

public class UnitWidgetFactory {

    private UnitWidgetFactory(){} //приватный потому что это util класс

    public static UnitWidget create(Unit unit) {

        if(unit instanceof Player player) {
            return new PlayerWidget(
                    player,
                    new Color(242,209,180)
            );
        }

        if(unit instanceof Wall wall) {
            return new WallWidget(
                    wall,
                    new Color(76,76,76)
            );
        }

        if(unit instanceof IronBlock ironBlock) {
            return new IronBlockWidget(
                    ironBlock,
                    new Color(150,150,150)
            );
        }

        if(unit instanceof Exit exit) {
            return new ExitWidget(
                    exit,
                    new Color(185,128,229)
            );
        }

        return null;
    }
}
