package graph.dagsp;

import graph.topo.TopologicalSort;
import model.*;
import utils.Metrics;
import java.util.*;

public class DAGPaths {

    public static double[] shortest(Digraph dag, int src, Metrics m) {
        double[] dist = new double[dag.n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[src] = 0;

        for (int u : TopologicalSort.kahn(dag)) {
            for (Edge e : dag.adj.get(u)) {
                m.edgeChecks++;
                if (dist[u] + e.weight < dist[e.to]) {
                    dist[e.to] = dist[u] + e.weight;
                    m.relaxations++;
                }
            }
        }
        return dist;
    }

    public static double[] longest(Digraph dag, int src, Metrics m) {
        double[] dist = new double[dag.n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        dist[src] = 0;

        for (int u : TopologicalSort.kahn(dag)) {
            for (Edge e : dag.adj.get(u)) {
                m.edgeChecks++;
                if (dist[u] + e.weight > dist[e.to]) {
                    dist[e.to] = dist[u] + e.weight;
                    m.relaxations++;
                }
            }
        }
        return dist;
    }
}
