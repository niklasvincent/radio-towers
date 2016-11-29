package info.lindblad.radio.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class TransmitterTower extends Tower {

    private int power;

    public TransmitterTower(int id, Coordinates coordinates, int power) {
        super(id, coordinates);
        this.power = power;
    }

    public int getPower() {
        return this.power;
    }

    public void increasePower(int powerIncrease) {
        this.power += powerIncrease;
    }

    public Set<Coordinates> reaches() {
        return reaches(this.power);
    }

    public Set<Coordinates> reachesWithIncreasedPower(int powerIncrease) {
        Set<Coordinates> previousCovered = reaches(this.power);
        return reaches(this.power + powerIncrease).stream()
                .filter(coordinates -> !previousCovered.contains(coordinates))
                .collect(Collectors.toSet());
    }

    private Set<Coordinates> reaches(int power) {
        HashSet<Coordinates> covered = new HashSet<Coordinates>();
        for (int deltaX = -1 * power; deltaX <= power; deltaX++) {
            for (int deltaY = -1 * power; deltaY <= power; deltaY++) {
                Coordinates coordinates = new Coordinates(this.coordinates.getX() + deltaX, this.coordinates.getY() + deltaY);
                covered.add(coordinates);
            }
        }
        return covered;
    }

    public String toString() {
        return String.format("Transmitter %d at %s with power %d", this.id, this.coordinates.toString(), this.power);
    }

}