package info.lindblad.radio.model;

public abstract class Tower {

    protected int id;
    protected Point point;

    Tower(int id, Point point) {
        if (id <= 0) {
            throw new IllegalArgumentException(String.format("Tower ID must be a positive integer, got %d", id));
        }
        this.id = id;
        this.point = point;
    }

    public int getId() {
        return this.id;
    }

    public Point getPoint() {
        return this.point;
    }

}

