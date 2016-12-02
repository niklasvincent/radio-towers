package info.lindblad.radio.model;

public abstract class Tower {

    protected int id;
    protected Point point;

    Tower(int id, Point point) {
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

