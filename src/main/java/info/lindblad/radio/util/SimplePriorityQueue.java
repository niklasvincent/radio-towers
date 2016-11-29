package info.lindblad.radio.util;


import java.util.HashSet;
import java.util.TreeMap;

public class SimplePriorityQueue<E> extends TreeMap<Integer, HashSet<E>> {

    public HashSet<E> put(Integer key, E value) {
        if (!containsKey(key)) {
            super.put(key, new HashSet<>());
        }
        super.get(key).add(value);
        return super.get(key);
    }

}
