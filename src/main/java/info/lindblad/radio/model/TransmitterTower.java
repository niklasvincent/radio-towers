package info.lindblad.radio.model;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class TransmitterTower extends Tower {

    private int power;

    public TransmitterTower(int id, Point point, int power) {
        super(id, point);
        this.power = power;
    }

    public int getPower() {
        return this.power;
    }

    public void increasePower(int powerIncrease) {
        this.power += powerIncrease;
    }

    public Set<Point> reaches() {
        return reaches(this.power);
    }

    public Set<Point> reachesWithIncreasedPower(int powerIncrease) {
        Set<Point> previousCovered = reaches(this.power);
        return reaches(this.power + powerIncrease).stream()
                .filter(coordinates -> !previousCovered.contains(coordinates))
                .collect(Collectors.toSet());
    }

    private Set<Point> reaches(int power) {
        HashSet<Point> covered = new HashSet<Point>();
        for (int deltaX = -1 * power; deltaX <= power; deltaX++) {
            for (int deltaY = -1 * power; deltaY <= power; deltaY++) {
                Point point = new Point(this.point.getX() + deltaX, this.point.getY() + deltaY);
                covered.add(point);
            }
        }
        return covered;
    }

    public String toString() {
        return String.format("Transmitter %d at %s with power %d", this.id, this.point.toString(), this.power);
    }

}