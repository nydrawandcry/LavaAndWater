package Model.gamefield;

import Model.units.Unit;

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
            throw new IndexOutOfBoundsException("Размеры поля должны быть положительные");
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

    public void destroy() {
        for(Cell cell : _cells){
            ArrayList<Unit> units = cell.getUnits(Unit.class);
            for(Unit u : units){
                u.deactivate();
            }
        }
        _isDestroyed = true;
    }

    private void initializeCells() {
        //создание клеток
        for(int y = 0; y < _height; ++y){
            for(int x = 0; x < _width; ++x){
                _cells.add(new Cell(this));
            }
        }

        for(int y = 0; y < getHeight(); ++y) {
            for(int x = 0; x < getWidth(); ++x) {
                Cell cell = getCell(x, y);

                if (x < getWidth() - 1) {
                    cell.setNeighbour(Direction.EAST, getCell(x + 1, y));
                }
                if (x > 0) {
                    cell.setNeighbour(Direction.WEST, getCell(x - 1, y));
                }
                if (y < getHeight() - 1) {
                    cell.setNeighbour(Direction.SOUTH, getCell(x, y + 1));
                }
                if (y > 0) {
                    cell.setNeighbour(Direction.NORTH, getCell(x, y - 1));
                }
            }
        }
    }

    public Cell getCell(int posX, int posY){
        if(posX < 0 || posY < 0 || posX >= getWidth() || posY >= getHeight()) {
            throw new IndexOutOfBoundsException("Клетки с такой позицией не существует");
        }

        int index = posY * getWidth() + posX;

        return _cells.get(index);
    }

    @Override
    public Iterator<Cell> iterator() {
        return _cells.iterator();
    }
}
