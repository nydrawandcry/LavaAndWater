package Model.gamefield;

public enum Direction {

    NORTH(0),
    SOUTH(6),
    EAST(3),
    WEST(9);

    private final int _hours;

    Direction(int hours){
        _hours = hours % 12;
    }
    
}
