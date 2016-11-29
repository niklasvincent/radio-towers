package info.lindblad.radio;

import info.lindblad.radio.model.Coordinates;
import info.lindblad.radio.model.Island;
import info.lindblad.radio.model.ReceiverTower;
import info.lindblad.radio.model.TransmitterTower;
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
        Coordinates firstCoordinates = new Coordinates(10, 10);
        Coordinates secondCoordinates = new Coordinates(10, 10);
        assertTrue(firstCoordinates.equals(secondCoordinates));
    }

    public void testEqualsForDifferentCoordinates() {
        Coordinates firstCoordinates = new Coordinates(10, 10);
        Coordinates secondCoordinates = new Coordinates(9, 9);
        assertFalse(firstCoordinates.equals(secondCoordinates));
    }

    public void testHashCodeForSameCoordinates() {
        Coordinates firstCoordinates = new Coordinates(10, 10);
        Coordinates secondCoordinates = new Coordinates(10, 10);
        assertEquals(firstCoordinates.hashCode(), secondCoordinates.hashCode());
    }

    public void testHashCodeForDifferentCoordinates() {
        Coordinates firstCoordinates = new Coordinates(10, 10);
        Coordinates secondCoordinates = new Coordinates(10, 10);
        assertNotSame(firstCoordinates.hashCode(), secondCoordinates.hashCode());
    }

    public void testSquareDistance() {
        Coordinates firstCoordinates= new Coordinates(5, 5);
        Coordinates secondCoordinates = new Coordinates(10, 10);
        assertEquals(50, firstCoordinates.squareDistance(secondCoordinates));
    }

    public void testNearestNeighbour() {
        Coordinates startingCoordinates= new Coordinates(5, 5);
        Set<Coordinates> nearbyCoordinates = new HashSet<Coordinates>();
        nearbyCoordinates.add(new Coordinates(1, 1));
        nearbyCoordinates.add(new Coordinates(2, 2));
        nearbyCoordinates.add(new Coordinates(3, 3));
        nearbyCoordinates.add(new Coordinates(10, 10));
        assertEquals(new Coordinates(3, 3), Coordinates.closestNeighbour(startingCoordinates, nearbyCoordinates));
    }
}