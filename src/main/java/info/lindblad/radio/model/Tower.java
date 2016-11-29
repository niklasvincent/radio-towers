package info.lindblad.radio.model;

public abstract class Tower {

    protected int id;
    protected Coordinates coordinates;

    Tower(int id, Coordinates coordinates) {
        this.id = id;
        this.coordinates = coordinates;
    }

    public int getId() {
        return this.id;
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }

}

