package Model.events.game;

import java.util.EventListener;

public interface GameActionListener extends EventListener {

    void gameIsWon();
    void gameIsLost();
}
