package graph;

import model.*;
import java.util.*;

public class Condensation {
    public static Digraph build(Digraph g, List<List<Integer>> sccs) {
        int k = sccs.size();
        Map<Integer,Integer> compId = new HashMap<>();
        for (int i=0;i<k;i++)
            for (int v:sccs.get(i)) compId.put(v,i);

        Digraph dag = new Digraph(k);
        Set<String> added = new HashSet<>();

        for (int u=0;u<g.n;u++)
            for (Edge e:g.adj.get(u)) {
                int cu=compId.get(u), cv=compId.get(e.to);
                if (cu!=cv && added.add(cu+"_"+cv))
                    dag.addEdge(cu,cv,e.weight);
            }
        return dag;
    }
}
