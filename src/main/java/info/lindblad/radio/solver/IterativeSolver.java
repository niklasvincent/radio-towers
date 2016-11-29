package info.lindblad.radio.solver;


import info.lindblad.radio.model.*;
import info.lindblad.radio.util.SimplePriorityQueue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class IterativeSolver {

    /**
     * Get a set of all receiver towers that are without signal coverage.
     *
     * @param coverage The coverage
     * @param receiverTowers The receiver towers
     * @return A set of receiver towers without signal coverage
     */
    public static Set<ReceiverTower> getReceiverTowersWithoutCoverage(Coverage coverage, HashMap<Point, ReceiverTower> receiverTowers) {
        return receiverTowers.values().stream()
                .filter(receiverTower -> !coverage.hasSignal(receiverTower.getPoint()))
                .collect(Collectors.toSet());
    }

    /**
     * Get a set of all receiver towers that are without signal coverage.
     *
     * @param island The island
     * @return A set of receiver towers without signal coverage
     */
    public static Set<ReceiverTower> getReceiverTowersWithoutCoverage(Island island) {
        Coverage coverage = new Coverage(island);
        return getReceiverTowersWithoutCoverage(coverage, island.getReceiverTowers());
    }

    /**
     * Get the number of receiver towers without coverage for a given island.
     *
     * @param island The island
     * @return The number of receiver towers without coverage
     */
    public static int nbrOfReceiverTowersWithoutCoverage(Island island) {
        Coverage coverage = new Coverage(island.getBounds(), island.getTransmitterTowers());
        return getReceiverTowersWithoutCoverage(coverage, island.getReceiverTowers()).size();
    }

    /**
     * Get a priority queue comprised of the possible power level adjustments for the applicable transmitter towers
     *
     * @param coverage The coverage
     * @param receiverTowersWithoutCoverage The receiver towers without signal coverage
     * @return A priority queue of possible power level adjustments for the applicable transmitter towers
     */
    private static SimplePriorityQueue<TransmitterTower> getPossiblePowerLevelChanges(Coverage coverage, Set<ReceiverTower> receiverTowersWithoutCoverage) {
        SimplePriorityQueue<TransmitterTower> requiredChanges = new SimplePriorityQueue<>();
        for (ReceiverTower receiverTowerWithoutCoverage :  receiverTowersWithoutCoverage) {
            Set<Point> closestCoveragePoints = coverage.findClosestPointsWithSignal(receiverTowerWithoutCoverage.getPoint());
            for (Point nearbyCoveragePoint : closestCoveragePoints) {
                int requiredPowerIncrease = nearbyCoveragePoint.roundedUpDistance(receiverTowerWithoutCoverage.getPoint());
                Set<TransmitterTower> candidateTransmitterTowers = coverage.getTransmitterTowersCovering(nearbyCoveragePoint);
                candidateTransmitterTowers
                        .forEach(candidateTransmitterTower -> requiredChanges.put(requiredPowerIncrease, candidateTransmitterTower));
            }
        }
        return requiredChanges;
    }

    /**
     * Get the new adjusted power levels required for applicable transmitter towers in order to assure
     * full signal coverage
     *
     * @param island The island
     * @return A map of transmitter towers and their new required power level to assure full signal coverage
     */
    public static Map<TransmitterTower, Integer> getRequiredTransmitterTowerChanges(Island island) {
        Map<TransmitterTower, Integer> newTransmitterTowerPowerLevels = new HashMap<>();

        // Since the state of the current transmitter towers are going to be mutated, we clone it to avoid mutating the island state
        HashMap<Point, TransmitterTower> currentTransmitterTowers = (HashMap<Point, TransmitterTower>) island.getTransmitterTowers().clone();

        // Calculate the current coverage
        Coverage currentCoverage = new Coverage(island.getBounds(), currentTransmitterTowers);

        // Calculate which receiver towers are currently out of signal coverage
        Set<ReceiverTower> receiverTowersWithoutCoverage = getReceiverTowersWithoutCoverage(currentCoverage, island.getReceiverTowers());

        // Iterate whilst there are still receiver towers without signal coverage
        while (receiverTowersWithoutCoverage.size() > 0) {
            // Calculate possible power level changes based on current configuration
            SimplePriorityQueue<TransmitterTower> possiblePowerLevelChanges = getPossiblePowerLevelChanges(currentCoverage, receiverTowersWithoutCoverage);

            SimplePriorityQueue<TransmitterTower> suggestedChanges = new SimplePriorityQueue<>();

            // Always evaluate the largest change, in the hope of covering additional receiver towers
            Map.Entry<Integer, HashSet<TransmitterTower>> requiredChange = possiblePowerLevelChanges.pollLargest();
            int requiredPowerIncrease = requiredChange.getKey();
            for (TransmitterTower transmitterTower: requiredChange.getValue()) {
                // Calculate the new reach of the transmitter tower with the possible power level change
                Set<Point> newReach = transmitterTower.reachesWithIncreasedPower(requiredPowerIncrease).stream()
                        .filter(island.getBounds()::contains)
                        .collect(Collectors.toSet());
                // Calculate how many receiver towers would get signal coverage if this change was applied
                int nbrOfNewReceiverTowersWithCoverage = newReach.stream()
                        .map(newPointWithCoverage -> island.getReceiverTowers().get(newPointWithCoverage))
                        .filter(receiverTower ->  receiverTower != null)
                        .filter(receiverTowersWithoutCoverage::contains)
                        .collect(Collectors.toList()).size();
                suggestedChanges.put(nbrOfNewReceiverTowersWithCoverage, transmitterTower);
            }
            // Adjust power for the transmission tower that would give new signal coverage to the most receiver towers
            TransmitterTower transmitterTowerToIncreasePowerFor = suggestedChanges.pollLargest().getValue().iterator().next();

            currentTransmitterTowers.get(transmitterTowerToIncreasePowerFor.getPoint()).increasePower(requiredPowerIncrease);
            newTransmitterTowerPowerLevels.put(transmitterTowerToIncreasePowerFor, transmitterTowerToIncreasePowerFor.getPower());

            // Re-calculate coverage and number of receiver towers without signal coverage after this iteration
            currentCoverage = new Coverage(island.getBounds(), currentTransmitterTowers);
            receiverTowersWithoutCoverage = getReceiverTowersWithoutCoverage(currentCoverage, island.getReceiverTowers());
        }
        return newTransmitterTowerPowerLevels;
    }

}
