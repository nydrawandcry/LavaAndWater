package View.unitView;

import Model.units.Unit;

import javax.swing.*;
import java.awt.*;

public abstract class UnitWidget extends JComponent {

    protected Unit _unit;

    protected UnitWidget(Unit unit) {
        _unit = unit;
        //подписка unit на событие

        setOpaque(true);
    }

    public Unit getUnit() {
        return _unit;
    }

    protected abstract Color getActiveColor();
    protected abstract Color getInactiveColor();

    protected abstract void refresh();
    protected abstract void changeColor(Color c);

    protected void changeColorByActivity() {
        if(_unit.isActive()){
            changeColor(getActiveColor());
        } else {
            changeColor(getInactiveColor());
        }
    }
}
