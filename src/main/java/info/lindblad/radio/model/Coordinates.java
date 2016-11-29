package info.lindblad.radio.model;

import info.lindblad.radio.util.SimplePriorityQueue;

import java.util.Set;

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

    /**
     * Calculate the square distance between two coordinates
     *
     * @param other Another coordinates object
     * @return The square distance between the two coordinates
     */
    public int squareDistance(Coordinates other) {
        return (int)(Math.pow((other.x - this.x), 2) + Math.pow((other.y - this.y), 2));
    }

    /**
     * Calculate the distance between two coordinates
     *
     * @param other Another coordinates object
     * @return The square distance between the two coordinates
     */
    public double distance(Coordinates other) {
        return Math.sqrt(squareDistance(other));
    }

    /**
     * Determine the nearest neighbour to a given starting coordinates from a set of nearby coordinates
     *
     * @param startingCoordinates The starting coordinates
     * @param nearbyCoordinates The nearby coordinates
     * @return The closest coordinates from the list of nearby coordinates
     */
    public static Coordinates closestNeighbour(Coordinates startingCoordinates, Set<Coordinates> nearbyCoordinates) {
        Coordinates closestCoordinates = null;
        int closestDistance = Integer.MAX_VALUE;
        for (Coordinates candidateCoordinates : nearbyCoordinates) {
            int distance = candidateCoordinates.squareDistance(startingCoordinates);
            if (distance < closestDistance) {
                closestCoordinates = candidateCoordinates;
                closestDistance = distance;
            }
        }
        return closestCoordinates;
    }

    /**
     * Determine the nearest neighbours to a given starting coordinates from a set of nearby coordinates
     *
     * @param startingCoordinates The starting coordinates
     * @param nearbyCoordinates The nearby coordinates
     * @return The closest coordinates from the list of nearby coordinates
     */
    public static SimplePriorityQueue<Coordinates> closestNeighbours(Coordinates startingCoordinates, Set<Coordinates> nearbyCoordinates) {
        SimplePriorityQueue<Coordinates> closestCoordinates = new SimplePriorityQueue<Coordinates>();
        int closestDistance = Integer.MAX_VALUE;
        for (Coordinates candidateCoordinates : nearbyCoordinates) {
            int distance = candidateCoordinates.squareDistance(startingCoordinates);
            if (distance <= closestDistance) {
                closestCoordinates.put(distance, candidateCoordinates);
                closestDistance = distance;
            }
        }
        return closestCoordinates;
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
