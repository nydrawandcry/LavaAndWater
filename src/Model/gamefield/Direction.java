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

    public Direction clockwise() {
        return fromHours(_hours + 3);
    }

    public Direction anticlockwise() {
        return fromHours(_hours - 3);
    }

    public Direction opposite() {
        return fromHours(_hours + 6);
    }

    public Direction onRight() {
        return clockwise();
    }

    public Direction onLeft() {
        return anticlockwise();
    }

    private static Direction fromHours(int hours){
        int normalized = (hours % 12 + 12) % 12;

        for(Direction dir : values()){
            if(dir._hours == normalized){
                return dir;
            }
        }
        throw new IllegalArgumentException("Неверное направление");
    }

    // ---------------------------------------

    public boolean isOpposite(Direction other) {
        return this.opposite().equals(other);
    }

    @Override
    public String toString() {
        String msg = "";

        if(_hours == 0) {
            msg = "N";
        } else if(_hours == 3) {
            msg = "E";
        } else if (_hours == 6) {
            msg = "S";
        } else if(_hours == 9) {
            msg = "W";
        }

        return msg;
    }
}
