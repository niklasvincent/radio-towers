package info.lindblad.radio.util;


import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple priority queue that does not force you to implement a Comparator or add
 * any additional properties to your classes.
 *
 * Each element put on the queue are given an integer priority and elements with the
 * same priority are grouped together in a set.
 *
 * The queue also does not enforce a convention on what is of high priority or what is
 * of low. Elements can be polled in the order of largest to smallest or the other way around.
 *
 * @param <E>
 */
public class SimplePriorityQueue<E> extends TreeMap<Integer, HashSet<E>> {

    private int nbrOfElements = 0;

    public HashSet<E> put(Integer key, E value) {
        if (!containsKey(key)) {
            super.put(key, new HashSet<>());
        }
        super.get(key).add(value);
        nbrOfElements++;
        return super.get(key);
    }

    /**
     * Poll the element with the largest priority number
     *
     * @return Entry for the element with the largest priority number
     */
    public Map.Entry<Integer, HashSet<E>> pollLargest() {
        nbrOfElements--;
        return pollLastEntry();
    }

    /**
     * Poll the element with the smallest priority number
     *
     * @return Entry for the element with the smallest priority number
     */
    public Map.Entry<Integer, HashSet<E>> pollSmallest() {
        nbrOfElements--;
        return pollFirstEntry();
    }

    @Override
    public int size() {
        return nbrOfElements;
    }
}
