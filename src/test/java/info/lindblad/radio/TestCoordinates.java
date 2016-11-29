package info.lindblad.radio;

import info.lindblad.radio.model.Coordinates;
import info.lindblad.radio.model.Island;
import info.lindblad.radio.model.ReceiverTower;
import info.lindblad.radio.model.TransmitterTower;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
        Coordinates firstCoordinate = new Coordinates(10, 10);
        Coordinates secondCoordinate = new Coordinates(10, 10);
        assertTrue(firstCoordinate.equals(secondCoordinate));
    }

    public void testEqualsForDifferentCoordinates() {
        Coordinates firstCoordinate = new Coordinates(10, 10);
        Coordinates secondCoordinate = new Coordinates(9, 9);
        assertFalse(firstCoordinate.equals(secondCoordinate));
    }

    public void testHashCodeForSameCoordinates() {
        Coordinates firstCoordinate = new Coordinates(10, 10);
        Coordinates secondCoordinate = new Coordinates(10, 10);
        assertEquals(firstCoordinate.hashCode(), secondCoordinate.hashCode());
    }

    public void testHashCodeForDifferentCoordinates() {
        Coordinates firstCoordinate = new Coordinates(10, 10);
        Coordinates secondCoordinate = new Coordinates(10, 10);
        assertNotSame(firstCoordinate.hashCode(), secondCoordinate.hashCode());
    }
}