package org.poo.graph;

import lombok.Getter;

@Getter
public class Edge {

    private String to;
    private double rate;

    public Edge(final String to, final double rate) {
        this.to = to;
        this.rate = rate;
    }
}
