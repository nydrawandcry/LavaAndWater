package Model.services;

import Model.gamefield.Gamefield;

public class Maze {

    private final LevelConfig _config;

    public Maze(LevelConfig config){
        if(config == null) {
            throw new NullPointerException("Невозможно создать игру с null-конфигурацией");
        }
        _config = config;
    }
    public Gamefield buildField() {
        Gamefield field = new Gamefield(_config.getHeight(), _config.getWidth());
        equipCells(field);
        return field;
    }

    private void equipCells(Gamefield field) {

    }
}
