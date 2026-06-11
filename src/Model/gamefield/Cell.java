package Model.gamefield;

import Model.events.cell.CellActionEvent;
import Model.events.cell.CellActionListener;
import Model.events.liquids.LiquidAppearanceInCellEvent;
import Model.events.liquids.LiquidAppearanceInCellListener;
import Model.units.liquids.LiquidSystem;

import java.util.*;

public class Cell {

    private Map<Direction, Cell> _neighbours = new HashMap<>();
    private ArrayList<Unit> _units = new ArrayList<>();
    private Gamefield _field;
    private LiquidSystem _liquidSystem;
    private boolean _isDestroyed;

    private ArrayList<CellActionListener> _listeners = new ArrayList<>();
    private ArrayList<LiquidAppearanceInCellListener> _liquidListeners = new ArrayList<>();

    Cell(Gamefield field){

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
            if (u.owner().extractUnit(u)) { //при старом владельце юнит полностью удаляется из клетки
                return false;
            }
        }

        if(!u.setOwner(this)){
            return false;
        }
        _units.add(u);
        u.activate();

        fireUnitPlaced(u);
        return true;
    }

    public boolean extractUnit(Unit u) {
        if(u == null) {
            return false;
        }
        if(!_units.contains(u) || u.owner() != this) { //проверка, что клетка извлекает не чужой юнит
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

    public boolean isDestroyed() {
        return _isDestroyed;
    }

    public LiquidSystem getLiquidSystem() {
        return _liquidSystem;
    }

    public void setLiquidSystem(LiquidSystem liquid) {
        LiquidSystem old = _liquidSystem;
        _liquidSystem = liquid;

        if(old == null && liquid != null) {
            fireLiquidAdded(liquid);
        } else if(old != null && liquid == null) {
            fireLiquidRemoved(old);
        }
    }

    public boolean isEmpty(){
        return _units.isEmpty();
    }

    ArrayList<Unit> getUnits(Class<?> c) {
        ArrayList<Unit> res = new ArrayList<>();

        for(Unit u : _units) {
            if(c.isInstance(u)){
                res.add(u);
            }
        }

        return res;
    }

    public Collection<Unit> getUnits() {
        return Collections.unmodifiableList(_units);
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

    void setNeighbour(Direction dir, Cell neighbour) {
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

    void destroy() {
        if(isDestroyed()) {
            return;
        }

        for(Cell neighbour : _neighbours.values()) {
            neighbour._neighbours.values().remove(this);
        }
        _neighbours.clear();

        for(Unit u : _units) {
            u.destroy();
        }

        _field = null;
        _isDestroyed = true;
    }


    //----------- events -----------//

    public void addCellActionListener(CellActionListener l) {
        if(l != null && !_listeners.contains(l)){
            _listeners.add(l);
        }
    }

    public void addLiquidAppearanceInCellListener(LiquidAppearanceInCellListener l) {
        if(l != null && !_liquidListeners.contains(l)) {
            _liquidListeners.add(l);
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

    private void fireUnitPlaced(Unit u) {
        CellActionEvent e = new CellActionEvent(this, u);

        for(CellActionListener l : _listeners) {
            l.unitPlaced(e);
        }
    }

    private void fireUnitExtracted(Unit u) {
        CellActionEvent e = new CellActionEvent(this, u);

        for(CellActionListener l : _listeners) {
            l.unitExtracted(e);
        }
    }
}
