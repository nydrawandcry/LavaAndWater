package Model.services;

import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.units.Exit;
import Model.units.Player;
import Model.units.impassable.IronBlock;
import Model.units.impassable.Wall;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;

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
        placeWalls(field);
        placeIronBlocks(field);
        placeLava(field);
        placeWater(field);
        placePlayer(field);
        placeExit(field);
    }

    private void placeWalls(Gamefield field) {
        for (Position position : _config.getWalls()) {
            Cell cell = field.getCell(position.row(), position.col());
            cell.putUnit(new Wall());
        }
    }

    private void placeIronBlocks(Gamefield field) {
        for (Position position : _config.getIronBlocks()) {
            Cell cell = field.getCell(position.row(), position.col());
            cell.putUnit(new IronBlock());
        }
    }

    private void placeLava(Gamefield field) {
        for (Position position : _config.getLava()) {
            Cell cell = field.getCell(position.row(), position.col());
            cell.putUnit(new Lava());
        }
    }

    private void placeWater(Gamefield field) {
        for (Position position : _config.getWater()) {
            Cell cell = field.getCell(position.row(), position.col());
            cell.putUnit(new Water());
        }
    }

    private void placePlayer(Gamefield field) {
        Position position = _config.getPlayerPosition();
        Cell cell = field.getCell(position.row(), position.col());
        cell.putUnit(new Player());
    }

    private void placeExit(Gamefield field) {
        Position position = _config.getExitPosition();
        Cell cell = field.getCell(position.row(), position.col());
        cell.putUnit(new Exit());
    }
}
