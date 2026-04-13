package Model;

import Model.gamefield.Gamefield;
import Model.services.CollisionDetector;
import Model.services.Maze;
import Model.units.Exit;
import Model.units.Player;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;

public class Game {

    private boolean _isOver;
    private boolean _isWon;

    private Gamefield _field;
    private Player _player;
    private CollisionDetector _collisionDetector = new CollisionDetector();

    

    private void spreadLiquids() {
        new Lava().expand(_field);
        new Water().expand(_field);
    }

    private void updateGameState() {
        if (!_player.isAlive()) {
            _isOver = true;
            _isWon = false;
            return;
        }

        if (isPlayerOnExit()) {
            _isOver = true;
            _isWon = true;
        }
    }

    private boolean isPlayerOnExit() {
        return _player.owner().getUnit(Exit.class) != null;
    }

    private Player findPlayer() {
        for (var cell : _field) {
            Player p = (Player) cell.getUnit(Player.class);
            if (p != null) {
                return p;
            }
        }

        throw new IllegalStateException("Игрок не найден на поле");
    }

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
