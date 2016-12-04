package info.lindblad.radio.util;


import java.util.*;

public class Permutations<T> {

    private List<List<T>> permutations;

    /**
     * Create a new list of permuted versions of the original list
     *
     * @param originalList The list to create permutations of
     */
    public Permutations(List<T> originalList) {
        List<T> list = new ArrayList<>(originalList);
        permutations = getPermutations(list);
    }

    /**
     * Get the list of permuted versions of the original list
     *
     * @return List of permuted versions of the list
     */
    public List<List<T>> getPermutations() {
        return permutations;
    }

    /**
     * Generate permutations of the provided list
     *
     * @param list The list to create permutations of
     * @return List of permuted versions of the list
     */
    private List<List<T>> getPermutations(List<T> list) {
        List<List<T>> result = new ArrayList<>();

        if (list.size() == 0) {
            result.add(new ArrayList<T>());
            return result;
        }

        T first = list.remove(0);

        for (List<T> subList : getPermutations(list))
            for (int i = 0; i <= subList.size(); i++) {
                List<T> permutedList = new ArrayList<>(subList);
                permutedList.add(i, first);
                result.add(permutedList);
            }
        return result;
    }

}
