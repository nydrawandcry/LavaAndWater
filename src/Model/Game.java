package Model;

import Model.gamefield.Direction;
import Model.gamefield.Gamefield;
import Model.services.CollisionDetector;
import Model.services.configs.LevelConfig;
import Model.services.configs.LevelConfigReader;
import Model.services.Maze;
import Model.units.Exit;
import Model.units.Player;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;

import java.io.IOException;

public class Game {

    private boolean _isOver;
    private boolean _isWon;

    private Gamefield _field;
    private Player _player;
    private CollisionDetector _collisionDetector = new CollisionDetector();

    public void loadLevel(String fileName) throws IOException {
        LevelConfigReader reader = new LevelConfigReader();
        LevelConfig config = reader.read(fileName);

        Maze maze = new Maze(config);
        _field = maze.buildField();
        _player = findPlayer();

        _isOver = false;
        _isWon = false;
    }

    public void makeTurn(Direction dir) {
        if (_isOver) {
            return;
        }

        boolean moved = _player.moveTo(dir);

        if (!moved) {
            return;
        }

        spreadLiquids();
        _collisionDetector.resolve(_field);

        //обновление
        updateGameState();
    }

    private void spreadLiquids() {
        new Lava().expand(_field);
        new Water().expand(_field);
    }

    private void updateGameState() {
        if (isPlayerInLava()) {
            _player.kill();
            _isOver = true;
            _isWon = false;
            return;
        }

        if (isPlayerOnExit()) {
            _isOver = true;
            _isWon = true;
        }
    }

    private boolean isPlayerInLava() {
        return _player.owner().getUnit(Lava.class) != null;
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
