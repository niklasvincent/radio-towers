package info.lindblad.radio.solver;


import info.lindblad.radio.model.*;
import info.lindblad.radio.util.Permutations;
import info.lindblad.radio.util.SimplePriorityQueue;
import info.lindblad.radio.solver.model.Matrix;

import java.util.*;

/**
 * This is an iterative solver based on a matrix representation of the transmitter/receiver tower configuration.
 *
 * The algorithm is:
 *
 *   - Set the best achieved overall power increase (minimalTotalPowerIncrease) to infinity (Integer.MAX_SIZE)
 *
 *   - Create a placeholder for a HashMap containing the resulting transmitter tower power increases
 *     (newTransmitterTowerPowerLevels)
 *
 *   - Construct list of all transmitter towers on the island (transmitterTowers)
 *
 *   - Construct a list of all receiver towers that are currently out of signal coverage on the island
 *     (receiverTowersWithoutCoverage)
 *
 *   - Generate all possible permutations of the list receiverTowersWithoutCoverage (permutedReceiverTowersWithoutCoverage)
 *
 *   - For each permuted version of receiverTowersWithoutCoverage:
 *
 *       - For each possible starting row (startingRow) in the range 0 -> transmitterTowers.size() - 1
 *
 *           - Construct a matrix where each row corresponds to a transmitter tower
 *             and each column corresponds to a receiver tower and the cell value is
 *             the required increase in power for the transmitter tower to give signal
 *             coverage to the receiver tower.
 *
 *           - Make the first change in transmitter tower power the value of (startingRow, 0) in the matrix
 *
 *           - Add the original matrix to the resultingMatrices list
 *
 *           - For each column in 1 -> receiverTowersWithoutCoverage.size() - 1:
 *
 *              - For each matrix in the resultingMatrices list:
 *
 *                  - For each minimal value in the column in the matrix, generate a new matrix by choosing
 *                    that minimal value and append the new matrices to a list (newResultingMatrix) if the current
 *                    overall increase in power level for that matrix is less than or equal to
 *                    knownMinimalTotalPowerIncrease
 *
 *              - Re-assign newResultingMatrix to resultingMatrices
 *
 *           - The remaining matrices in resultingMatrices have now had their columns traversed left to right
 *              and they have only been kept if they at the time of evaluation did not have a power level increase
 *              higher than knownMinimalTotalPowerIncrease
 *
 *            - Put each matrix from resultingMatrices into a priority queue with the priority being their
 *              overall increase in power level
 *
 *            - Get and return the lowest priority number (totalPowerIncrease)
 *
 *      - Compare totalPowerIncrease increase with minimalTotalPowerIncrease and set minimalTotalPowerIncrease to
 *        totalPowerIncrease if totalPowerIncrease is smaller
 *
 *      - If the totalPowerIncrease was smaller than minimalPowerIncrease, also re-assign the power level increases
 *        per transmitter tower from the matrix to newTransmitterTowerPowerLevels
 *
 *   - newTransmitterTowerPowerLevels represents the optimal changes to make to the transmitter towers in order to
 *     achieve full signal coverage for all receiver towers
 *
 */
public class MatrixSolver implements Solver {

    /**
     * Get the new adjusted power levels required for applicable transmitter towers in order to assure
     * full signal coverage.
     *
     * @param island The island
     * @return A map of transmitter towers and their new required power level to assure full signal coverage
     */
    public Map<TransmitterTower, Integer> getRequiredTransmitterTowerChanges(Island island) {
        int minimalTotalPowerIncrease = Integer.MAX_VALUE;

        Map<TransmitterTower, Integer> newTransmitterTowerPowerLevels = new HashMap<>();

        List<TransmitterTower> transmitterTowers = new ArrayList<>(island.getTransmitterTowers().values());

        List<ReceiverTower> receiverTowersWithoutCoverage = new ArrayList<>(Solver.getReceiverTowersWithoutCoverage(island));
        List<List<ReceiverTower>> receiverTowersWithoutCoveragePermutations = new Permutations<>(receiverTowersWithoutCoverage).getPermutations();

        for (List<ReceiverTower> permutedReceiverTowersWithoutCoverage : receiverTowersWithoutCoveragePermutations) {
            for (int startingRow = 0; startingRow < transmitterTowers.size(); startingRow++) {

                Matrix matrix = new Matrix(transmitterTowers, permutedReceiverTowersWithoutCoverage);
                int totalPowerIncrease = getSmallestTotalPowerIncreaseForMatrix(matrix, startingRow, minimalTotalPowerIncrease);

                if (totalPowerIncrease < minimalTotalPowerIncrease) {
                    minimalTotalPowerIncrease = totalPowerIncrease;
                    newTransmitterTowerPowerLevels = matrix.getNewTransmitterTowerPowerLevels();
                }

            }
        }

        return newTransmitterTowerPowerLevels;
    }

    /**
     * Calculate the smallest achievable total power increase for this particular matrix by evaluating
     * all minimising choices of values while traversing the columns left to right.
     *
     * @param matrix The matrix
     * @param startingRow The starting row
     * @param knownMinimalTotalPowerIncrease The current best-knowledge achievable total power increase
     * @return The smallest achievable total power increase for this particular matrix
     */
    private static int getSmallestTotalPowerIncreaseForMatrix(Matrix matrix, int startingRow, int knownMinimalTotalPowerIncrease) {
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

    /**
     * For a given matrix and column, find all minimal values in the column and choose those as the starting
     * point for the next iteration.
     *
     * @param matrix The matrix
     * @param column The column
     * @param knownMinimalTotalPowerIncrease The current best-knowledge achievable total power increase
     * @return A list of subsequent matrices that arose from selecting all minimising choices
     */
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

}
