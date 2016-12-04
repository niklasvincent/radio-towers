package info.lindblad.radio.util;

import info.lindblad.radio.model.Island;
import info.lindblad.radio.model.Point;
import info.lindblad.radio.model.ReceiverTower;
import info.lindblad.radio.model.TransmitterTower;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Optional;


public class TestInputParser extends TestCase {

    public TestInputParser(String testName) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite(TestInputParser.class);
    }

    /**
     * Test a simple case with an island with just one transmitter tower and one receiver tower.
     *
     *   x   x   x   x   x
     *   x   *   *   *   x
     *   x   *   T1  *   x
     *   x   *   *   *   x
     *   R1  x   x   x   x
     *
     */
    public void testParseSimpleCase() {
        Island expectedIsland = new Island(5, 5);
        expectedIsland.addTransmitterTower(new TransmitterTower(1, new Point(2, 2), 1));
        expectedIsland.addReceiverTower(new ReceiverTower(1, new Point(0, 0)));

        Optional<Island> islandOptional = InputParser.parse("5 5\n1 2 2 1\n 1 0 0\n");
        assertTrue(islandOptional.isPresent());
        assertEquals(expectedIsland, islandOptional.get());
    }

    /**
     * Test the known case from the problem statement.
     *
     *   *   *   *   *   x   x   x   x   x   x
     *   *   *   *   *   *   *   *   x   R2  x
     *   *   *   *   *   *   *   *   x   x   x
     *   T2  *   *   *   *   *   *   x   x   x
     *   *   *   T1  T4  *   *   R3  x   x   x
     *   *   *   *   *   *   *   *   x   x   x
     *   *   *   *   *   *   *   *   x   x   x
     *   *   T3  *   *   *   *   *   x   x   x
     *   R1  *   *   *   x   x   x   x   x   x
     *   *   *   *   *   x   x   x   x   x   x
     *
     */
    public void testParseKnownCase() {
        Island expectedIsland = new Island(10, 10);
        expectedIsland.addTransmitterTower(new TransmitterTower(1, new Point(2, 5), 1));
        expectedIsland.addTransmitterTower(new TransmitterTower(2, new Point(0, 6), 3));
        expectedIsland.addTransmitterTower(new TransmitterTower(3, new Point(1, 2), 2));
        expectedIsland.addTransmitterTower(new TransmitterTower(4, new Point(3, 5), 3));
        expectedIsland.addReceiverTower(new ReceiverTower(1, new Point(0, 1)));
        expectedIsland.addReceiverTower(new ReceiverTower(2, new Point(8, 8)));
        expectedIsland.addReceiverTower(new ReceiverTower(3, new Point(6, 5)));

        Optional<Island> islandOptional = InputParser.parse("10 10\n1 2 5 1\n2 0 6 3\n3 1 2 2\n4 3 5 3\n1 0 1\n2 8 8\n3 6 5\n");
        assertTrue(islandOptional.isPresent());
        assertEquals(expectedIsland, islandOptional.get());
    }

}