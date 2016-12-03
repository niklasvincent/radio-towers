package info.lindblad.radio.solver;


import info.lindblad.radio.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface Solver {

    /**
     * Get a set of all receiver towers that are without signal coverage
     *
     * @param coverage The coverage
     * @param receiverTowers The receiver towers
     * @return A set of receiver towers without signal coverage
     */
     static Set<ReceiverTower> getReceiverTowersWithoutCoverage(Coverage coverage, HashMap<Point, ReceiverTower> receiverTowers) {
        return receiverTowers.values().stream()
                .filter(receiverTower -> !coverage.hasSignal(receiverTower.getPoint()))
                .collect(Collectors.toSet());
    }

    /**
     * Get a set of all receiver towers that are without signal coverage for a given island
     *
     * @param island The island
     * @return A set of receiver towers without signal coverage
     */
     static Set<ReceiverTower> getReceiverTowersWithoutCoverage(Island island) {
        Coverage coverage = new Coverage(island);
        return getReceiverTowersWithoutCoverage(coverage, island.getReceiverTowers());
    }

    /**
     * Get the number of receiver towers without coverage for a given island.
     *
     * @param island The island
     * @return The number of receiver towers without coverage
     */
     static int nbrOfReceiverTowersWithoutCoverage(Island island) {
        Coverage coverage = new Coverage(island.getBounds(), island.getTransmitterTowers());
        return getReceiverTowersWithoutCoverage(coverage, island.getReceiverTowers()).size();
    }

    /**
     * Get the new adjusted power levels required for applicable transmitter towers in order to assure
     * full signal coverage.
     *
     * @param island The island
     * @return A map of transmitter towers and their new required power level to assure full signal coverage
     */
     Map<TransmitterTower, Integer> getRequiredTransmitterTowerChanges(Island island);

}
