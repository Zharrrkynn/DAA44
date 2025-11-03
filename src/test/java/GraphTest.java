import graph.*;
import graph.dagsp.DAGPaths;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import model.*;
import utils.Metrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class GraphTest {

    @Test
    void testTarjanSCC_Correctness() {
        // Example graph with two strongly connected components: {0,1,2} and {3,4}
        Digraph g = new Digraph(5);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 3, 1);
        g.addEdge(2, 3, 1);

        Metrics m = new Metrics();
        m.start();
        TarjanSCC tarjan = new TarjanSCC();
        var sccs = tarjan.findSCCs(g, m);
        m.stop();

        assertEquals(2, sccs.size(), "Should find 2 SCCs");
        assertTrue(m.dfsVisits > 0, "DFS count must be positive");
        assertTrue(m.getTimeMs() >= 0, "Execution time must be non-negative");
    }

    @Test
    void testCondensationAndToposort() {
        Digraph g = new Digraph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 3, 1);

        Metrics m = new Metrics();
        TarjanSCC tarjan = new TarjanSCC();
        var sccs = tarjan.findSCCs(g, m);
        Digraph cond = Condensation.build(g, sccs);

        var order = TopologicalSort.kahn(cond);

        // Graph is a simple chain, so topological order is 0→1→2→3
        assertEquals(4, order.size());
        assertEquals(0, order.getFirst());
    }

    @Test
    void testDAGPaths_MetricsNonZero() {
        Digraph g = new Digraph(3);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);

        Metrics m = new Metrics();
        var shortest = DAGPaths.shortest(g, 0, m);
        var longest = DAGPaths.longest(g, 0, m);

        assertTrue(shortest[2] < Double.POSITIVE_INFINITY, "Shortest path must exist");
        assertTrue(longest[2] > Double.NEGATIVE_INFINITY, "Longest path must exist");
        assertTrue(m.edgeChecks > 0, "Edge checks should be counted");
    }
}
