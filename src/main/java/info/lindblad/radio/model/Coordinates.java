package info.lindblad.radio.model;

public class Coordinates {

    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = ((hash + x) << 5) - (hash + x);
        hash = ((hash + y) << 5) - (hash + y);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Coordinates) && (((Coordinates) o).getX() == this.x && ((Coordinates) o).getY() == this.y);
    }

}
