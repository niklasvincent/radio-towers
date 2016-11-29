package info.lindblad.radio.model;

import info.lindblad.radio.util.SimplePriorityQueue;

import java.util.*;
import java.util.stream.Collectors;

public class Island {

    private int sizeX;
    private int sizeY;

    private HashMap<Point, TransmitterTower> transmitterTowers;
    private HashMap<Point, ReceiverTower> receiverTowers;

    private Coverage coverage;

    public Island(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        transmitterTowers = new HashMap<>();
        receiverTowers = new HashMap<>();
        coverage = new Coverage();
    }

    public void addTransmitterTower(TransmitterTower transmitterTower) {
        transmitterTowers.put(transmitterTower.getPoint(), transmitterTower);
        updateCoverage(transmitterTower);
    }

    private void increasePowerForTransmissionTower(TransmitterTower transmitterTower, int powerIncrease) {
        transmitterTower.increasePower(powerIncrease);
        updateCoverage(transmitterTower);
    }

    public void addReceiverTower(ReceiverTower receiverTower) {
        receiverTowers.put(receiverTower.getPoint(), receiverTower);
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
        Set<ReceiverTower> receiveTowersWithoutCoverage =
                receiverTowers.keySet().stream()
                        .filter(coordinates -> !coverage.isCovered(coordinates))
                        .map(coordinates -> receiverTowers.get(coordinates))
                        .collect(Collectors.toSet());
        return receiveTowersWithoutCoverage;
    }

    private Set<Point> findClosestCoveragePoints(Point point) {
        return Point.closestNeighbours(point, coverage.keySet()).pollFirstEntry().getValue();
    }

    private SimplePriorityQueue<TransmitterTower> getRequiredTransmitterTowerChanges() {
        SimplePriorityQueue<TransmitterTower> requiredChanges = new SimplePriorityQueue<>();
        for (ReceiverTower receiverTower :  receiverTowersWithoutCoverage()) {
            Set<Point> closestCoverageCoordinates = findClosestCoveragePoints(receiverTower.getPoint());
            for (Point closest : closestCoverageCoordinates) {
                int requiredPowerIncrease = (int) Math.ceil(closest.distance(receiverTower.point));
                Set<TransmitterTower> candidateTransmitterTowers = coverage.get(closest);
                candidateTransmitterTowers
                        .forEach(candidateTransmitterTower -> requiredChanges.put(requiredPowerIncrease, candidateTransmitterTower));
            }
        }
        return requiredChanges;
    }

    public Map<TransmitterTower, Integer> getNecessaryChanges() {
        Map<TransmitterTower, Integer> changesMade = new HashMap<>();
        Set<ReceiverTower> receiverTowersWithoutCoverage = receiverTowersWithoutCoverage();
        while (receiverTowersWithoutCoverage.size() > 0) {
            SimplePriorityQueue<TransmitterTower> requiredChanges = getRequiredTransmitterTowerChanges();
            SimplePriorityQueue<TransmitterTower> suggestedChanges = new SimplePriorityQueue<>();
            Map.Entry<Integer, HashSet<TransmitterTower>> requiredChange = requiredChanges.pollLastEntry();
            int requiredPowerIncrease = requiredChange.getKey();
            for (TransmitterTower transmitterTower: requiredChange.getValue()) {
                Set<Point> newReach = transmitterTower.reachesWithIncreasedPower(requiredPowerIncrease).stream()
                        .filter(this::isWithinBounds)
                        .collect(Collectors.toSet());
                int nbrOfNewReceiverTowersWithCoverage = newReach.stream()
                        .map(coordinates -> receiverTowers.get(coordinates))
                        .filter(receiverTower ->  receiverTower != null)
                        .filter(receiverTowersWithoutCoverage::contains)
                        .collect(Collectors.toList()).size();
                System.out.println("Increase for " + transmitterTower + " would yield " + nbrOfNewReceiverTowersWithCoverage);
                suggestedChanges.put(nbrOfNewReceiverTowersWithCoverage, transmitterTower);
            }
//            System.out.println("suggestedChanges = " + suggestedChanges);
            TransmitterTower transmitterTowerToIncreasePowerFor = suggestedChanges.pollFirstEntry().getValue().iterator().next();
//            System.out.println("Making changes for " + transmitterTowerToIncreasePowerFor);
            increasePowerForTransmissionTower(transmitterTowerToIncreasePowerFor, requiredPowerIncrease);
            changesMade.put(transmitterTowerToIncreasePowerFor, transmitterTowerToIncreasePowerFor.getPower());
//            System.out.println("receiverTowersWithoutCoverage = " + receiverTowersWithoutCoverage);
            receiverTowersWithoutCoverage = receiverTowersWithoutCoverage();
        }
        return changesMade;
    }

    private boolean isWithinBounds(Point point) {
        return point.getX() >= 0 && point.getX() < sizeX && point.getY() >= 0 && point.getY() < sizeY;
    }

    private void updateCoverage(TransmitterTower transmitterTower) {
        transmitterTower.reaches().stream().filter(this::isWithinBounds).forEach(coordinatesReached -> {
            coverage.put(coordinatesReached, transmitterTower);
        });
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = sizeX - 1; y >= 0; y--) {
            for (int x = 0; x < this.sizeX; x++) {
                Point point = new Point(x, y);
                if (receiverTowers.containsKey(point)) {
                    sb.append(String.format("  R%d", receiverTowers.get(point).getId()));
                } else if (transmitterTowers.containsKey(point)) {
                    sb.append(String.format("  T%d", transmitterTowers.get(point).getId()));
                } else if (coverage.isCovered(point)) {
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
