package Model.events.cell;

import Model.gamefield.Unit;

import java.util.EventObject;

public class CellActionEvent extends EventObject {

    private final Unit _unit;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @param unit unit connected to current Cell
     * @throws IllegalArgumentException if source is null
     */
    public CellActionEvent(Object source, Unit unit) {
        super(source);
        _unit = unit;
    }

    public Unit getUnit() {
        return _unit;
    }
}
