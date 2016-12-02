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

    /**
     * Get the power level
     *
     * @return The transmitter town power level
     */
    public int getPower() {
        return this.power;
    }

    /**
     * Set the power level
     *
     * @param power The new transmitter tower power level
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * Increase the power level
     *
     * @param powerIncrease The transmitter tower power level increase
     */
    public void increasePower(int powerIncrease) {
        this.power += powerIncrease;
    }

    /**
     * Get a set of points this transmitter can cover with signal
     *
     * @return A set of points this transmitter can cover with signal
     */
    public Set<Point> reaches() {
        return reaches(this.power);
    }

    /**
     * Get a set of new points this transmitter could cover with a given increase in power level
     *
     * @param powerIncrease The increase in power level
     * @return A set of points new this transmitter can cover with signal
     */
    public Set<Point> reachesWithIncreasedPower(int powerIncrease) {
        Set<Point> previousCovered = reaches(this.power);
        return reaches(this.power + powerIncrease).stream()
                .filter(coordinates -> !previousCovered.contains(coordinates))
                .collect(Collectors.toSet());
    }

    /**
     * Check equality between two transmitter towers
     *
     * @param o Other transmitter tower
     * @return Whether the two transmitter towers are the same
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof TransmitterTower) && (((TransmitterTower) o).getId() == getId()
                && ((TransmitterTower) o).getPoint().equals(getPoint())
                && ((TransmitterTower) o).getPower() == getPower()
        );
    }

    /**
     * Get a set of points this transmitter can cover with signal for a given power level
     *
     * @param power The power level to base calculations upon
     * @return A set of points this transmitter can cover with signal
     */
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