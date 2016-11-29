package info.lindblad.radio.model;

import info.lindblad.radio.util.SimplePriorityQueue;

import java.util.Set;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
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
     * Calculate the square distance between two point
     *
     * @param other Another point object
     * @return The square distance between the two point
     */
    public int squareDistance(Point other) {
        return (int)(Math.pow((other.x - this.x), 2) + Math.pow((other.y - this.y), 2));
    }

    /**
     * Calculate the distance between two point
     *
     * @param other Another point object
     * @return The square distance between the two point
     */
    public double distance(Point other) {
        return Math.sqrt(squareDistance(other));
    }

    /**
     * Determine the nearest neighbour to a given starting point from a set of nearby point
     *
     * @param startingPoint The starting point
     * @param nearbyCoordinates The nearby point
     * @return The closest point from the list of nearby point
     */
    public static Point closestNeighbour(Point startingPoint, Set<Point> nearbyCoordinates) {
        Point closestPoint = null;
        int closestDistance = Integer.MAX_VALUE;
        for (Point candidatePoint : nearbyCoordinates) {
            int distance = candidatePoint.squareDistance(startingPoint);
            if (distance < closestDistance) {
                closestPoint = candidatePoint;
                closestDistance = distance;
            }
        }
        return closestPoint;
    }

    /**
     * Determine the nearest neighbours to a given starting point from a set of nearby point
     *
     * @param startingPoint The starting point
     * @param nearbyCoordinates The nearby point
     * @return The closest point from the list of nearby point
     */
    public static SimplePriorityQueue<Point> closestNeighbours(Point startingPoint, Set<Point> nearbyCoordinates) {
        SimplePriorityQueue<Point> closestCoordinates = new SimplePriorityQueue<Point>();
        int closestDistance = Integer.MAX_VALUE;
        for (Point candidatePoint : nearbyCoordinates) {
            int distance = candidatePoint.squareDistance(startingPoint);
            if (distance <= closestDistance) {
                closestCoordinates.put(distance, candidatePoint);
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
        return (o instanceof Point) && (((Point) o).getX() == this.x && ((Point) o).getY() == this.y);
    }

}
