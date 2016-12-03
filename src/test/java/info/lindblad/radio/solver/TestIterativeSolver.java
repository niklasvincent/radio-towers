package info.lindblad.radio.solver;

import info.lindblad.radio.model.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestIterativeSolver extends TestCase {

    public TestIterativeSolver(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestIterativeSolver.class);
    }

    /**
     * Test a simple case with an island with just one transmitter tower and one receiver tower.
     *
     * The transmitter tower has power 1, so it will not reach the receiver tower.
     *
     *   x   x   x   x   x
     *   x   *   *   *   x
     *   x   *   T1  *   x
     *   x   *   *   *   x
     *   R1  x   x   x   x
     *
     */
    public void testSimpleCaseBadCoverage() {
        Island island = new Island(5, 5);
        island.addTransmitterTower(new TransmitterTower(1, new Point(2, 2), 1));
        island.addReceiverTower(new ReceiverTower(1, new Point(0, 0)));

        assertEquals(1, Solver.nbrOfReceiverTowersWithoutCoverage(island));
        assertEquals(1, island.getNbrOfReceiverTowers());
    }

    /**
     * Test the known case from the problem statement where receiver two is slightly out of range.
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
    public void testKnownBadCoverage() {
        Island island = new Island(10, 10);
        island.addTransmitterTower(new TransmitterTower(1, new Point(2, 5), 1));
        island.addTransmitterTower(new TransmitterTower(2, new Point(0, 6), 3));
        island.addTransmitterTower(new TransmitterTower(3, new Point(1, 2), 2));
        island.addTransmitterTower(new TransmitterTower(4, new Point(3, 5), 3));
        island.addReceiverTower(new ReceiverTower(1, new Point(0, 1)));
        island.addReceiverTower(new ReceiverTower(2, new Point(8, 8)));
        island.addReceiverTower(new ReceiverTower(3, new Point(6, 5)));

        assertEquals(1, Solver.nbrOfReceiverTowersWithoutCoverage(island));
        assertEquals(3, island.getNbrOfReceiverTowers());
    }

    /**
     * Test a modified version of the known case from the problem statement
     * where all receivers are in range after because transmitter 4 has increased power.
     *
     *   *   *   *   *   *   *   *   *   *   x
     *   *   *   *   *   *   *   *   *   R2  x
     *   *   *   *   *   *   *   *   *   *   x
     *   T2  *   *   *   *   *   *   *   *   x
     *   *   *   T1  T4  *   *   R3  *   *   x
     *   *   *   *   *   *   *   *   *   *   x
     *   *   *   *   *   *   *   *   *   *   x
     *   *   T3  *   *   *   *   *   *   *   x
     *   R1  *   *   *   *   *   *   *   *   x
     *   *   *   *   *   *   *   *   *   *   x
     *
     */
    public void testKnownGoodCoverage() {
        Island island = new Island(10, 10);

        // Add transmitter towers
        island.addTransmitterTower(new TransmitterTower(1, new Point(2, 5), 1));
        island.addTransmitterTower(new TransmitterTower(2, new Point(0, 6), 3));
        island.addTransmitterTower(new TransmitterTower(3, new Point(1, 2), 2));
        island.addTransmitterTower(new TransmitterTower(4, new Point(3, 5), 5));

        // Add receiver towers
        island.addReceiverTower(new ReceiverTower(1, new Point(0, 1)));
        island.addReceiverTower(new ReceiverTower(2, new Point(8, 8)));
        island.addReceiverTower(new ReceiverTower(3, new Point(6, 5)));

        assertEquals(0, Solver.nbrOfReceiverTowersWithoutCoverage(island));
        assertEquals(3, island.getNbrOfReceiverTowers());
    }

    /**
     * Test solving the known case from the problem statement where receiver two is slightly out of range.
     *
     * The solution is to increase the power level of transmitter tower 4 to 5.
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
    public void testSolvingKnownBadCoverage() {
        Island island = new Island(10, 10);

        // Add transmitter towers
        island.addTransmitterTower(new TransmitterTower(1, new Point(2, 5), 1));
        island.addTransmitterTower(new TransmitterTower(2, new Point(0, 6), 3));
        island.addTransmitterTower(new TransmitterTower(3, new Point(1, 2), 2));
        TransmitterTower transmitterTowerFour = new TransmitterTower(4, new Point(3, 5), 3);
        island.addTransmitterTower(transmitterTowerFour);

        // Add receiver towers
        island.addReceiverTower(new ReceiverTower(1, new Point(0, 1)));
        ReceiverTower receiverTowerTwo = new ReceiverTower(2, new Point(8, 8));
        island.addReceiverTower(new ReceiverTower(3, new Point(6, 5)));
        island.addReceiverTower(receiverTowerTwo);

        Set<ReceiverTower> expectedReceiverTowersWithoutCoverage = new HashSet<>();
        expectedReceiverTowersWithoutCoverage.add(receiverTowerTwo);

        assertEquals(expectedReceiverTowersWithoutCoverage, Solver.getReceiverTowersWithoutCoverage(island));
        assertEquals(1, Solver.nbrOfReceiverTowersWithoutCoverage(island));
        assertEquals(3, island.getNbrOfReceiverTowers());

        Map<TransmitterTower, Integer> expectedRequiredTransmitterTowerChanges = new HashMap<>();
        expectedRequiredTransmitterTowerChanges.put(transmitterTowerFour, 5);

        IterativeSolver iterativeSolver = new IterativeSolver();

        Map<TransmitterTower, Integer> requiredTransmitterTowerChanges = iterativeSolver.getRequiredTransmitterTowerChanges(island);
        assertEquals(expectedRequiredTransmitterTowerChanges, requiredTransmitterTowerChanges);

        // Apply the suggested changes
        for ( Map.Entry<TransmitterTower,    Integer> change : requiredTransmitterTowerChanges.entrySet() ) {
            island.getTransmitterTowers().get(change.getKey().getPoint()).setPower(change.getValue());
        }
        assertEquals(0, Solver.nbrOfReceiverTowersWithoutCoverage(island));
    }

    /**
     * Test solving for a case where receiver two and three are both out of range.
     *
     * An inefficient solution would be to increase the power level of transmitter tower five by one
     * and that of transmitter tower four by two.
     *
     * That would lead to an overall power level increase of three.
     *
     * A more efficient solution is to instead increase the power level of transmitter four by two and by
     * doing so covering both receiver two and three.
     *
     *   *   *   *   *   x   *   *   *   x   x
     *   *   *   *   *   x   *   T4  *   x   R2
     *   *   *   *   *   x   *   *   *   x   x
     *   T2  *   *   *   x   x   x   x   x   x
     *   *   *   T1  *   x   x   R3  x   x   x
     *   *   *   *   *   x   *   *   *   x   x
     *   *   *   *   *   x   *   T5  *   x   x
     *   *   T3  *   *   x   *   *   *   x   x
     *   R1  *   *   *   x   x   x   x   x   x
     *   *   *   *   *   x   x   x   x   x   x
     *
     */
    public void testMultiReceiverCase() {
        Island island = new Island(10, 10);

        // Add transmitter towers
        island.addTransmitterTower(new TransmitterTower(1, new Point(2, 5), 1));
        island.addTransmitterTower(new TransmitterTower(2, new Point(0, 6), 3));
        island.addTransmitterTower(new TransmitterTower(3, new Point(1, 2), 2));

        TransmitterTower transmitterTowerFour = new TransmitterTower(4, new Point(6, 8), 1);
        island.addTransmitterTower(transmitterTowerFour);

        island.addTransmitterTower(new TransmitterTower(5, new Point(6, 3), 1));

        // Add receiver towers
        island.addReceiverTower(new ReceiverTower(1, new Point(0, 1)));
        ReceiverTower receiverTowerTwo = new ReceiverTower(2, new Point(9, 8));
        ReceiverTower receiverTowerThree = new ReceiverTower(3, new Point(6, 5));
        island.addReceiverTower(receiverTowerTwo);
        island.addReceiverTower(receiverTowerThree);

        // Create set of receiver towers that are expected to be out of signal coverage
        Set<ReceiverTower> expectedReceiverTowersWithoutCoverage = new HashSet<>();
        expectedReceiverTowersWithoutCoverage.add(receiverTowerTwo);
        expectedReceiverTowersWithoutCoverage.add(receiverTowerThree);

        assertEquals(expectedReceiverTowersWithoutCoverage, Solver.getReceiverTowersWithoutCoverage(island));
        assertEquals(2, Solver.nbrOfReceiverTowersWithoutCoverage(island));
        assertEquals(3, island.getNbrOfReceiverTowers());

        Map<TransmitterTower, Integer> expected = new HashMap<>();
        expected.put(transmitterTowerFour, 3);

        IterativeSolver iterativeSolver = new IterativeSolver();

        Map<TransmitterTower, Integer> requiredTransmitterTowerChanges = iterativeSolver.getRequiredTransmitterTowerChanges(island);
        assertEquals(expected, requiredTransmitterTowerChanges);

        // Apply the suggested changes
        for ( Map.Entry<TransmitterTower, Integer> change : requiredTransmitterTowerChanges.entrySet() ) {
            island.getTransmitterTowers().get(change.getKey().getPoint()).setPower(change.getValue());
        }
        assertEquals(0, Solver.nbrOfReceiverTowersWithoutCoverage(island));
    }

    /**
     * Test solving for a case where receiver two and three are both out of range.
     *
     * There are two solutions here that would yield the same overall increase in power level:
     *
     *   1. Increasing the power level of transmitter tower four by two to cover both receiver tower
     *     two and three
     *
     *  or
     *
     *    2. Increasing the power level of transmitter tower six by two to cover both receiver tower
     *     two and three
     *
     *  The iterative solver always picks the path that gives signal coverage to the most receiver towers
     *  in one change. Therefore, either transmitter tower four or six can be increased.
     *
     *   *   *   *   *   x   *   *   *   x   x
     *   *   *   *   *   x   *   T4  *   x   R2
     *   *   *   *   *   x   *   *   *   x   x
     *   T2  *   *   *   x   x   x   x   *   *
     *   *   *   T1  *   x   x   R3  x   *   T6
     *   *   *   *   *   x   *   *   *   *   *
     *   *   *   *   *   x   *   T5  *   x   x
     *   *   T3  *   *   x   *   *   *   x   x
     *   R1  *   *   *   x   x   x   x   x   x
     *   *   *   *   *   x   x   x   x   x   x
     *
     */
    public void testMultiReceiverCaseWithTie() {
        Island island = new Island(10, 10);

        // Add transmitter towers
        island.addTransmitterTower(new TransmitterTower(1, new Point(2, 5), 1));
        island.addTransmitterTower(new TransmitterTower(2, new Point(0, 6), 3));
        island.addTransmitterTower(new TransmitterTower(3, new Point(1, 2), 2));
        TransmitterTower transmitterTowerFour = new TransmitterTower(4, new Point(6, 8), 1);
        island.addTransmitterTower(transmitterTowerFour);
        island.addTransmitterTower(new TransmitterTower(5, new Point(6, 3), 1));
        TransmitterTower transmitterTowerSix = new TransmitterTower(6, new Point(9, 5), 1);
        island.addTransmitterTower(transmitterTowerSix);

        // Add receiver towers
        island.addReceiverTower(new ReceiverTower(1, new Point(0, 1)));
        ReceiverTower receiverTowerTwo = new ReceiverTower(2, new Point(9, 8));
        ReceiverTower receiverTowerThree = new ReceiverTower(3, new Point(6, 5));
        island.addReceiverTower(receiverTowerTwo);
        island.addReceiverTower(receiverTowerThree);

        // Create set of receiver towers that are expected to be out of signal coverage
        Set<ReceiverTower> expectedReceiverTowersWithoutCoverage = new HashSet<>();
        expectedReceiverTowersWithoutCoverage.add(receiverTowerTwo);
        expectedReceiverTowersWithoutCoverage.add(receiverTowerThree);

        assertEquals(expectedReceiverTowersWithoutCoverage, Solver.getReceiverTowersWithoutCoverage(island));
        assertEquals(2, Solver.nbrOfReceiverTowersWithoutCoverage(island));
        assertEquals(3, island.getNbrOfReceiverTowers());

        IterativeSolver iterativeSolver = new IterativeSolver();

        Map<TransmitterTower, Integer> requiredTransmitterTowerChanges = iterativeSolver.getRequiredTransmitterTowerChanges(island);

        // Only one change is expected and the new power level should be three
        assertEquals(1, requiredTransmitterTowerChanges.size());
        assertTrue(requiredTransmitterTowerChanges.containsValue(3));
        assertEquals(3, requiredTransmitterTowerChanges.entrySet().iterator().next().getValue().intValue());

        // Apply the suggested changes
        for ( Map.Entry<TransmitterTower, Integer> change : requiredTransmitterTowerChanges.entrySet() ) {
            island.getTransmitterTowers().get(change.getKey().getPoint()).setPower(change.getValue());
        }
        assertEquals(0, Solver.nbrOfReceiverTowersWithoutCoverage(island));
    }

    /**
     * Test solving for a case where receiver one and two are both out of range.
     *
     * Transmitter tower one could increase its power level by three and cover both
     * of the receiver towers, but a more efficient solution is to increase the power levels
     * of transmitter tower two and three by one each to achieve an overall power level increase of two.
     *
     *   *   T1  *   x   x   x   x
     *   *   *   *   x   x   x   x
     *   x   x   x   x   x   x   x
     *   x   x   x   x   x   x   x
     *   x   R1  x   x   x   R2  x
     *   *   *   *   x   *   *   *
     *   *   T2  *   x   *   T3  *
     *
     */
    public void testFavourableMultiChangeCase() {
        Island island = new Island(7, 7);

        // Add transmitter towers
        island.addTransmitterTower(new TransmitterTower(1, new Point(1, 6), 1));
        TransmitterTower transmitterTowerTwo = new TransmitterTower(2, new Point(1, 0), 1);
        TransmitterTower transmitterTowerThree = new TransmitterTower(3, new Point(5, 0), 1);
        island.addTransmitterTower(transmitterTowerTwo);
        island.addTransmitterTower(transmitterTowerThree);

        // Add receiver towers
        ReceiverTower receiverTowerOne= new ReceiverTower(1, new Point(1, 2));
        ReceiverTower receiverTowerTwo = new ReceiverTower(2, new Point(5, 2));
        island.addReceiverTower(receiverTowerOne);
        island.addReceiverTower(receiverTowerTwo);

        // Create set of receiver towers that are expected to be out of signal coverage
        Set<ReceiverTower> expectedReceiverTowersWithoutCoverage = new HashSet<>();
        expectedReceiverTowersWithoutCoverage.add(receiverTowerOne);
        expectedReceiverTowersWithoutCoverage.add(receiverTowerTwo);

        assertEquals(expectedReceiverTowersWithoutCoverage, Solver.getReceiverTowersWithoutCoverage(island));
        assertEquals(2, Solver.nbrOfReceiverTowersWithoutCoverage(island));
        assertEquals(2, island.getNbrOfReceiverTowers());

        Map<TransmitterTower, Integer> expected = new HashMap<>();
        expected.put(transmitterTowerTwo, 2);
        expected.put(transmitterTowerThree, 2);

        IterativeSolver iterativeSolver = new IterativeSolver();

        Map<TransmitterTower, Integer> requiredTransmitterTowerChanges = iterativeSolver.getRequiredTransmitterTowerChanges(island);
        assertEquals(expected, requiredTransmitterTowerChanges);

        // Apply the suggested changes
        for ( Map.Entry<TransmitterTower, Integer> change : requiredTransmitterTowerChanges.entrySet() ) {
            island.getTransmitterTowers().get(change.getKey().getPoint()).setPower(change.getValue());
        }
        assertEquals(0, Solver.nbrOfReceiverTowersWithoutCoverage(island));
    }

}