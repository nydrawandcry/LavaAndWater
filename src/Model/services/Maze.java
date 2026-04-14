package Model.services;

import Model.gamefield.Cell;
import Model.gamefield.Gamefield;
import Model.services.configs.LevelConfig;
import Model.units.Exit;
import Model.units.Player;
import Model.units.Unit;
import Model.units.impassable.IronBlock;
import Model.units.impassable.Wall;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;

import java.util.List;
import java.util.function.Supplier;

public class Maze {

    private LevelConfig _config;
    private boolean _isDestroyed;

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
        placeAll(field, _config.getWalls(),      Wall::new);
        placeAll(field, _config.getIronBlocks(), IronBlock::new);
        placeAll(field, _config.getLava(),       Lava::new);
        placeAll(field, _config.getWater(),      Water::new);
        place(field, _config.getPlayerPosition(), Player::new);
        place(field, _config.getExitPosition(),   Exit::new);
    }

    private void placeAll(Gamefield field, List<Position> positions, Supplier<Unit> factory) {
        for (Position position : positions) {
            place(field, position, factory);
        }
    }

    private void place(Gamefield field, Position position, Supplier<Unit> factory) {
        Cell cell = field.getCell(position.row(), position.col());
        Unit unit = factory.get();

        cell.putUnit(unit);
    }

    public boolean isDestroyed(){
        return _isDestroyed;
    }

    public void deactivate() {
        _isDestroyed = true;
    }

    public void destroy() {
        deactivate();
        _config = null;
    }
}
