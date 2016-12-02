package info.lindblad.radio.util;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashSet;

public class TestSimplePriorityQueue extends TestCase {

    public TestSimplePriorityQueue(String testName) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite(TestSimplePriorityQueue.class);
    }


    public void testFindElementWithHighestPriorityNumber() {
        SimplePriorityQueue<String> queue = new SimplePriorityQueue<>();
        queue.put(1, "Apple");
        queue.put(5, "Banana");
        queue.put(2, "Lemon");
        queue.put(3, "Kiwi");
        assertEquals(queue.size(), 4);
        assertEquals(queue.pollLargest().getValue().iterator().next(), "Banana");
        assertEquals(queue.size(), 3);
        assertEquals(queue.pollLargest().getValue().iterator().next(), "Kiwi");
        assertEquals(queue.size(), 2);
        assertEquals(queue.pollLargest().getValue().iterator().next(), "Lemon");
        assertEquals(queue.size(), 1);
        assertEquals(queue.pollLargest().getValue().iterator().next(), "Apple");
        assertEquals(queue.size(), 0);
    }

    public void testFindElementWithLowestPriorityNumber() {
        SimplePriorityQueue<String> queue = new SimplePriorityQueue<>();
        queue.put(1, "Apple");
        queue.put(5, "Banana");
        queue.put(2, "Lemon");
        queue.put(3, "Kiwi");
        assertEquals(4, queue.size());
        assertEquals("Apple", queue.pollSmallest().getValue().iterator().next());
        assertEquals(3, queue.size());
        assertEquals("Lemon", queue.pollSmallest().getValue().iterator().next());
        assertEquals(2, queue.size());
        assertEquals("Kiwi", queue.pollSmallest().getValue().iterator().next());
        assertEquals(1, queue.size());
        assertEquals("Banana", queue.pollSmallest().getValue().iterator().next());
        assertEquals(0, queue.size());
    }

    public void testCollidingPriorityNumbers() {
        SimplePriorityQueue<String> queue = new SimplePriorityQueue<>();
        queue.put(1, "Apple");
        queue.put(1, "Banana");
        queue.put(2, "Lemon");
        queue.put(3, "Kiwi");
        HashSet<String> fruits = new HashSet<>();
        fruits.add("Apple");
        fruits.add("Banana");
        assertEquals(queue.size(), 4);
        assertEquals(queue.pollSmallest().getValue(), fruits);
    }

}