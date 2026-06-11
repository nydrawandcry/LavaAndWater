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
import java.util.List;

public class Player extends Unit {

    private final ArrayList<PlayerMovementListener> _modelMovementListeners = new ArrayList<>();
    private final ArrayList<PlayerMovementListener> _viewMovementListeners = new ArrayList<>();

    private ArrayList<PlayerReachListener> _reachListeners = new ArrayList<>();

    @Override
    protected boolean canBelongTo(Cell cell) {
        return cell != null
                && cell.getUnit(Solid.class) == null
                && cell.getUnit(Player.class) == null;
    }

    public boolean moveTo(Direction dir) {
        if(dir == null) {
            throw new IllegalStateException("Направление не может быть null");
        }
        if(owner() == null) {
            throw new IllegalStateException("Игрок не находится на поле");
        }

        Cell destination = owner().getNeighbour(dir);

        if(destination == null) {
            return false;
        }

        InteractiveUnit unit = (InteractiveUnit) destination.getUnit(InteractiveUnit.class);

        if(unit != null) {
            unit.interact(dir);
        }

        if(canBelongTo(destination)){
            if(!owner().extractUnit(this)) {
                return false;
            }
            if(!destination.putUnit(this)) {
                return false;
            }

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
        return false;
    }

    public void addModelPlayerMovementListener(PlayerMovementListener l) {
        if (l != null && !_modelMovementListeners.contains(l)) {
            _modelMovementListeners.add(l);
        }
    }

    public void addViewPlayerMovementListener(PlayerMovementListener l) {
        if (l != null && !_viewMovementListeners.contains(l)) {
            _viewMovementListeners.add(l);
        }
    }

    public void addPlayerReachListener(PlayerReachListener l) {
        if(l != null && !_reachListeners.contains(l)){
            _reachListeners.add(l);
        }
    }

    private void firePlayerMoved() {
        for (PlayerMovementListener l : List.copyOf(_modelMovementListeners)) {
            l.playerMoved();
        }

        for (PlayerMovementListener l : List.copyOf(_viewMovementListeners)) {
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
