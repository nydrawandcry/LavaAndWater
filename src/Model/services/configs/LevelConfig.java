package Model.services.configs;

import Model.services.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelConfig {

    private final int _height;
    private final int _width;

    private Position _playerPosition;
    private Position _exitPosition;

    private final List<Position> _walls = new ArrayList<>();
    private final List<Position> _ironBlocks = new ArrayList<>();
    private final List<Position> _lava = new ArrayList<>();
    private final List<Position> _water = new ArrayList<>();

    public LevelConfig(int height, int width) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("Размеры поля должны быть положительными");
        }

        _height = height;
        _width = width;
    }

    public int getHeight() {
        return _height;
    }

    public int getWidth() {
        return _width;
    }

    public Position getPlayerPosition() {
        return _playerPosition;
    }

    public void setPlayerPosition(Position playerPosition) {
        _playerPosition = playerPosition;
    }

    public Position getExitPosition() {
        return _exitPosition;
    }

    public void setExitPosition(Position exitPosition) {
        _exitPosition = exitPosition;
    }

    public void addWall(Position position) {
        _walls.add(position);
    }

    public void addIronBlock(Position position) {
        _ironBlocks.add(position);
    }

    public void addLava(Position position) {
        _lava.add(position);
    }

    public void addWater(Position position) {
        _water.add(position);
    }

    public List<Position> getWalls() {
        return Collections.unmodifiableList(_walls);
    }

    public List<Position> getIronBlocks() {
        return Collections.unmodifiableList(_ironBlocks);
    }

    public List<Position> getLava() {
        return Collections.unmodifiableList(_lava);
    }

    public List<Position> getWater() {
        return Collections.unmodifiableList(_water);
    }

    public void validate() {
        if (_playerPosition == null) {
            throw new IllegalStateException("Не задана стартовая позиция игрока");
        }
        if (_exitPosition == null) {
            throw new IllegalStateException("Не задана позиция выхода");
        }
    }
}
