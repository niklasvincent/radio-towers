package info.lindblad.radio.model;

import info.lindblad.radio.util.SimplePriorityQueue;

import java.util.Set;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the X coordinate for the point
     *
     * @return X coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the Y coordinate for the point
     *
     * @return Y coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * Calculate the Chebyshev distance between two points
     *
     * @param other Another point object
     * @return The Chebyshev distance between the two points
     */
    public int distance(Point other) {
        return max(abs(x - other.x), abs(y - other.y));
    }

    /**
     * Determine the nearest neighbours to a given starting point from a set of nearby points
     *
     * @param startingPoint The starting point
     * @param nearbyPoints The nearby point
     * @return The closest points from the list of nearby point
     */
    public static Set<Point> closestNeighbours(Point startingPoint, Set<Point> nearbyPoints) {
        SimplePriorityQueue<Point> closestPoints = new SimplePriorityQueue<Point>();
        int closestDistanceSoFar = Integer.MAX_VALUE;
        for (Point candidatePoint : nearbyPoints) {
            int distance = candidatePoint.distance(startingPoint);
            if (distance <= closestDistanceSoFar) {
                closestDistanceSoFar = distance;
                closestPoints.put(distance, candidatePoint);
            }
        }
        return closestPoints.pollSmallest().getValue();
    }

    /**
     * Sort a set of neighbouring points by distance
     *
     * @param startingPoint The starting point
     * @param neighbourPoints The neighbour point
     * @return The neighbouring points sorted by distance
     */
    public static SimplePriorityQueue<Point> neighboursByDistance(Point startingPoint, Set<Point> neighbourPoints) {
        SimplePriorityQueue<Point> neighbourPointsByDistance = new SimplePriorityQueue<Point>();
        for (Point candidatePoint : neighbourPoints) {
            int distance = candidatePoint.distance(startingPoint);
            neighbourPointsByDistance.put(distance, candidatePoint);
        }
        return neighbourPointsByDistance;
    }

    /**
     * Calculate the hash code for the point
     *
     * @return Hash code for the point
     */
    @Override
    public int hashCode() {
        /**
         * The hash code needs to be the same for each point with the same X and Y coordinates. It also
         * cannot collide for points that have different coordinates.
         *
         * This is key to using HashSet and HashMap throughout the application.
         *
         * Based on <http://stackoverflow.com/questions/3934100/good-hash-function-for-list-of-2-d-positions>
         */
        int hash = 17;
        hash = ((hash + x) << 5) - (hash + x);
        hash = ((hash + y) << 5) - (hash + y);
        return hash;
    }

    /**
     * Check equality between two points
     *
     * @param o Other point
     * @return Whether the two points are the same
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof Point) && (((Point) o).getX() == this.x && ((Point) o).getY() == this.y);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }

}
