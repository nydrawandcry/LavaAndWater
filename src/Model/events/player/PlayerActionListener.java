package Model.events.player;

import java.util.EventListener;

public interface PlayerActionListener extends EventListener {

    void playerMoved();
}
