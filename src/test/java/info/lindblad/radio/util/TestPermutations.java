package info.lindblad.radio.util;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestPermutations extends TestCase {

    public TestPermutations(String testName) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite(TestPermutations.class);
    }

    /**
     * Test generating permutations of the list (1, 2, 3). Should result in six different permuted lists:
     *
     *  (1, 2, 3)
     *  (2, 1, 3)
     *  (2, 3, 1)
     *  (1, 3, 2)
     *  (3, 1, 2)
     *  (3, 2, 1)
     *
     */
    public void testSimplePermutations() {
        ArrayList<Integer> originalList = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3}));

        Permutations<Integer> permutations = new Permutations<>(originalList);

        List<List<Integer>> permutedLists = permutations.getPermutations();

        ArrayList<List<Integer>> expectedLists = new ArrayList<>();

        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{2, 1, 3})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{2, 3, 1})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{1, 3, 2})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{3, 1, 2})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{3, 2, 1})));

        assertEquals(6, permutedLists.size());
        assertEquals(expectedLists, permutedLists);
    }

    /**
     * Test generating permutations of the list (1, 2, 3, 4). Should result in 24 different permuted lists:
     *
     *  (1, 2, 3, 4)
     *  (2, 1, 3, 4)
     *  (2, 3, 1, 4)
     *  (2, 3, 4, 1)
     *  (1, 3, 2, 4)
     *  (3, 1, 2, 4)
     *  (3, 2, 1, 4)
     *  (3, 2, 4, 1)
     *  (1, 3, 4, 2)
     *  (3, 1, 4, 2)
     *  (3, 4, 1, 2),
     *  (3, 4, 2, 1),
     *  (1, 2, 4, 3),
     *  (2, 1, 4, 3),
     *  (2, 4, 1, 3),
     *  (2, 4, 3, 1),
     *  (1, 4, 2, 3),
     *  (4, 1, 2, 3),
     *  (4, 2, 1, 3),
     *  (4, 2, 3, 1),
     *  (1, 4, 3, 2),
     *  (4, 1, 3, 2),
     *  (4, 3, 1, 2),
     *  (4, 3, 2, 1)
     *
     */
    public void testLongerPermutations() {
        ArrayList<Integer> originalList = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4}));

        Permutations<Integer> permutations = new Permutations<>(originalList);

        List<List<Integer>> permutedLists = permutations.getPermutations();

        ArrayList<List<Integer>> expectedLists = new ArrayList<>();

        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{2, 1, 3, 4})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{2, 3, 1, 4})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{2, 3, 4, 1})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{1, 3, 2, 4})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{3, 1, 2, 4})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{3, 2, 1, 4})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{3, 2, 4, 1})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{1, 3, 4, 2})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{3, 1, 4, 2})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{3, 4, 1, 2})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{3, 4, 2, 1})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 4, 3})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{2, 1, 4, 3})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{2, 4, 1, 3})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{2, 4, 3, 1})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{1, 4, 2, 3})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{4, 1, 2, 3})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{4, 2, 1, 3})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{4, 2, 3, 1})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{1, 4, 3, 2})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{4, 1, 3, 2})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{4, 3, 1, 2})));
        expectedLists.add(new ArrayList<>(Arrays.asList(new Integer[]{4, 3, 2, 1})));

        assertEquals(24, permutedLists.size());
        assertEquals(expectedLists, permutedLists);
    }

    /**
     * Make sure that generating permutations of a list does not mutate the original list.
     */
    public void testOriginalListIntact() {
        ArrayList<Integer> originalList = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4}));

        Permutations<Integer> permutations = new Permutations<>(originalList);

        permutations.getPermutations();

        ArrayList<Integer> expectedOriginalList = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4}));

        assertEquals(expectedOriginalList, originalList);
    }

}
