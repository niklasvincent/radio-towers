package info.lindblad.radio.model;

import java.util.*;

public class Island {

    private int sizeX;
    private int sizeY;

    private HashMap<Coordinates, TransmitterTower> transmitterTowers;
    private HashMap<Coordinates, ReceiverTower> receiverTowers;

    private Coverage coverage;

    public Island(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        transmitterTowers = new HashMap<Coordinates, TransmitterTower>();
        receiverTowers = new HashMap<Coordinates, ReceiverTower>();
        coverage = new Coverage();
    }

    public void addTransmitterTower(TransmitterTower transmitterTower) {
        transmitterTowers.put(transmitterTower.getCoordinates(), transmitterTower);
        updateCoverage(transmitterTower);
    }

    public void addReceiverTower(ReceiverTower receiverTower) {
        receiverTowers.put(receiverTower.getCoordinates(), receiverTower);
    }

    public int nbrOfReceiverTowers() {
        return receiverTowers.size();
    }

    public int nbrOfReceiverTowersWithCoverage() {
        return receiverTowers.size() - receiverTowersWithoutCoverage().size();
    }

    public int nbrOfReceiverTowersWithoutCoverage() {
        return receiverTowersWithoutCoverage().size();
    }

    private Set<ReceiverTower> receiverTowersWithoutCoverage() {
        Set<ReceiverTower> receiveTowersWithoutCoverage  = new HashSet<ReceiverTower>();
        for (Coordinates coordinates : receiverTowers.keySet()) {
            if (!coverage.get(coordinates)) {
                receiveTowersWithoutCoverage.add(receiverTowers.get(coordinates));
            }
        }
        return receiveTowersWithoutCoverage;
    }

    private boolean isWithinBounds(Coordinates coordinates) {
        return coordinates.getX() >= 0 && coordinates.getX() < sizeX && coordinates.getY() >= 0 && coordinates.getY() < sizeY;
    }

    private void updateCoverage(TransmitterTower transmitterTower) {
        for (Coordinates coordinatesReached: transmitterTower.reaches()) {
            if (isWithinBounds(coordinatesReached)) {
                coverage.put(coordinatesReached, true);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = sizeX - 1; y >= 0; y--) {
            for (int x = 0; x < this.sizeX; x++) {
                Coordinates coordinates = new Coordinates(x, y);
                if (receiverTowers.containsKey(coordinates)) {
                    sb.append(String.format("  R%d", receiverTowers.get(coordinates).getId()));
                } else if (transmitterTowers.containsKey(coordinates)) {
                    sb.append(String.format("  T%d", transmitterTowers.get(coordinates).getId()));
                } else if (coverage.get(coordinates)) {
                    sb.append("  * ");
                }
                else {
                    sb.append("  x ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
