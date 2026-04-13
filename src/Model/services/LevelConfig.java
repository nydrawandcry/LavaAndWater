package Model.services;

public class LevelConfig {

    private final int _height;
    private final int _width;
    private final char [][] _layout;

    LevelConfig(int height, int width, char[][] layout) {
        _height = height;
        _width = width;
        _layout = layout;
    }
    
}
