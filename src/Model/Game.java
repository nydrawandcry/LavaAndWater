package Model;

import Model.events.player.PlayerActionListener;
import Model.events.game.GameActionListener;
import Model.gamefield.Gamefield;
import Model.services.CollisionDetector;
import Model.units.Exit;
import Model.units.moving.Player;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private boolean _isLost;
    private boolean _isWon;

    private final Gamefield _field;
    private final Player _player;
    private final Lava _lava;
    private final Water _water;
    private final CollisionDetector _collisionDetector;

    private final PlayerActionListener _playerListener = new PlayerMovementHandler();

    private ArrayList<GameActionListener> _gameListeners = new ArrayList<>();

    public Game(Gamefield field, Player player, Lava lava, Water water) {
        if (field == null || player == null || lava == null || water == null) {
            throw new NullPointerException("Параметры игры не должны быть null");
        }

        _field = field;
        _player = player;
        _player.addPlayerActionListener(_playerListener);
        _lava = lava;
        _water = water;
        _collisionDetector = new CollisionDetector();
        _lava.addLiquidSystemCollisionListener(_collisionDetector.getLiquidListener());
        _water.addLiquidSystemCollisionListener(_collisionDetector.getLiquidListener());
        _isLost = false;
        _isWon = false;
    }

    private class PlayerMovementHandler implements PlayerActionListener{
        @Override
        public void playerMoved () {
            spreadLiquids();
            updateGameState();
        }
    }

    private void spreadLiquids() {
        _lava.spread();
        _water.spread();
    }

    private void updateGameState() {
        if (isPlayerOnExit()) {
            fireGameIsWon(); //сообщение о победе для GUI-классов
            return;
        }

        if (isPlayerInLava()) {
            fireGameIsLost();
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
        return _isLost || _isWon;
    }

    public boolean isWon() {
        return _isWon;
    }

    public void addGameActionListener(GameActionListener l) {
        if(l != null && !_gameListeners.contains(l)){
            _gameListeners.add(l);
        }
    }

    public void removeGameActionListener(GameActionListener l) {
        if(l != null){
            _gameListeners.remove(l);
        }
    }

    private void fireGameIsWon() {
        winTheGame();

        List<GameActionListener> listenersCopy =
                new ArrayList<>(_gameListeners);

        for(GameActionListener listener : listenersCopy) {
            listener.gameIsWon();
        }
    }

    private void fireGameIsLost() {
        loseTheGame();

        List<GameActionListener> listenersCopy =
                new ArrayList<>(_gameListeners);

        for(GameActionListener listener : listenersCopy) {
            listener.gameIsLost();
        }
    }

    private void winTheGame() {
        if(_isLost) {
            return;
        }
        _player.deactivate();
        _isWon = true;
    }

    private void loseTheGame() {
        _player.deactivate();
        _isLost = true;
        _isWon = false;
    }
}
