package Model.units.interactive;

import Model.events.player.PlayerMovementListener;
import Model.events.player.PlayerReachListener;
import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.units.Exit;
import Model.gamefield.Unit;
import Model.units.liquids.Lava;
import Model.units.solid.Solid;

import java.util.ArrayList;

public class Player extends Unit {

    private ArrayList<PlayerMovementListener> _listeners = new ArrayList<>();

    private ArrayList<PlayerReachListener> _reachListeners = new ArrayList<>();

    @Override
    protected boolean canBelongTo(Cell cell) {
        return cell != null && cell.getUnit(Solid.class) == null;
    }

    public boolean moveTo(Direction dir) {
        if(owner().getUnit(Solid.class) != null) {
            return false; //обработка случая "игрок в стене" (а то он из нее щас может вылехти)
        }

        Cell destination = owner().getNeighbour(dir);

        if(destination == null) {
            return false;
        }

        InteractiveUnit unit = (InteractiveUnit) destination.getUnit(InteractiveUnit.class);

        if(unit != null) {
            unit.interact(dir);
        }

        owner().extractUnit(this);
        destination.putUnit(this);
        firePlayerMoved();

        if (destination.getLiquidSystem() != null
                && destination.getLiquidSystem().contains(destination)
                && destination.getLiquidSystem().getClass().equals(Lava.class)) {
            firePlayerInLava();
        } else if (destination.getUnit(Solid.class) != null) {
            firePlayerInWall();
        } else if (destination.getUnit(Exit.class) != null && destination.getUnit(Exit.class).isActive()) {
            firePlayerInExit();
        }

        return true;
    }

    public void addPlayerActionListener(PlayerMovementListener l) {
        if(l != null && !_listeners.contains(l)){
            _listeners.add(l);
        }
    }

    public void addPlayerReachListener(PlayerReachListener l) {
        if(l != null && !_reachListeners.contains(l)){
            _reachListeners.add(l);
        }
    }

    public void removePlayerActionListener(PlayerMovementListener l) {
        if(l != null){
            _listeners.remove(l);
        }
    }

    public void firePlayerMoved() {
        for(PlayerMovementListener l : _listeners) {
            l.playerMoved();
        }
    }

    private void firePlayerInLava() {
        for(PlayerReachListener l : _reachListeners) {
            l.playerInLava();
        }
    }

    private void firePlayerInWall() {
        for(PlayerReachListener l : _reachListeners) {
            l.playerInWall();
        }
    }

    private void firePlayerInExit() {
        for(PlayerReachListener l : _reachListeners) {
            l.playerInExit();
        }
    }
}
