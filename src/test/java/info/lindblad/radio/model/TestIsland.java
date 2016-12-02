package info.lindblad.radio.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestIsland extends TestCase  {

    public TestIsland( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( TestIsland.class );
    }

    /**
     * Test a simple case with an island with just one transmitter tower and once receiver tower
     */
    public void testSimpleIslandConfiguration() {
        Island island = new Island(5, 5);
        island.addTransmitterTower(new TransmitterTower(1, new Point(2, 2), 3));
        island.addReceiverTower(new ReceiverTower(1, new Point(0, 0)));

        assertEquals(island.nbrOfReceiverTowers(), 1);
        assertEquals(island.nbrOfTransmitterTowers(), 1);
    }
}