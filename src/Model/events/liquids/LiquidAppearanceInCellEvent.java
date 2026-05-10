package Model.events.liquids;

import Model.gamefield.Cell;
import Model.units.liquids.LiquidSystem;

import java.util.EventObject;

public class LiquidAppearanceInCellEvent extends EventObject {

    private final Cell _cell;
    private final LiquidSystem _liquid;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public LiquidAppearanceInCellEvent(Object source, Cell cell, LiquidSystem liquid) {
        super(source);

        _cell = cell;
        _liquid = liquid;
    }

    public Cell getCell() {
        return _cell;
    }

    public LiquidSystem getLiquidSystem() {
        return _liquid;
    }
}
