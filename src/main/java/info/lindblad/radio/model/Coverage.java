package info.lindblad.radio.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Coverage {

    private HashMap<Point, Set<TransmitterTower>> coverage;

    public Coverage() {
        coverage = new HashMap<Point, Set<TransmitterTower>>();
    }

    public Set<TransmitterTower> get(Point point) {
        return coverage.containsKey(point) ? coverage.get(point) : new HashSet<TransmitterTower>();
    }

    public void put(Point point, TransmitterTower transmitterTower) {
        if (!coverage.containsKey(point)) {
            coverage.put(point, new HashSet<TransmitterTower>());
        }
        coverage.get(point).add(transmitterTower);
    }

    public boolean isCovered(Point point) {
        return coverage.containsKey(point);
    }

    public Set<Point> keySet() {
        return coverage.keySet();
    }

}
