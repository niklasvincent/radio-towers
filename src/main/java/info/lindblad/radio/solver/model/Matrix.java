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

    public Matrix(List<TransmitterTower> transmitterTowers, List<ReceiverTower> receiverTowers) {
        newTransmitterTowerPowerLevels = new HashMap<>();
        this.transmitterTowers = transmitterTowers;
        this.receiverTowers = receiverTowers;
        this.totalPowerIncrease = 0;
        matrix = constructInitialMatrix();
    }

    private Matrix() {

    }

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

    public int getNbrOfRows() {
        return this.nbrOfRows;
    }

    public int getNbrOfColumns() {
        return this.nbrOfColumns;
    }

    public int getValue(int column, int row) {
        return matrix[row][column];
    }

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

    public List<Integer> getMinimumRows(int column) {
        SimplePriorityQueue<Integer> lowestValueIndices = new SimplePriorityQueue<>();
        for (int row = 0; row < matrix.length; row++) {
            lowestValueIndices.put(matrix[row][column], row);
        }
        return new ArrayList<>(lowestValueIndices.pollSmallest().getValue());
    }

    public Map<TransmitterTower, Integer> getNewTransmitterTowerPowerLevels() {
        Map<TransmitterTower, Integer> newTransmitterTowerPowerLevels= new HashMap<>();
        for (Map.Entry<TransmitterTower, AtomicInteger> entry : this.newTransmitterTowerPowerLevels.entrySet()) {
            newTransmitterTowerPowerLevels.put(entry.getKey(), entry.getKey().getPower() + entry.getValue().intValue());
        }
        return newTransmitterTowerPowerLevels;
    }

    public Integer getTotalPowerIncrease() {
        return totalPowerIncrease;
    }

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

    public Matrix copy() {
        // TODO(nlindblad) Clean this up
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
        matrixCopy.newTransmitterTowerPowerLevels = this.newTransmitterTowerPowerLevels;
        matrixCopy.matrix = copiedMatrix;

        return matrixCopy;
    }

}