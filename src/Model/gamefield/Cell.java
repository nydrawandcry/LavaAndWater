package Model.gamefield;

import Model.events.cell.CellActionEvent;
import Model.events.cell.CellActionListener;
import Model.events.liquids.LiquidAppearanceInCellEvent;
import Model.events.liquids.LiquidAppearanceInCellListener;
import Model.units.Unit;
import Model.units.liquids.LiquidSystem;

import java.util.*;

public class Cell {

    private Map<Direction, Cell> _neighbours = new HashMap<>();
    private ArrayList<Unit> _units = new ArrayList<>();
    private Gamefield _field;
    private LiquidSystem _liquidSystem;

    private ArrayList<CellActionListener> _listeners = new ArrayList<>();
    private ArrayList<LiquidAppearanceInCellListener> _liquidListeners = new ArrayList<>();

    public Cell(Gamefield field){

        if(field == null) {
            throw new NullPointerException("Игровое поле не может быть null");
        }

        _field = field;
    }

    public boolean putUnit(Unit u) {
        if(u == null) {
            return false;
        }

        if(u.owner() != null) {
            u.owner()._units.remove(u);
            u.removeOwner();
        }

        if(u.owner() != null) {
            return false;
        }

        if(!u.canBelongTo(this)){
            return false;
        }

        _units.add(u);
        u.setOwner(this);
        u.activate();

        fireUnitPlaced(u);
        return true;
    }

    public boolean extractUnit(Unit u) {
        if(u == null) {
            return false;
        }
        if(this._units.isEmpty()) {
            return false;
        }

        u.removeOwner();
        _units.remove(u);

        fireUnitExtracted(u);
        return true;
    }

    public Gamefield getOwner(){
        return _field;
    }

    public LiquidSystem getLiquidSystem() {
        return _liquidSystem;
    }

    public void setLiquidSystem(LiquidSystem liquid) {
        LiquidSystem old = _liquidSystem;
        _liquidSystem = liquid;

        if(old == null && liquid != null) {
            fireLiquidAdded(liquid);
        } else if(old != null && liquid == null) { //бля эт надо вообще?
            fireLiquidRemoved(old);
        }
    }

    public boolean isEmpty(){
        return _units.isEmpty();
    }

    public ArrayList<Unit> getUnits(Class<?> c){
        ArrayList<Unit> res = new ArrayList<>();

        for(Unit u : _units) {
            if(c.isInstance(u)){
                res.add(u);
            }
        }

        return res;
    }

    public Unit getUnit(Class<?> c){
        if(_units.isEmpty()){
            return null;
        }

        for(Unit u : _units){
            if(c.isInstance(u)){
                return u;
            }
        }
        return null;
    }

    public boolean isNeighbour(Cell cell) {
        return _neighbours.containsValue(cell);
    }

    public void setNeighbour(Direction dir, Cell neighbour) {
        if(neighbour == null || neighbour == this) {
            return;
        }
        _neighbours.put(dir, neighbour);
    }

    public Map<Direction, Cell> getNeighbours() {
        return Collections.unmodifiableMap(_neighbours);
    }

    public Cell getNeighbour(Direction dir) {
        return _neighbours.get(dir);
    }


    //----------- events -----------//

    public void addCellActionListener(CellActionListener l) {
        if(l != null && !_listeners.contains(l)){
            _listeners.add(l);
        }
    }

    public void removeCellActionListener(CellActionListener l) {
        if(l != null){
            _listeners.remove(l);
        }
    }

    public void addLiquidAppearanceInCellListener(LiquidAppearanceInCellListener l) {
        if(l != null && !_liquidListeners.contains(l)) {
            _liquidListeners.add(l);
        }
    }

    public void removeLiquidAppearanceInCellListener(LiquidAppearanceInCellListener l) {
        if(l != null) {
            _liquidListeners.remove(l);
        }
    }

    private void fireLiquidAdded(LiquidSystem liquid) {
        LiquidAppearanceInCellEvent e = new LiquidAppearanceInCellEvent(this, this, liquid);
        for(LiquidAppearanceInCellListener l : _liquidListeners) {
            l.liquidAdded(e);
        }
    }

    private void fireLiquidRemoved(LiquidSystem liquid) {
        LiquidAppearanceInCellEvent e = new LiquidAppearanceInCellEvent(this, this, liquid);
        for(LiquidAppearanceInCellListener l : _liquidListeners) {
            l.liquidRemoved(e);
        }
    }

    public void fireUnitPlaced(Unit u) {
        CellActionEvent e = new CellActionEvent(this, u);

        for(CellActionListener l : _listeners) {
            l.unitPlaced(e);
        }
    }

    public void fireUnitExtracted(Unit u) {
        CellActionEvent e = new CellActionEvent(this, u);

        for(CellActionListener l : _listeners) {
            l.unitExtracted(e);
        }
    }
}
