package org.poo.system;

import org.poo.graph.CurrencyGraph;
import org.poo.graph.Edge;

import java.util.HashSet;

public final class Converter {


    private CurrencyGraph graph;
    private static Converter instance = null;

    /**
     * necessary for Singleton pattern, used because we only need one Converter to do the job
     * @return the instance of the Converter
     */
    public static Converter getInstance() {
        if (instance == null) {
            instance = new Converter();
        }
        return instance;
    }

    private Converter() {
        graph = new CurrencyGraph();
    }

    /**
     * adds two edges for both conversions in the graph, one with the given rate and the other one
     * with a rate of 1 / given rate
     * @param from the currency we want to convert from
     * @param to the currency we want to convert to
     * @param rate the conversion rate from the first to the second currency
     */
    public void addNewRate(final String from, final String to, final double rate) {
        Edge edge = new Edge(to, rate);
        Edge reverseEdge = new Edge(from, 1 / rate);

        graph.add(from, edge);
        graph.add(to, reverseEdge);
    }

    /**
     * a depth first search that returns the current rate of the stage we are in at a given moment
     * when we find the wanted currency, return the final rate
     * @param start the currency where we want to start our new search
     * @param searched the currency we want to get to
     * @param visited a set that keeps track of the visited currencies
     * @param conversionRate it will store our current result
     * @return the wanted conversionRate if there's a way in our graph between the 2 currencies
     */
    private double dfs(final String start, final String searched, final HashSet<String> visited,
                     final double conversionRate) {

        if (start.equals(searched)) {
            return conversionRate;
        }

        visited.add(start);

        for (Edge edge : graph.get(start)) {
            if (!visited.contains(edge.getTo())) {
                double result = dfs(edge.getTo(), searched, visited,
                        conversionRate * edge.getRate());
                if (result != -1) {
                    return result;
                }
            }
        }
        return -1;
    }

    /**
     * declares the HashSet used in the search and returns the result of the dfs method
     * @param from the starting currency
     * @param to the final currency
     * @return the rate between the two
     */
    public double convert(final String from, final String to) {
        HashSet<String> visited = new HashSet<>();
        return dfs(from, to, visited, 1);
    }

    /**
     * used to reset between tests
     */
    public void reset() {
        graph = new CurrencyGraph();
    }

}
