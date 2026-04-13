package Model.gamefield;

import Model.units.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
}
