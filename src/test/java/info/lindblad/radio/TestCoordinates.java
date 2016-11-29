package info.lindblad.radio;

import info.lindblad.radio.model.Point;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashSet;
import java.util.Set;

public class TestCoordinates
        extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestCoordinates( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( TestCoordinates.class );
    }

    public void testEqualsForSameCoordinates() {
        Point firstPoint = new Point(10, 10);
        Point secondPoint = new Point(10, 10);
        assertTrue(firstPoint.equals(secondPoint));
    }

    public void testEqualsForDifferentCoordinates() {
        Point firstPoint = new Point(10, 10);
        Point secondPoint = new Point(9, 9);
        assertFalse(firstPoint.equals(secondPoint));
    }

    public void testHashCodeForSameCoordinates() {
        Point firstPoint = new Point(10, 10);
        Point secondPoint = new Point(10, 10);
        assertEquals(firstPoint.hashCode(), secondPoint.hashCode());
    }

    public void testHashCodeForDifferentCoordinates() {
        Point firstPoint = new Point(10, 10);
        Point secondPoint = new Point(10, 10);
        assertNotSame(firstPoint.hashCode(), secondPoint.hashCode());
    }

    public void testSquareDistance() {
        Point firstPoint = new Point(5, 5);
        Point secondPoint = new Point(10, 10);
        assertEquals(50, firstPoint.squareDistance(secondPoint));
    }

    public void testNearestNeighbour() {
        Point startingPoint = new Point(5, 5);
        Set<Point> nearbyCoordinates = new HashSet<Point>();
        nearbyCoordinates.add(new Point(1, 1));
        nearbyCoordinates.add(new Point(2, 2));
        nearbyCoordinates.add(new Point(3, 3));
        nearbyCoordinates.add(new Point(10, 10));
        assertEquals(new Point(3, 3), Point.closestNeighbour(startingPoint, nearbyCoordinates));
    }
}