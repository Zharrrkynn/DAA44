package model;

import java.util.*;

public class Digraph {
    public int n;
    public List<List<Edge>> adj;

    public Digraph(int n) {
        this.n = n;
        adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, double w) {
        adj.get(u).add(new Edge(u, v, w));
    }

    public Digraph reverse() {
        Digraph r = new Digraph(n);
        for (int u = 0; u < n; u++)
            for (Edge e : adj.get(u))
                r.addEdge(e.to, e.from, e.weight);
        return r;
    }
}
