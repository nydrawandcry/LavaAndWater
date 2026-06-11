package Model.events.player;

import java.util.EventListener;

public interface PlayerReachListener extends EventListener {
    void playerInLava();
    void playerInWall();
    void playerInExit();
}
