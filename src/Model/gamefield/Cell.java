package Model.gamefield;

import Model.units.Unit;

import java.util.*;

public class Cell {

    private Map<Direction, Cell> _neighbours = new HashMap<>();
    private ArrayList<Unit> _units = new ArrayList<>();
    private Gamefield _field;

    public Cell(Gamefield field){

        if(field == null) {
            throw new NullPointerException("Игровое поле не может быть null");
        }

        _field = field;
    }

    public Gamefield getOwner(){
        return _field;
    }

    public boolean isEmpty(){
        return _units.isEmpty();
    }

    public ArrayList<Unit> getUnits(Class<?> c){
        ArrayList<Unit> res = new ArrayList<>();

        for(Unit u : _units) {
            if(u.getClass() == c){
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
    
}
