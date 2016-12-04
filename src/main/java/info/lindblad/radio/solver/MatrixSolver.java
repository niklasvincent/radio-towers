package info.lindblad.radio.solver;


import info.lindblad.radio.model.*;
import info.lindblad.radio.util.Permutations;
import info.lindblad.radio.util.SimplePriorityQueue;
import info.lindblad.radio.solver.model.Matrix;

import java.util.*;

public class MatrixSolver implements Solver {

    private static List<Matrix> findResultingMatrices(Matrix matrix, int column, int knownMinimalTotalPowerIncrease) {
        List<Matrix> newResultingMatrix = new ArrayList<>();
        List<Integer> minimumRowIndices = matrix.getMinimumRows(column);
        if (minimumRowIndices.size() == 1) {
            matrix.choose(column, minimumRowIndices.get(0));
            if (matrix.getTotalPowerIncrease() < knownMinimalTotalPowerIncrease) {
                newResultingMatrix.add(matrix);
            }
        } else {
            for (int minimumRowIndex : minimumRowIndices) {
                Matrix newMatrix = matrix.copy();
                newMatrix.choose(column, minimumRowIndex);
                if (matrix.getTotalPowerIncrease() < knownMinimalTotalPowerIncrease) {
                    newResultingMatrix.add(newMatrix);
                }
            }
        }
        return newResultingMatrix;
    }

    private static int solve(Matrix matrix, int startingRow, int knownMinimalTotalPowerIncrease) {
        final int startingColumn = 0;

        // Make the first choice
        matrix.choose(startingColumn, startingRow);

        List<Matrix> resultingMatrices = new ArrayList<>();
        resultingMatrices.add(matrix);

        for (int column = startingColumn + 1; column < matrix.getNbrOfColumns(); column++) {
            List<Matrix> newResultingMatrix = new ArrayList<>();
            for (Matrix resultingMatrix : resultingMatrices) {
                newResultingMatrix = findResultingMatrices(resultingMatrix, column, knownMinimalTotalPowerIncrease);
            }
            resultingMatrices = newResultingMatrix;
        }

        SimplePriorityQueue<Matrix> optimalMatrices = new SimplePriorityQueue<>();
        for (Matrix finalResultingMatrix : resultingMatrices) {
            optimalMatrices.put(finalResultingMatrix.getTotalPowerIncrease(), finalResultingMatrix);
        }

        if (optimalMatrices.size() == 0) {
            return Integer.MAX_VALUE;
        }
        return optimalMatrices.pollSmallest().getKey();
    }

    public Map<TransmitterTower, Integer> getRequiredTransmitterTowerChanges(Island island) {
        int minimalTotalPowerIncrease = Integer.MAX_VALUE;

        Map<TransmitterTower, Integer> newTransmitterTowerPowerLevels = new HashMap<>();

        List<TransmitterTower> transmitterTowers = new ArrayList<>(island.getTransmitterTowers().values());

        List<ReceiverTower> receiverTowersWithoutCoverage = new ArrayList<>(Solver.getReceiverTowersWithoutCoverage(island));
        List<List<ReceiverTower>> receiverTowersWithoutCoveragePermutations = new Permutations<>(receiverTowersWithoutCoverage).getPermutations();

        for (List<ReceiverTower> permutedReceiverTowersWithoutCoverage : receiverTowersWithoutCoveragePermutations) {
            for (int startingRow = 0; startingRow < transmitterTowers.size(); startingRow++) {

                Matrix matrix = new Matrix(transmitterTowers, permutedReceiverTowersWithoutCoverage);
                int totalPowerIncrease = solve(matrix, startingRow, minimalTotalPowerIncrease);

                if (totalPowerIncrease < minimalTotalPowerIncrease) {
                    minimalTotalPowerIncrease = totalPowerIncrease;
                    newTransmitterTowerPowerLevels = matrix.getNewTransmitterTowerPowerLevels();
                }

            }
        }

        return newTransmitterTowerPowerLevels;
    }

}
