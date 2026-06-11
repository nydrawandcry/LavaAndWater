package Model.gamefield;

import Model.events.units.ActivationListener;
import Model.units.Unit;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Gamefield implements Iterable<Cell> {

    private int _height;
    private int _width;

    private boolean _isActive;

    private ArrayList<Cell> _cells = new ArrayList<>();
    private boolean _isDestroyed;

    private ArrayList<ActivationListener> _listeners = new ArrayList<>();

    public Gamefield(int height, int width) {
        if(height <= 0 || width <= 0) {
            throw new IndexOutOfBoundsException("Размеры поля должны быть положительные");
        }

        setSize(new Dimension(width, height));
        activate();
    }

    private void setSize(Dimension2D size) {
        if(isDestroyed()) {
            return;
        }
        _height = (int) size.getHeight();
        _width = (int) size.getWidth();

        initializeCells();
    }

    public int getHeight(){
        return _height;
    }

    public int getWidth(){
        return _width;
    }

    public boolean isActive() {
        return _isActive;
    }

    public boolean isDestroyed(){
        return _isDestroyed;
    }

    private void activate() {
        if(isDestroyed()) {
            return;
        }
        _isActive = true;
        fireActivateChanged();
    }

    public void deactivate() {
        if(isDestroyed()) {
            return;
        }
        for(Cell cell : _cells){
            ArrayList<Unit> units = cell.getUnits(Unit.class);
            for(Unit u : units){
                u.deactivate();
            }
        }
        _isActive = false;
        fireActivateChanged();
    }

    private void clear() {
        if(_cells == null) {
            return;
        }

        for(Cell cell : _cells) {
            cell.destroy();
        }

        _cells = null;
    }

    void destroy() {
        deactivate();
        clear();

        _listeners.clear();
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

    public void addGamefieldActivationListener(ActivationListener l) {
        if (l != null && !_listeners.contains(l)) {
            _listeners.add(l);
        }
    }

    private void fireActivateChanged() {
        if (isDestroyed()) return;
        for (ActivationListener listener : List.copyOf(_listeners)) {
            listener.activateChanged();
        }
    }

    @Override
    public Iterator<Cell> iterator() {
        return _cells.iterator();
    }
}
