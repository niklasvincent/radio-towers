package info.lindblad.radio.model;

import java.util.LinkedList;
import java.util.List;

public class TransmitterTower extends Tower {

    private int power;

    public TransmitterTower(int id, Coordinates coordinates, int power) {
        super(id, coordinates);
        this.power = power;
    }

    public List<Coordinates> reaches() {
        LinkedList<Coordinates> covered = new LinkedList<Coordinates>();
        for (int deltaX = -1 * this.power; deltaX <= this.power; deltaX++) {
            for (int deltaY = -1 * this.power; deltaY <= this.power; deltaY++) {
                Coordinates coordinates = new Coordinates(this.coordinates.getX() + deltaX, this.coordinates.getY() + deltaY);
                covered.add(coordinates);
            }
        }
        return covered;
    }

    public String toString() {
        return String.format("Transmitter at %s with power %d", this.coordinates.toString(), this.power);
    }

}