package View.unitView;

import Model.events.units.UnitActivationListener;
import Model.units.Unit;

import javax.swing.*;
import java.awt.*;

public abstract class UnitWidget extends JComponent {

    protected Unit _unit;
    protected UnitActivationListener _unitListener = new UnitActivationHandler();

    protected UnitWidget(Unit unit) {
        _unit = unit;
        _unit.addUnitActivationListener(_unitListener);

        setOpaque(true);
    }

    public Unit getUnit() {
        return _unit;
    }

    public int getRenderPriority() {
        return 0;
    }

    protected abstract Color getActiveColor();
    protected abstract Color getInactiveColor();

    protected void changeColorByActivity() {
        repaint();
    }

    private class UnitActivationHandler implements UnitActivationListener {
        @Override
        public void activateChanged() {
            changeColorByActivity();
        }
    }
}
