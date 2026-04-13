package Model;

import Model.gamefield.Gamefield;
import Model.services.Maze;

public class Game {

    private boolean _isOver;
    private Gamefield _field;
    private Maze _maze;

    public Gamefield getField() {
        return _field;
    }

    public Maze getMaze() {
        return _maze;
    }
}
