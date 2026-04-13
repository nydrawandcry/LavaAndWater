package Model.gamefield;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Iterator;

public class Gamefield implements Iterable<Cell> {

    private int _height;
    private int _width;
    private ArrayList<Cell> _cells = new ArrayList<>();
    private boolean _isDestroyed;

    public Gamefield(int height, int width) {
        if(height <= 0 || width <= 0) {
            throw new IllegalArgumentException("Размеры поля должны быть положительные");
        }

        _height = height;
        _width = width;

        initializeCells();
    }

    public int getHeight(){
        return _height;
    }

    public int getWidth(){
        return _width;
    }

    public boolean isDestroyed(){
        return _isDestroyed;
    }

    private void initializeCells() {
        //создание клеток
        for(int i = 0; i < _height; ++i){
            for(int j = 0; j < _width; ++j){
                _cells.add(new Cell(this));
            }
        }

        for(int i = 0; i < getHeight(); ++i) {
            for(int j = 0; j < getWidth(); ++j) {
                Cell cell = getCell(i, j);

                if (getHeight() > 1 && i < getHeight() - 1) {
                    cell.setNeighbour(Direction.SOUTH, getCell(i + 1, j));
                }
                if (i > 0) {
                    cell.setNeighbour(Direction.NORTH, getCell(i - 1, j));
                }
                if (getWidth() > 1 && j < getWidth() - 1) {
                    cell.setNeighbour(Direction.EAST, getCell(i, j + 1));
                }
                if (j > 0) {
                    cell.setNeighbour(Direction.WEST, getCell(i, j - 1));
                }
            }
        }
    }

    public Cell getCell(int posX, int posY){
        if(posX < 0 || posY < 0 || posX >= getHeight() || posY >= getWidth()) {
            throw new IllegalArgumentException("Клетки с такой позицией не существует");
        }

        int index = posX * getWidth() + posY;

        return _cells.get(index);
    }

    public void setSize(Dimension2D size) {
        if(size == null) {
            throw new NullPointerException("Размер не может быть null!");
        }

        this._height = (int)size.getHeight();
        this._width = (int)size.getWidth();

        initializeCells(); //пока не думаю, что это правильное решение, чтобы метод setSize еще и клетки инициализировал. стоит метод переименовать тогда думаю
    }

    @Override
    public Iterator<Cell> iterator() {
        return _cells.iterator();
    }
}
