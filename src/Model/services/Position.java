package Model.services;

import java.util.Objects;

public class Position {

    private final int _row;
    private final int _col;

    public Position(int row, int col) {
        _row = row;
        _col = col;
    }

    public int row() {
        return _row;
    }

    public int col() {
        return _col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        
        Position position = (Position) o;
        return _row == position._row && _col == position._col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_row, _col);
    }
}
