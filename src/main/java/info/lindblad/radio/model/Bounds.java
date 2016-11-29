package info.lindblad.radio.model;


import info.lindblad.radio.model.Point;

public class Bounds {

    private int sizeX;
    private int sizeY;

    public Bounds(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public boolean contains(Point point) {
        return point.getX() >= 0 && point.getX() < sizeX && point.getY() >= 0 && point.getY() < sizeY;
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    /**
     * Check equality between two bounds
     *
     * @param o Other bounds
     * @return Whether the two bounds are the same
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof Bounds) && (((Bounds) o).getSizeX() == getSizeX()
                && ((Bounds) o).getSizeX() == getSizeY()
        );
    }

}
