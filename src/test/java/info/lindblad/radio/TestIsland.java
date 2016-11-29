package info.lindblad.radio;

import info.lindblad.radio.model.Coordinates;
import info.lindblad.radio.model.Island;
import info.lindblad.radio.model.ReceiverTower;
import info.lindblad.radio.model.TransmitterTower;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.List;

public class TestIsland
        extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestIsland( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( TestIsland.class );
    }

    public void testCoverage() {
        Island island = new Island(10, 10);
        island.addTransmitterTower(new TransmitterTower(1, new Coordinates(2, 5), 1));
        island.addTransmitterTower(new TransmitterTower(2, new Coordinates(0, 6), 3));
        island.addTransmitterTower(new TransmitterTower(3, new Coordinates(1, 2), 2));
        island.addTransmitterTower(new TransmitterTower(4, new Coordinates(3, 5), 3));
        island.addReceiverTower(new ReceiverTower(1, new Coordinates(0, 1)));
        island.addReceiverTower(new ReceiverTower(2, new Coordinates(8, 8)));
        island.addReceiverTower(new ReceiverTower(3, new Coordinates(6, 5)));

        assertEquals(island.nbrOfReceiverTowersWithinCoverage(), 2);
        assertEquals(island.nbrOfReceiverTowers(), 3);
    }
}