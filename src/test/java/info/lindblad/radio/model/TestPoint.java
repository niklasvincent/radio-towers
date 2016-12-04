package info.lindblad.radio.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashSet;
import java.util.Set;

public class TestPoint extends TestCase {

    public TestPoint(String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( TestPoint.class );
    }

    /**
     * Test that two points with the same coordinates are equal
     */
    public void testEqualsForSameCoordinates() {
        Point firstPoint = new Point(10, 10);
        Point secondPoint = new Point(10, 10);
        assertTrue(firstPoint.equals(secondPoint));
    }

    /**
     * Test that two points with different coordinates are not equal
     */
    public void testEqualsForDifferentCoordinates() {
        Point firstPoint = new Point(10, 10);
        Point secondPoint = new Point(9, 9);
        assertFalse(firstPoint.equals(secondPoint));
    }

    /**
     * Test that two points with the same coordinates have the same hash code
     */
    public void testHashCodeForSameCoordinates() {
        Point firstPoint = new Point(10, 10);
        Point secondPoint = new Point(10, 10);
        assertEquals(firstPoint.hashCode(), secondPoint.hashCode());
    }

    /**
     * Test that two points with different coordinates have different hash codes
     */
    public void testHashCodeForDifferentCoordinates() {
        Point firstPoint = new Point(10, 10);
        Point secondPoint = new Point(10, 10);
        assertNotSame(firstPoint.hashCode(), secondPoint.hashCode());
    }

    /**
     * Test the square distance calculation for two points
     */
    public void testSquareDistance() {
        Point firstPoint = new Point(5, 5);
        Point secondPoint = new Point(10, 10);
        assertEquals(5, firstPoint.distance(secondPoint));
    }

    /**
     * Test the nearest neighbour search among a set of nearby points
     */
    public void testNearestNeighbour() {
        Point startingPoint = new Point(5, 5);
        Set<Point> nearbyPoints = new HashSet<Point>();
        nearbyPoints.add(new Point(1, 1));
        nearbyPoints.add(new Point(2, 2));
        nearbyPoints.add(new Point(3, 3));
        nearbyPoints.add(new Point(10, 10));
        Set<Point> expected = new HashSet<>();
        expected.add(new Point(3, 3));
        assertEquals(expected, Point.closestNeighbours(startingPoint, nearbyPoints));
    }

    /**
     * Test that providing a negative X coordinate results in an exception
     */
    public void testIllegalXCoordinate() {
        try {
            new Point(-1, 0);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException illegalArgumentException) {
            return;
        }
    }

    /**
     * Test that providing a negative Y coordinate results in an exception
     */
    public void testIllegalYCoordinate() {
        try {
            new Point(0, -1);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException illegalArgumentException) {
            return;
        }
    }
}