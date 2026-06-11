package Model;

import Model.events.game.GameActionListener;
import Model.events.player.PlayerReachListener;
import Model.services.GameManager;
import Model.gamefield.Gamefield;
import Model.services.SimpleGameManager;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private boolean _isLost;
    private boolean _isWon;

    private Gamefield _field;

    private final PlayerReachListener _playerListener = new PlayerReachHandler();

    private ArrayList<GameActionListener> _gameListeners = new ArrayList<>();

    public void start() {
        _isLost = false;
        _isWon = false;

        _field = new Gamefield(11,16);

        GameManager manager = new SimpleGameManager(_field, this);
        manager.start();
    }

    private class PlayerReachHandler implements PlayerReachListener {
        @Override
        public void playerInLava() {
            fireGameIsLost();
        }

        @Override
        public void playerInWall() {
            fireGameIsLost();
        }

        @Override
        public void playerInExit() {
            fireGameIsWon();
        }
    }

    public PlayerReachListener getPlayerListener() {
        return _playerListener;
    }

    public Gamefield getField() {
        return _field;
    }

    public boolean isOver() {
        return _isLost || _isWon;
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
        if(!winTheGame()) {
            return;
        }

        List<GameActionListener> listenersCopy =
                new ArrayList<>(_gameListeners);

        for(GameActionListener listener : listenersCopy) {
            listener.gameIsWon();
        }
    }

    private void fireGameIsLost() {
        if(!loseTheGame()) {
            return;
        }

        List<GameActionListener> listenersCopy =
                new ArrayList<>(_gameListeners);

        for(GameActionListener listener : listenersCopy) {
            listener.gameIsLost();
        }
    }

    private boolean winTheGame() {
        //предотвращение вызова метода до инициализации игры
        if(_field == null) {
            throw new IllegalStateException("Игра не запущена");
        }

        if (isOver()) {
            return false;
        }

        _isWon = true;
        _field.deactivate();

        return true;
    }

    private boolean loseTheGame() {
        //предотвращение вызова метода до инициализации игры
        if(_field == null) {
            throw new IllegalStateException("Игра не запущена");
        }

        if (isOver()) {
            return false;
        }

        _isLost = true;
        _isWon = false;
        _field.deactivate();

        return true;
    }
}
