package info.lindblad.radio.solver.model;


import info.lindblad.radio.model.Point;
import info.lindblad.radio.model.ReceiverTower;
import info.lindblad.radio.model.TransmitterTower;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestMatrix extends TestCase {

    public TestMatrix(String testName) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite(TestMatrix.class);
    }

    /**
     * Generate a matrix based on the known case from the problem statement where receiver two is slightly out of range.
     *
     * The expected matrix is:
     *
     *      R2
     *  T1  5
     *  T2  5
     *  T3  5
     *  T4  2
     *
     */
    public void testKnownBadCoverageCaseMatrix() {
        List<TransmitterTower> transmitterTowers = new ArrayList<>();
        transmitterTowers.add(new TransmitterTower(1, new Point(2, 5), 1));
        transmitterTowers.add(new TransmitterTower(2, new Point(0, 6), 3));
        transmitterTowers.add(new TransmitterTower(3, new Point(1, 2), 2));
        transmitterTowers.add(new TransmitterTower(4, new Point(3, 5), 3));

        // Add receiver towers
        List<ReceiverTower> receiverTowersWithoutCoverage = new ArrayList<>();
        receiverTowersWithoutCoverage.add(new ReceiverTower(2, new Point(8, 8)));

        Matrix matrix = new Matrix(transmitterTowers, receiverTowersWithoutCoverage);

        assertEquals(receiverTowersWithoutCoverage.size(), matrix.getNbrOfColumns());
        assertEquals(transmitterTowers.size(), matrix.getNbrOfRows());

        assertEquals(5, matrix.getValue(0, 0));
        assertEquals(5, matrix.getValue(0, 1));
        assertEquals(5, matrix.getValue(0, 2));
        assertEquals(2, matrix.getValue(0, 3));
    }

    /**
     * Generate a matrix based on a known test case where four receiver towers are out of signal coverage.
     *
     * The expected matrix is:
     *
     *      R1   R2   R3   R4
     *  T1  5    5    5    5
     *  T2  4    10   10   16
     *  T3  16   10   10   4
     *  T4  10   4    16   10
     *  T5  10   16   4    10
     *
     */
    public void testRadialDistributionCaseMatrix() {
        List<TransmitterTower> transmitterTowers = new ArrayList<>();
        transmitterTowers.add(new TransmitterTower(1, new Point(12, 12), 1));
        transmitterTowers.add(new TransmitterTower(2, new Point(12, 1), 1));
        transmitterTowers.add(new TransmitterTower(3, new Point(12, 23), 1));
        transmitterTowers.add(new TransmitterTower(4, new Point(1, 12), 1));
        transmitterTowers.add(new TransmitterTower(5, new Point(23, 12), 1));

        // Add receiver towers
        List<ReceiverTower> receiverTowersWithoutCoverage = new ArrayList<>();
        receiverTowersWithoutCoverage.add(new ReceiverTower(1, new Point(12, 6)));
        receiverTowersWithoutCoverage.add(new ReceiverTower(2, new Point(6, 12)));
        receiverTowersWithoutCoverage.add(new ReceiverTower(3, new Point(18, 12)));
        receiverTowersWithoutCoverage.add(new ReceiverTower(4, new Point(12, 18)));

        Matrix matrix = new Matrix(transmitterTowers, receiverTowersWithoutCoverage);

        assertEquals(receiverTowersWithoutCoverage.size(), matrix.getNbrOfColumns());
        assertEquals(transmitterTowers.size(), matrix.getNbrOfRows());

        assertEquals(5, matrix.getValue(0, 0));
        assertEquals(4, matrix.getValue(0, 1));
        assertEquals(16, matrix.getValue(0, 2));
        assertEquals(10, matrix.getValue(0, 3));
        assertEquals(10, matrix.getValue(0, 4));

        assertEquals(5, matrix.getValue(1, 0));
        assertEquals(10, matrix.getValue(1, 1));
        assertEquals(10, matrix.getValue(1, 2));
        assertEquals(4, matrix.getValue(1, 3));
        assertEquals(16, matrix.getValue(1, 4));

        assertEquals(5, matrix.getValue(2, 0));
        assertEquals(10, matrix.getValue(2, 1));
        assertEquals(10, matrix.getValue(2, 2));
        assertEquals(16, matrix.getValue(2, 3));
        assertEquals(4, matrix.getValue(2, 4));

        assertEquals(5, matrix.getValue(3, 0));
        assertEquals(16, matrix.getValue(3, 1));
        assertEquals(4, matrix.getValue(3, 2));
        assertEquals(10, matrix.getValue(3, 3));
        assertEquals(10, matrix.getValue(3, 4));
    }

    /**
     * Test that making a copy of a matrix does not end up with references to the original matrix.
     */
    public void testMatrixCopyMutations() {
        List<TransmitterTower> transmitterTowers = new ArrayList<>();
        transmitterTowers.add(new TransmitterTower(1, new Point(12, 12), 1));
        transmitterTowers.add(new TransmitterTower(2, new Point(12, 1), 1));
        transmitterTowers.add(new TransmitterTower(3, new Point(12, 23), 1));
        transmitterTowers.add(new TransmitterTower(4, new Point(1, 12), 1));
        transmitterTowers.add(new TransmitterTower(5, new Point(23, 12), 1));

        // Add receiver towers
        List<ReceiverTower> receiverTowersWithoutCoverage = new ArrayList<>();
        receiverTowersWithoutCoverage.add(new ReceiverTower(1, new Point(12, 6)));
        receiverTowersWithoutCoverage.add(new ReceiverTower(2, new Point(6, 12)));
        receiverTowersWithoutCoverage.add(new ReceiverTower(3, new Point(18, 12)));
        receiverTowersWithoutCoverage.add(new ReceiverTower(4, new Point(12, 18)));

        Matrix matrix = new Matrix(transmitterTowers, receiverTowersWithoutCoverage);

        Matrix matrixCopy = matrix.copy();
        matrixCopy.choose(0, 0);

        assertEquals(5, matrix.getValue(0, 0));
        assertEquals(4, matrix.getValue(0, 1));
        assertEquals(16, matrix.getValue(0, 2));
        assertEquals(10, matrix.getValue(0, 3));
        assertEquals(10, matrix.getValue(0, 4));

        assertEquals(5, matrix.getValue(1, 0));
        assertEquals(10, matrix.getValue(1, 1));
        assertEquals(10, matrix.getValue(1, 2));
        assertEquals(4, matrix.getValue(1, 3));
        assertEquals(16, matrix.getValue(1, 4));

        assertEquals(5, matrix.getValue(2, 0));
        assertEquals(10, matrix.getValue(2, 1));
        assertEquals(10, matrix.getValue(2, 2));
        assertEquals(16, matrix.getValue(2, 3));
        assertEquals(4, matrix.getValue(2, 4));

        assertEquals(5, matrix.getValue(3, 0));
        assertEquals(16, matrix.getValue(3, 1));
        assertEquals(4, matrix.getValue(3, 2));
        assertEquals(10, matrix.getValue(3, 3));
        assertEquals(10, matrix.getValue(3, 4));

        assertEquals(new Integer(0), matrix.getTotalPowerIncrease());
        assertEquals(new Integer(5), matrixCopy.getTotalPowerIncrease());

        assertEquals(new HashMap<TransmitterTower, Integer>(), matrix.getNewTransmitterTowerPowerLevels());
    }

    /**
     * Test that calling the choose method updates the internal state correctly.
     */
    public void testMatrixChoose() {
        List<TransmitterTower> transmitterTowers = new ArrayList<>();
        TransmitterTower transmitterTowerOne = new TransmitterTower(1, new Point(12, 12), 1);
        transmitterTowers.add(transmitterTowerOne);
        transmitterTowers.add(new TransmitterTower(2, new Point(12, 1), 1));
        transmitterTowers.add(new TransmitterTower(3, new Point(12, 23), 1));
        transmitterTowers.add(new TransmitterTower(4, new Point(1, 12), 1));
        transmitterTowers.add(new TransmitterTower(5, new Point(23, 12), 1));

        // Add receiver towers
        List<ReceiverTower> receiverTowersWithoutCoverage = new ArrayList<>();
        receiverTowersWithoutCoverage.add(new ReceiverTower(1, new Point(12, 6)));
        receiverTowersWithoutCoverage.add(new ReceiverTower(2, new Point(6, 12)));
        receiverTowersWithoutCoverage.add(new ReceiverTower(3, new Point(18, 12)));
        receiverTowersWithoutCoverage.add(new ReceiverTower(4, new Point(12, 18)));

        Matrix matrix = new Matrix(transmitterTowers, receiverTowersWithoutCoverage);

        assertEquals(5, matrix.getValue(0, 0));
        assertEquals(4, matrix.getValue(0, 1));
        assertEquals(16, matrix.getValue(0, 2));
        assertEquals(10, matrix.getValue(0, 3));
        assertEquals(10, matrix.getValue(0, 4));

        assertEquals(5, matrix.getValue(1, 0));
        assertEquals(10, matrix.getValue(1, 1));
        assertEquals(10, matrix.getValue(1, 2));
        assertEquals(4, matrix.getValue(1, 3));
        assertEquals(16, matrix.getValue(1, 4));

        assertEquals(5, matrix.getValue(2, 0));
        assertEquals(10, matrix.getValue(2, 1));
        assertEquals(10, matrix.getValue(2, 2));
        assertEquals(16, matrix.getValue(2, 3));
        assertEquals(4, matrix.getValue(2, 4));

        assertEquals(5, matrix.getValue(3, 0));
        assertEquals(16, matrix.getValue(3, 1));
        assertEquals(4, matrix.getValue(3, 2));
        assertEquals(10, matrix.getValue(3, 3));
        assertEquals(10, matrix.getValue(3, 4));

        assertEquals(new Integer(0), matrix.getTotalPowerIncrease());
        assertEquals(new HashMap<TransmitterTower, Integer>(), matrix.getNewTransmitterTowerPowerLevels());

        matrix.choose(0, 0);

        assertEquals(0, matrix.getValue(0, 0));
        assertEquals(0, matrix.getValue(1, 0));
        assertEquals(0, matrix.getValue(2, 0));
        assertEquals(0, matrix.getValue(3, 0));

        HashMap<TransmitterTower, Integer> expectedNewTransmitterTowerPowerLevels = new HashMap<>();
        expectedNewTransmitterTowerPowerLevels.put(transmitterTowerOne, 6);

        assertEquals(new Integer(5), matrix.getTotalPowerIncrease());
        assertEquals(expectedNewTransmitterTowerPowerLevels, matrix.getNewTransmitterTowerPowerLevels());
    }

}
