package info.lindblad.radio.solver;


import info.lindblad.radio.model.*;
import info.lindblad.radio.util.Permutations;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MatrixSolver implements Solver {

    private class Matrix {

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
            matrix = constructInitialMatrix();
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

        public int choose(int column, int row) {
            int chosenValue = matrix[row][column];
            newTransmitterTowerPowerLevels.putIfAbsent(transmitterTowers.get(row), new AtomicInteger());
            newTransmitterTowerPowerLevels.get(transmitterTowers.get(row)).addAndGet(chosenValue);
            for (int c = 0; c < nbrOfColumns; c++) {
                matrix[row][c] = Math.max(0, matrix[row][c] - chosenValue);
            }
            return chosenValue;
        }

        public int getMinimumRow(int column) {
            int lowestValue = Integer.MAX_VALUE;
            int lowestValueIndex = -1;
            for (int row = 0; row < matrix.length; row++) {
                if (lowestValue > matrix[row][column]) {
                    lowestValue = matrix[row][column];
                    lowestValueIndex = row;
                }
            }
            return lowestValueIndex;
        }

        public Map<TransmitterTower, Integer> getNewTransmitterTowerPowerLevels() {
            Map<TransmitterTower, Integer> newTransmitterTowerPowerLevels= new HashMap<>();
            for (Map.Entry<TransmitterTower, AtomicInteger> entry : this.newTransmitterTowerPowerLevels.entrySet()) {
                newTransmitterTowerPowerLevels.put(entry.getKey(), entry.getKey().getPower() + entry.getValue().intValue());
            }
            return newTransmitterTowerPowerLevels;
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
        int minimalTotalPowerIncrease = Integer.MAX_VALUE;

        Map<TransmitterTower, Integer> newTransmitterTowerPowerLevels = new HashMap<>();

        List<TransmitterTower> transmitterTowers = new ArrayList<>(island.getTransmitterTowers().values());

        List<ReceiverTower> receiverTowersWithoutCoverage = new ArrayList<>(Solver.getReceiverTowersWithoutCoverage(island));
        List<List<ReceiverTower>> receiverTowersWithoutCoveragePermutations = new Permutations<>(receiverTowersWithoutCoverage).getPermutations();

        for (List<ReceiverTower> permutedReceiverTowersWithoutCoverage : receiverTowersWithoutCoveragePermutations) {
            nextTransmitterTowerPermutation:
            for (int startingRow = 0; startingRow < transmitterTowers.size(); startingRow++) {

                int totalPowerIncrease = 0;
                Matrix matrix = new Matrix(transmitterTowers, permutedReceiverTowersWithoutCoverage);

                totalPowerIncrease += matrix.choose(0, startingRow);

                for (int column = 1; column < matrix.nbrOfColumns; column++) {
                    int row = matrix.getMinimumRow(column);
                    totalPowerIncrease += matrix.choose(column, row);
                    if (totalPowerIncrease > minimalTotalPowerIncrease) {
                        continue nextTransmitterTowerPermutation;
                    }
                }

                if (totalPowerIncrease < minimalTotalPowerIncrease) {
                    minimalTotalPowerIncrease = totalPowerIncrease;
                    newTransmitterTowerPowerLevels = matrix.getNewTransmitterTowerPowerLevels();
                    // Save state about which changes need to be made to transmitters!
                }

            }
        }

        return newTransmitterTowerPowerLevels;
    }

}
