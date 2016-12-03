package info.lindblad.radio.model;

import java.util.HashMap;
import java.util.Optional;

public class Island {

    /**
     * The bound of the island. Used to check whether points are actually
     * part of the island or not.
     */
    private Bounds bounds;

    /**
     * Instead of storing a 2D grid with (potentially) a lot of wasted space,
     * the transmitters and receivers are sparsely stored using two different
     * maps where the key is a given Point.
     */
    private HashMap<Point, TransmitterTower> transmitterTowers;
    private HashMap<Point, ReceiverTower> receiverTowers;

    public Island(int sizeX, int sizeY) {
        bounds = new Bounds(sizeX, sizeY);
        transmitterTowers = new HashMap<>();
        receiverTowers = new HashMap<>();
    }

    /**
     * Get the bounds of the island
     *
     * @return The bounds of the island
     */
    public Bounds getBounds() {
        return bounds;
    }

    /**
     * Get a map of all transmitter towers on the island
     *
     * @return Map of transmitter towers
     */
    public HashMap<Point, TransmitterTower> getTransmitterTowers() {
        return transmitterTowers;
    }

    /**
     * Get a map of all receiver towers on the island
     *
     * @return Map of receiver towers
     */
    public HashMap<Point, ReceiverTower> getReceiverTowers() {
        return receiverTowers;
    }

    /**
     * Add transmitter tower to the island
     *
     * @param transmitterTower The transmitter tower
     */
    public void addTransmitterTower(TransmitterTower transmitterTower) {
        transmitterTowers.put(transmitterTower.getPoint(), transmitterTower);
    }

    /**
     * Add receiver tower to the island
     *
     * @param receiverTower The receiver tower
     */
    public void addReceiverTower(ReceiverTower receiverTower) {
        receiverTowers.put(receiverTower.getPoint(), receiverTower);
    }

    /**
     * Get the number of transmitter towers on the island
     *
     * @return The number of transmitter towers on the island
     */
    public int getNbrOfTransmitterTowers() {
        return receiverTowers.size();
    }

    /**
     * Get the number of receiver towers on the island
     *
     * @return The number of receiver towers on the island
     */
    public int getNbrOfReceiverTowers() {
        return receiverTowers.size();
    }

    /**
     * Check equality between two islands
     *
     * @param o Other island
     * @return Whether the two islands are the same
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof Island)
                && (((Island) o).getReceiverTowers().equals(getReceiverTowers())
                && ((Island) o).getTransmitterTowers().equals(getTransmitterTowers())
                && ((Island) o).getBounds().equals(getBounds())
        );
    }

    /**
     * Return a string representation of the island as an ASCII grid with signal coverage marked
     *
     * @param coverage The signal coverage
     * @return String representation of the island
     */
    public String toString(Coverage coverage) {
        Optional<Coverage> optionalCoverage = Optional.ofNullable(coverage);
        StringBuilder sb = new StringBuilder();
        for (int y = this.bounds.getSizeX() - 1; y >= 0; y--) {
            for (int x = 0; x < this.bounds.getSizeX(); x++) {
                Point point = new Point(x, y);
                if (receiverTowers.containsKey(point)) {
                    sb.append(String.format("  R%d", receiverTowers.get(point).getId()));
                } else if (transmitterTowers.containsKey(point)) {
                    sb.append(String.format("  T%d", transmitterTowers.get(point).getId()));
                } else if (optionalCoverage.isPresent() && optionalCoverage.get().hasSignal(point)) {
                    sb.append("  * ");
                } else {
                    sb.append("  x ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Return a string representation of the island as an ASCII grid
     *
     * @return String representation of the island
     */
    @Override
    public String toString() {
       return toString(null);
    }

}
