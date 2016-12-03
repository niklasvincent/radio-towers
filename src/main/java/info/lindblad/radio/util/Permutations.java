package info.lindblad.radio.util;


import java.util.*;

public class Permutations<T> {

    private List<T> list;

    public Permutations(List<T> list) {
        this.list = list;
    }

    public List<List<T>> getPermutations() {
        return getPermutations(this.list);
    }

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
