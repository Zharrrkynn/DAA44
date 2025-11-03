package graph.scc;

import model.Digraph;
import utils.Metrics;
import java.util.*;

public class TarjanSCC {
    private int index = 0;
    private int[] ids, low;
    private boolean[] onStack;
    private Deque<Integer> stack = new ArrayDeque<>();
    private List<List<Integer>> sccs = new ArrayList<>();
    private Metrics metrics;

    public List<List<Integer>> findSCCs(Digraph g, Metrics m) {
        this.metrics = m;
        int n = g.n;
        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        Arrays.fill(ids, -1);

        for (int i = 0; i < n; i++)
            if (ids[i] == -1)
                dfs(i, g);
        return sccs;
    }

    private void dfs(int at, Digraph g) {
        metrics.dfsVisits++;

        ids[at] = low[at] = index++;
        stack.push(at);
        onStack[at] = true;

        for (var e : g.adj.get(at)) {
            metrics.edgeChecks++;

            int to = e.to;
            if (ids[to] == -1) {
                dfs(to, g);
                low[at] = Math.min(low[at], low[to]);
            } else if (onStack[to]) {
                low[at] = Math.min(low[at], ids[to]);
            }
        }

        if (low[at] == ids[at]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int node = stack.pop();
                onStack[node] = false;
                comp.add(node);
                if (node == at) break;
            }
            sccs.add(comp);
        }
    }
}
