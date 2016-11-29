package info.lindblad.radio.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Coverage {

    private HashMap<Coordinates, Set<TransmitterTower>> coverage;

    public Coverage() {
        coverage = new HashMap<Coordinates, Set<TransmitterTower>>();
    }

    public Set<TransmitterTower> get(Coordinates coordinates) {
        return coverage.containsKey(coordinates) ? coverage.get(coordinates) : new HashSet<TransmitterTower>();
    }

    public void put(Coordinates coordinates, TransmitterTower transmitterTower) {
        if (!coverage.containsKey(coordinates)) {
            coverage.put(coordinates, new HashSet<TransmitterTower>());
        }
        coverage.get(coordinates).add(transmitterTower);
    }

    public boolean isCovered(Coordinates coordinates) {
        return coverage.containsKey(coordinates);
    }

    public Set<Coordinates> keySet() {
        return coverage.keySet();
    }

}
