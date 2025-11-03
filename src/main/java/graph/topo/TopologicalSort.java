package graph.topo;

import model.Digraph;
import java.util.*;

public class TopologicalSort {
    public static List<Integer> kahn(Digraph g) {
        int[] indeg = new int[g.n];
        for (var u : g.adj)
            for (var e : u)
                indeg[e.to]++;
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < g.n; i++)
            if (indeg[i] == 0) q.add(i);
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (var e : g.adj.get(u))
                if (--indeg[e.to] == 0) q.add(e.to);
        }
        return order;
    }
}
