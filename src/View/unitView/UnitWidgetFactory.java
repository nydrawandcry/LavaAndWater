package View.unitView;

import Model.units.Exit;
import Model.units.Unit;
import Model.units.interactive.Boat;
import Model.units.interactive.ExitScore;
import Model.units.interactive.IronBlock;
import Model.units.interactive.Player;
import Model.units.solid.Wall;

import java.awt.*;

public class UnitWidgetFactory {

    private UnitWidgetFactory(){} //приватный потому что это util класс

    public static UnitWidget create(Unit unit) {

        if(unit instanceof Player player) {
            return new PlayerWidget(player);
        }

        if(unit instanceof Wall wall) {
            return new WallWidget(wall);
        }

        if(unit instanceof IronBlock ironBlock) {
            return new IronBlockWidget(ironBlock);
        }

        if(unit instanceof Exit exit) {
            return new ExitWidget(exit);
        }

        if(unit instanceof ExitScore score) {
            return new ExitScoreWidget(score);
        }

        if(unit instanceof Boat boat) {
            return new BoatWidget(boat);
        }

        return null;
    }
}
