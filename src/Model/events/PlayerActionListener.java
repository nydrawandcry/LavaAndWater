package Model.events;

import java.util.EventListener;

public interface PlayerActionListener extends EventListener {

    void playerMoved();
}
