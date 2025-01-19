package org.poo.graph;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrencyGraph extends HashMap<String, ArrayList<Edge>> {

    /**
     * adds an element to the array list that already exists into the HashMap
     * @param from the currency we want to convert from
     * @param edge the currency we want to convert to
     */
    public void add(final String from, final Edge edge) {
        ArrayList<Edge> array;
        if (!containsKey(from)) {
            array = new ArrayList<>();
        } else {
            array = get(from);
        }
        array.add(edge);
        put(from, array);

    }

}
