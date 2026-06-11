package Model.events.liquids;

import java.util.EventListener;

public interface LiquidAppearanceInCellListener extends EventListener {

    void liquidAdded(LiquidAppearanceInCellEvent e);
    void liquidRemoved(LiquidAppearanceInCellEvent e);

}
