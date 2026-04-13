package Model.services;

import java.util.ArrayList;
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
    
}
