package Model;

import Model.gamefield.Gamefield;
import Model.services.CollisionDetector;
import Model.services.Maze;
import Model.units.Player;

public class Game {

    private boolean _isOver;
    private boolean _isWon;

    private Gamefield _field;
    private Player _player;
    private CollisionDetector _collisionDetector = new CollisionDetector();

    

    public Gamefield getField() {
        return _field;
    }

    public Player getPlayer() {
        return _player;
    }

    public boolean isOver() {
        return _isOver;
    }

    public boolean isWon() {
        return _isWon;
    }
}
