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

    /**
     * Test a simple case with an island with just one transmitter tower and once receiver tower
     */
    public void testSimpleCaseGoodCoverage() {
        Island island = new Island(5, 5);
        island.addTransmitterTower(new TransmitterTower(1, new Coordinates(2, 2), 3));
        island.addReceiverTower(new ReceiverTower(1, new Coordinates(0, 0)));

        assertEquals(island.nbrOfReceiverTowersWithCoverage(), 1);
        assertEquals(island.nbrOfReceiverTowersWithoutCoverage(), 0);
        assertEquals(island.nbrOfReceiverTowers(), 1);
    }

    /**
     * Test a simple case with an island with just one transmitter tower and once receiver tower
     */
    public void testSimpleCaseBadCoverage() {
        Island island = new Island(5, 5);
        island.addTransmitterTower(new TransmitterTower(1, new Coordinates(2, 2), 1));
        island.addReceiverTower(new ReceiverTower(1, new Coordinates(0, 0)));

        assertEquals(island.nbrOfReceiverTowersWithCoverage(), 0);
        assertEquals(island.nbrOfReceiverTowersWithoutCoverage(), 1);
        assertEquals(island.nbrOfReceiverTowers(), 1);
    }

    /**
     * Test the known case from the problem statement where one receiver is slightly out of range
     */
    public void testKnownBadCoverage() {
        Island island = new Island(10, 10);
        island.addTransmitterTower(new TransmitterTower(1, new Coordinates(2, 5), 1));
        island.addTransmitterTower(new TransmitterTower(2, new Coordinates(0, 6), 3));
        island.addTransmitterTower(new TransmitterTower(3, new Coordinates(1, 2), 2));
        island.addTransmitterTower(new TransmitterTower(4, new Coordinates(3, 5), 3));
        island.addReceiverTower(new ReceiverTower(1, new Coordinates(0, 1)));
        island.addReceiverTower(new ReceiverTower(2, new Coordinates(8, 8)));
        island.addReceiverTower(new ReceiverTower(3, new Coordinates(6, 5)));

        assertEquals(island.nbrOfReceiverTowersWithCoverage(), 2);
        assertEquals(island.nbrOfReceiverTowersWithoutCoverage(), 1);
        assertEquals(island.nbrOfReceiverTowers(), 3);
    }

    /**
     * Test a modified version of the known case from the problem statement
     * where all receivers are in range after because transmitter 4 has increased power
     */
    public void testKnownGoodCoverage() {
        Island island = new Island(10, 10);
        island.addTransmitterTower(new TransmitterTower(1, new Coordinates(2, 5), 1));
        island.addTransmitterTower(new TransmitterTower(2, new Coordinates(0, 6), 3));
        island.addTransmitterTower(new TransmitterTower(3, new Coordinates(1, 2), 2));
        island.addTransmitterTower(new TransmitterTower(4, new Coordinates(3, 5), 5));
        island.addReceiverTower(new ReceiverTower(1, new Coordinates(0, 1)));
        island.addReceiverTower(new ReceiverTower(2, new Coordinates(8, 8)));
        island.addReceiverTower(new ReceiverTower(3, new Coordinates(6, 5)));

        assertEquals(island.nbrOfReceiverTowersWithCoverage(), 3);
        assertEquals(island.nbrOfReceiverTowersWithoutCoverage(), 0);
        assertEquals(island.nbrOfReceiverTowers(), 3);
    }
}