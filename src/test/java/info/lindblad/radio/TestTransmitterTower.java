package info.lindblad.radio;

import info.lindblad.radio.model.Coordinates;
import info.lindblad.radio.model.TransmitterTower;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.List;

public class TestTransmitterTower
        extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestTransmitterTower( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( TestTransmitterTower.class );
    }

    public void testReaches() {
        Coordinates coordinates = new Coordinates(1, 1);
        TransmitterTower transmitterTower = new TransmitterTower(1, coordinates, 1);
        List<Coordinates> reaches = transmitterTower.reaches();
        List<Coordinates> expected = Arrays.asList(
                new Coordinates(0, 0),
                new Coordinates(0, 1),
                new Coordinates(0, 2),
                new Coordinates(1, 0),
                new Coordinates(1, 1),
                new Coordinates(1, 2),
                new Coordinates(2, 0),
                new Coordinates(2, 1),
                new Coordinates(2, 2)
        );
        assertEquals(reaches, expected);
    }
}