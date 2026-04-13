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
}
