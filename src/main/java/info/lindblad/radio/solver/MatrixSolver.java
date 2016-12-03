package info.lindblad.radio.solver;


import info.lindblad.radio.model.*;
import info.lindblad.radio.util.Permutations;

import java.util.*;

public class MatrixSolver implements Solver {

    private class Matrix {

        List<TransmitterTower> transmitterTowers;
        List<ReceiverTower> receiverTowers;

        int nbrOfTransmitterTowers;
        int nbrOfReceiverTowersWithoutCoverage;

        int[][] matrix;

        public Matrix(List<TransmitterTower> transmitterTowers, List<ReceiverTower> receiverTowers) {
            this.transmitterTowers = transmitterTowers;
            this.receiverTowers = receiverTowers;
            matrix = constructInitialMatrix();
        }

        private int[][] constructInitialMatrix() {
            nbrOfTransmitterTowers = transmitterTowers.size();
            nbrOfReceiverTowersWithoutCoverage = receiverTowers.size();

            int[][] matrix = new int[nbrOfTransmitterTowers][nbrOfReceiverTowersWithoutCoverage];

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

        public int choose(int column, int row) {
            return 0;
        }

        public int getMinimumRow(int row) {
            return 0;
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

    }

    public Map<TransmitterTower, Integer> getRequiredTransmitterTowerChanges(Island island) {
        int minimalTotalPowerIncrese = Integer.MAX_VALUE;
        Map<TransmitterTower, Integer> newTransmitterTowerPowerLevels = new HashMap<>();

        List<TransmitterTower> transmitterTowers = new ArrayList<>(island.getTransmitterTowers().values());

        List<ReceiverTower> receiverTowersWithoutCoverage = new ArrayList<>(Solver.getReceiverTowersWithoutCoverage(island));
        List<List<ReceiverTower>> receiverTowersWithoutCoveragePermutations = new Permutations<>(receiverTowersWithoutCoverage).getPermutations();

        for (List<ReceiverTower> permutedReceiverTowersWithoutCoverage : receiverTowersWithoutCoveragePermutations) {

            Matrix matrix = new Matrix(transmitterTowers, permutedReceiverTowersWithoutCoverage);

            System.out.println(matrix);


//            findTotalPowerCost(matrix, 0);

        }


        return newTransmitterTowerPowerLevels;
    }

    public static void findTotalPowerCost(int[][] matrix, int row) {
        System.out.println("");
        int value = matrix[row][0];
        int totalPower = value;
        int[][] copiedMatrix = matrix.clone();
        for (int copiedMatrixColumn = 0; copiedMatrixColumn < copiedMatrix[0].length; copiedMatrixColumn++) {
            copiedMatrix[row][copiedMatrixColumn] = Math.min(0, copiedMatrix[row][copiedMatrixColumn] - value);
        }
        System.out.println("");
        System.out.println(findMinimumInColumn(matrix, 1));
    }

    public static int findMinimumInColumn(int[][] matrix, int column) {
        int lowestSoFar = Integer.MAX_VALUE;
        int lowestSoFarRow = -1;
        for (int row = 0; row < matrix.length; row++) {
            if (lowestSoFar > matrix[row][column]) {
                lowestSoFar = matrix[row][column];
                lowestSoFarRow = row;
            }
        }
        System.out.println("Lowest found was " + matrix[lowestSoFarRow][column]);
        return lowestSoFarRow;
    }

}
