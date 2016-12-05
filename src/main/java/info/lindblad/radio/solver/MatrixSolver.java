package info.lindblad.radio.solver;


import info.lindblad.radio.model.*;
import info.lindblad.radio.util.Permutations;
import info.lindblad.radio.util.SimplePriorityQueue;
import info.lindblad.radio.solver.model.Matrix;

import java.util.*;

/**
 * This solver finds the minimal overall power level increases required, specified by the new total power level for each
 * transmitter tower, for a given island in order to ensure that all receiver towers have signal coverage.
 *
 * The basis of the solver is to represent each transmitter/receiver configuration as a matrix. Each row in the
 * matrix corresponds to a transmitter tower and each column corresponds to a receiver tower.
 *
 * The value in each cell represents the required increase in power for the transmitter tower to give signal
 * coverage to the receiver tower.
 *
 * An example of such a matrix is:
 *
 *      R3   R1   R2   R4
 *  T1  5    5    5    5
 *  T3  10   16   10   4
 *  T4  16   10   4    10
 *  T2  10   4    10   16
 *  T5  4    10   16   10
 *
 * Since adjusting the power level of a transmitter will change the signal coverage for the rest of the island,
 * the order in which changes are evaluated matters. In order to completely exhaust the possible search space, the list of
 * receiver towers without signal coverage is turned into a list of all permuted versions of itself. This assures that
 * all possible chains of changes are tested, e.g. R1 -> R2 -> R3 -> R4, R1 -> R3 -> R4 -> R2, etc.
 *
 * Similarly, as we go through each matrix we also have to vary the initial row we start with, in order to make sure each
 * receiver tower permutation is tested with each individual transmitter tower as the starting point.
 *
 * This means that a total of n! x m matrices will be evaluated, where n is the number of receiver towers without signal
 * coverage and m is the number of transmitter towers on the island. These are our starting matrices.
 *
 * For each such starting matrix, we have to pick a row in each column, representing a choice in making the transmitter
 * corresponding to that row increase its power level. When a choice has been made, the entire row has to be updated to
 * reflect the new conditions for each receiver tower. For example, if we pick the (1, 0) cell in the matrix shown earlier,
 * we are effectively increasing transmitter tower 3 (T3) by 10, thus altering the power level increase required to give the
 * other receiver towers signal coverage:
 *
 *      R3   R1   R2   R4
 *  T1  5    5    5    5
 *  T3  0    6    0    0
 *  T4  16   10   4    10
 *  T2  10   4    10   16
 *  T5  4    10   16   10
 *
 * Whilst the initial choice has to be pre-determined to make sure all possible starting configurations are evaluated,
 * the overall aim is to minimise the overall power increase. Thus, for each subsequent column, we pick the minimal value,
 * because that corresponds to locally optimising the power required to bring the receiver tower the column represents into
 * signal coverage.
 *
 * For the example matrix given, the next column to evaluate is now [5, 6, 10, 4, 10], which means that the optimal choice
 * for giving signal coverage to receiver tower 1 (R1) is now 4, which means our overall total power increase after
 * evaluating two columns is 10 + 4 = 14.
 *
 * The new resulting matrix is:
 *
 *      R3   R1   R2   R4
 *  T1  5    5    5    5
 *  T3  0    6    0    0
 *  T4  16   10   4    10
 *  T2  6    0    6    12
 *  T5  4    10   16   10
 *
 * If there are multiple minimal values in a column, they all have to be evaluated.
 *
 * For example, if the following matrix is evaluated and we pick (0, 0) initially:
 *
 *      R1   R2   R3
 *  T1  5    8    10
 *  T2  7    3     4
 *
 *  It will give rise to the following matrix:
 *
 *      R1   R2   R3
 *  T1  0    3    5
 *  T2  7    3    4
 *
 *  In column two, there are now two rows that have the minimal value and depending on which one we choose our resulting
 *  overall power increase will be either 9 or 12.
 *
 * To take this into account, at the beginning of the evaluation of each matrix, a list holding all matrices that need
 * further evaluation is constructed and the initial matrix is added.
 *
 * As the columns in the matrix are traversed left to right evaluating each column gives rise to one or more new matrices
 * that need to be further evaluated. These matrices are all added to the list.
 *
 * Once the rightmost column has been evaluated, we are left with a list of fully traversed matrices that all have kept
 * track of their own overall total power increase as they have made choices of which transmitter tower to increase power
 * for throughout all their iterations.
 *
 * Using the list of fully evaluated matrices that arose from the single starting matrix, the matrix with the minimal
 * total power increase is selected and the power increase it had to make is fed back before the next starting matrix
 * is being evaluated.
 *
 * By keeping track of the best known total power increase so far, we can reduce the number of iterations we have to do
 * by simply omitting any more evaluations of a matrix if it already has a total power increase higher than the current
 * known best.
 *
 * The final solution to what the lowest overall power level increase can be is the latest version of the best known
 * total power increase so far. Since each fully evaluated matrix keeps track of the transmitter power level adjustments
 * it has done, we can now return a mapping for the new required power level of each transmitter tower that needs to be
 * adjusted.
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
                // Branch out by making a deep copy of the matrix, which allows it to be
                // evaluated independently.
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
