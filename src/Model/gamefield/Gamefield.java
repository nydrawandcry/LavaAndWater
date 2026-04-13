package Model.gamefield;

import java.util.ArrayList;
import java.util.Iterator;

public class Gamefield implements Iterable<Cell> {

    private int _height;
    private int _width;
    private ArrayList<Cell> _cells = new ArrayList<>();
    private boolean _isDestroyed;

    public int getHeight(){
        return _height;
    }

    public int getWidth(){
        return _width;
    }

    public boolean isDestroyed(){
        return _isDestroyed;
    }

    public Cell getCell(int posX, int posY){
        if(posX < 0 || posY < 0 || posX >= getHeight() || posY >= getWidth()) {
            throw new IllegalArgumentException("Клетки с такой позицией не существует");
        }

        int index = posX * getWidth() * posY;

        return _cells.get(index);
    }

    @Override
    public Iterator<Cell> iterator() {
        return _cells.iterator();
    }
}
