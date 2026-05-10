package Model.events.units;

import java.util.EventListener;

public interface UnitActivationListener extends EventListener {

    void activateChanged();
}
