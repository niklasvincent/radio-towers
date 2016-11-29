package info.lindblad.radio.model;

import java.util.HashMap;


public class Coverage extends HashMap<Coordinates, Boolean> {

    @Override
    public Boolean get(Object k) {
        return containsKey(k) ? super.get(k) : false;
    }

}
