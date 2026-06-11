package Model;

import Model.events.game.GameActionListener;
import Model.gamefield.Gamefield;
import Model.services.GameManager;
import Model.services.SimpleGameManager;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private boolean _isLost;
    private boolean _isWon;

    private Gamefield _field;

    private ArrayList<GameActionListener> _gameListeners = new ArrayList<>();

    public void start() {
        _isLost = false;
        _isWon = false;

        _field = new Gamefield(11,16);

        GameManager manager = new SimpleGameManager(_field, this);
        manager.start();
    }

    public Gamefield getField() {
        return _field;
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
        _isWon = true;

        deactivate();
    }

    private void loseTheGame() {
        _isLost = true;
        _isWon = false;

        deactivate();
    }

    private void deactivate() {
        _field.deactivate();
    }
}
