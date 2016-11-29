package info.lindblad.radio;

import info.lindblad.radio.model.Point;
import info.lindblad.radio.model.TransmitterTower;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        Point point = new Point(1, 1);
        TransmitterTower transmitterTower = new TransmitterTower(1, point, 1);
        Set<Point> reaches = transmitterTower.reaches();
        Set<Point> expected = new HashSet<>(Arrays.asList(
                new Point(0, 0),
                new Point(0, 1),
                new Point(0, 2),
                new Point(1, 0),
                new Point(1, 1),
                new Point(1, 2),
                new Point(2, 0),
                new Point(2, 1),
                new Point(2, 2)
        ));
        assertEquals(reaches, expected);
    }

    public void testReachesWithIncreasedPower() {
        Point point = new Point(2, 2);
        TransmitterTower transmitterTower = new TransmitterTower(1, point, 1);
        Set<Point> reaches = transmitterTower.reachesWithIncreasedPower(1);
        Set<Point> expected = new HashSet<>(Arrays.asList(
                new Point(0, 4),
                new Point(1, 4),
                new Point(0, 3),
                new Point(0, 2),
                new Point(2, 4),
                new Point(3, 4),
                new Point(0, 1),
                new Point(0, 0),
                new Point(4, 4),
                new Point(1, 0),
                new Point(4, 3),
                new Point(2, 0),
                new Point(4, 2),
                new Point(3, 0),
                new Point(4, 1),
                new Point(4, 0)
        ));
        assertEquals(reaches, expected);
    }
}