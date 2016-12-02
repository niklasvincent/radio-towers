package info.lindblad.radio.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Coverage {

    /**
     * The coverage is represented by a map where the key a
     * point and the value is a set of transmitter towers
     * whose signal covers that point.
     */
    private HashMap<Point, Set<TransmitterTower>> coverage;

    public Coverage(Bounds bounds, HashMap<Point, TransmitterTower> transmitterTowers) {
        coverage = new HashMap<>();
        calculateCoverage(bounds, transmitterTowers);
    }

    public Coverage(Island island) {
        coverage = new HashMap<>();
        calculateCoverage(island.getBounds(), island.getTransmitterTowers());
    }

    /**
     * Given a set of transmitter towers and the bounds of a grid,
     * a mapping between points and sets of covering transmitter towers
     * is constructed.
     *
     * @param bounds The bounds of the grid
     * @param transmitterTowers The set of transmitter towers
     */
    private void calculateCoverage(Bounds bounds, HashMap<Point, TransmitterTower> transmitterTowers) {
        transmitterTowers.forEach((point, transmitterTower) -> transmitterTower.reaches().stream()
                        .filter(bounds::contains)
                        .forEach(pointReached -> coveredBy(pointReached, transmitterTower))
        );
    }

    /**
     * Get the transmitter towers covering a specific point, if any.
     *
     * Returns an empty set in case there is no signal at the specific point.
     *
     * @param point The point
     * @return Set of transmitter towers providing coverage for the point
     */
    public Set<TransmitterTower> getTransmitterTowersCovering(Point point) {
        return coverage.containsKey(point) ? coverage.get(point) : new HashSet<>();
    }

    /**
     * Mark a point as covered by a specific transmitter tower.
     *
     * @param point The point
     * @param transmitterTower The transmitter tower covering the point
     */
    public void coveredBy(Point point, TransmitterTower transmitterTower) {
        if (!coverage.containsKey(point)) {
            coverage.put(point, new HashSet<>());
        }
        coverage.get(point).add(transmitterTower);
    }

    /**
     * Check whether a specific point has signal or not
     *
     * @param point The point
     * @return Whether the point has signal or not
     */
    public boolean hasSignal(Point point) {
        return coverage.containsKey(point);
    }


    /**
     * Find closest points with signal with respect to a specified point
     *
     * @param point The point
     * @return A set of nearby points with signal
     */
    public Set<Point> findClosestPointsWithSignal(Point point) {
        return Point.closestNeighbours(point, getAllPointsWithSignal());
    }

    /**
     * Get all points that have signal.
     *
     * @return A set of points
     */
    public Set<Point> getAllPointsWithSignal() {
        return coverage.keySet();
    }

}
