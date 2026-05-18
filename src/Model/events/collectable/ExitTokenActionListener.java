package Model.events.collectable;

import Model.units.interactive.ExitToken;

import java.util.EventListener;

public interface ExitTokenActionListener extends EventListener {
    void tokenCollected(ExitToken token);
}
