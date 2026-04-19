package Model.services;

import Model.gamefield.Gamefield;

public class Maze {

    private boolean _isDestroyed = false;
    
    public Gamefield buildField() {
        Gamefield field = new Gamefield(5,5); //заглушки, потом убрать
        equipCells(field);
        return field;
    }

    private void equipCells(Gamefield field) {
        // TODO
    }

    public boolean isDestroyed(){
        return _isDestroyed;
    }

    public void destroy() {
        _isDestroyed = true;
    }
}
