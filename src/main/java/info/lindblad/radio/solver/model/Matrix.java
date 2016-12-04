package info.lindblad.radio.solver.model;

import info.lindblad.radio.model.ReceiverTower;
import info.lindblad.radio.model.TransmitterTower;
import info.lindblad.radio.util.SimplePriorityQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Matrix {

    private Integer totalPowerIncrease;

    private Map<TransmitterTower, AtomicInteger> newTransmitterTowerPowerLevels;

    private List<TransmitterTower> transmitterTowers;
    private List<ReceiverTower> receiverTowers;

    private int nbrOfRows;
    private int nbrOfColumns;

    private int[][] matrix;

    /**
     * Construct a matrix representation of a given transmitter/receiver configuration
     *
     * @param transmitterTowers List of transmitter towers
     * @param receiverTowersWithoutCoverage List of receiver towers that are without signal coverage
     */
    public Matrix(List<TransmitterTower> transmitterTowers, List<ReceiverTower> receiverTowersWithoutCoverage) {
        newTransmitterTowerPowerLevels = new HashMap<>();
        this.transmitterTowers = transmitterTowers;
        this.receiverTowers = receiverTowersWithoutCoverage;
        this.totalPowerIncrease = 0;
        matrix = constructInitialMatrix();
    }

    /**
     * Empty constructor that is only used internally for doing a deep copy of the matrix.
     */
    private Matrix() {

    }

    /**
     * Construct the initial matrix using the internal list of transmitter and receiver towers.
     *
     * Each row in the matrix corresponds to a transmitter tower and each column corresponds to a receiver tower.
     *
     * The value in each cell represents the required increase in power for the transmitter tower to give signal
     * coverage to the receiver tower.
     *
     * For the known provided test case, the matrix is:
     *
     *      R2
     *  T3  5
     *  T2  5
     *  T1  5
     *  T4  2
     *
     * @return The initial matrix as a 2D array of integers
     */
    private int[][] constructInitialMatrix() {
        nbrOfRows = transmitterTowers.size();
        nbrOfColumns = receiverTowers.size();

        int[][] matrix = new int[nbrOfRows][nbrOfColumns];

        for (int i = 0; i < transmitterTowers.size(); i++) {
            for (int j = 0; j < receiverTowers.size(); j++) {
                TransmitterTower transmitterTower = transmitterTowers.get(i);
                ReceiverTower receiverTower = receiverTowers.get(j);
                int powerIncreaseRequired = receiverTower.getPoint().distance(transmitterTower.getPoint()) - transmitterTower.getPower();
                matrix[i][j] = powerIncreaseRequired;
            }
        }
        return matrix;
    }

    /**
     * Get the number of matrix rows
     *
     * @return The number of matrix rows
     */
    public int getNbrOfRows() {
        return this.nbrOfRows;
    }

    /**
     * Get the number of matrix columns
     *
     * @return The number of matrix columns
     */
    public int getNbrOfColumns() {
        return this.nbrOfColumns;
    }

    /**
     * Get the value of a cell in the matrix
     *
     * @param column The cell column
     * @param row The cell row
     * @return The value of the cell
     */
    public int getValue(int column, int row) {
        return matrix[row][column];
    }


    /**
     * Choose a cell as a solution to the optimisation for a particular transmitter/receiver pairing
     *
     * Returns the value of the cell (the increase in power for the transmission tower) and updates
     * the matrix row to reflect the change.
     *
     * @param column The cell column
     * @param row The cell row
     * @return The value of the cell
     */
    public int choose(int column, int row) {
        int chosenValue = matrix[row][column];
        totalPowerIncrease += chosenValue;
        newTransmitterTowerPowerLevels.putIfAbsent(transmitterTowers.get(row), new AtomicInteger());
        newTransmitterTowerPowerLevels.get(transmitterTowers.get(row)).addAndGet(chosenValue);
        for (int c = 0; c < nbrOfColumns; c++) {
            matrix[row][c] = Math.max(0, matrix[row][c] - chosenValue);
        }
        return chosenValue;
    }

    /**
     * Get a list of row indices indicating the minimal value seen in the column
     *
     * @param column The column
     * @return A list of the row indices containing the minimal value seen in the column
     */
    public List<Integer> getMinimumRows(int column) {
        SimplePriorityQueue<Integer> lowestValueIndices = new SimplePriorityQueue<>();
        for (int row = 0; row < matrix.length; row++) {
            lowestValueIndices.put(matrix[row][column], row);
        }
        return new ArrayList<>(lowestValueIndices.pollSmallest().getValue());
    }

    /**
     * Get a map of transmitter towers and their required new power level
     *
     * @return A map of transmitter towers and their required new power level
     */
    public Map<TransmitterTower, Integer> getNewTransmitterTowerPowerLevels() {
        Map<TransmitterTower, Integer> newTransmitterTowerPowerLevels= new HashMap<>();
        for (Map.Entry<TransmitterTower, AtomicInteger> entry : this.newTransmitterTowerPowerLevels.entrySet()) {
            newTransmitterTowerPowerLevels.put(entry.getKey(), entry.getKey().getPower() + entry.getValue().intValue());
        }
        return newTransmitterTowerPowerLevels;
    }

    /**
     * Get the total power increase so far for this solution matrix
     *
     * @return The total power increase so far for this solution matrix
     */
    public Integer getTotalPowerIncrease() {
        return totalPowerIncrease;
    }

    /**
     * Get a string representation of the matrix
     *
     * @return String representation of the matrix
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        for (ReceiverTower receiverTower : receiverTowers) {
            sb.append(String.format(" R%d  ", receiverTower.getId()));
        }
        sb.append("\n");
        for (int row = 0; row < matrix.length; row++) {
            sb.append(String.format(" T%d ", transmitterTowers.get(row).getId()));
            for (int column = 0; column < matrix[0].length; column++) {
                sb.append(String.format(" %-3d ", matrix[row][column]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Create a deep copy of the matrix that can withstand internal state being mutated.
     *
     * This is necessary in order to facility branching out to solve cases where there are
     * multiple options for the next step.
     *
     * @return A deep copy of the matrix
     */
    public Matrix copy() {
        int [][] copiedMatrix = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++)
        {
            int[] matrixRow = matrix[i];
            int matrixRowLength = matrixRow.length;
            copiedMatrix[i] = new int[matrixRowLength];
            System.arraycopy(matrixRow, 0, copiedMatrix[i], 0, matrixRowLength);
        }
        Matrix matrixCopy = new Matrix();
        matrixCopy.nbrOfColumns = this.nbrOfColumns;
        matrixCopy.nbrOfRows = this.nbrOfRows;
        matrixCopy.transmitterTowers = this.transmitterTowers;
        matrixCopy.receiverTowers = this.receiverTowers;
        matrixCopy.totalPowerIncrease = this.totalPowerIncrease;
        matrixCopy.newTransmitterTowerPowerLevels = new HashMap<>(this.newTransmitterTowerPowerLevels);
        matrixCopy.matrix = copiedMatrix;

        return matrixCopy;
    }

}