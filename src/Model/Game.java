package Model;

import Model.events.PlayerActionListener;
import Model.gamefield.Direction;
import Model.gamefield.Gamefield;
import Model.services.CollisionDetector;
import Model.units.Exit;
import Model.units.moving.Player;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;

public class Game implements PlayerActionListener {

    private boolean _isOver;
    private boolean _isWon;

    private final Gamefield _field;
    private final Player _player;
    private final Lava _lava;
    private final Water _water;
    private final CollisionDetector _collisionDetector;

    public Game(Gamefield field, Player player, Lava lava, Water water) {
        if (field == null || player == null || lava == null || water == null) {
            throw new NullPointerException("Параметры игры не должны быть null");
        }

        _field = field;
        _player = player;
        _player.addPlayerActionListener(this);
        _lava = lava;
        _water = water;
        _collisionDetector = new CollisionDetector();

        _isOver = false;
        _isWon = false;
    }

    @Override
    public void playerMoved() {
        updateGameState();
        spreadLiquids();
    }

    private void spreadLiquids() {
        _lava.spread();
        _water.spread();
    }

    private void updateGameState() {
        if (isPlayerInLava()) {
            _player.deactivate();
            _isOver = true;
            _isWon = false;
            return; //тут в будущем будет сообщение о гибели игрока для GUI-классов (для изменения модельки игрока и вывода панели "поражение")
        }

        if (isPlayerOnExit()) {
            _isOver = true;
            _isWon = true; //тут в будущем будет сообщение о победе для GUI-классов
        }
    }

    private boolean isPlayerInLava() {
        return _lava.contains(_player.owner());
    }

    private boolean isPlayerOnExit() {
        return _player.owner().getUnit(Exit.class) != null;
    }

    public Gamefield getField() {
        return _field;
    }

    public Player getPlayer() {
        return _player;
    }

    public Lava getLava() {
        return _lava;
    }

    public Water getWater() {
        return _water;
    }

    public boolean isOver() {
        return _isOver;
    }

    public boolean isWon() {
        return _isWon;
    }
}
