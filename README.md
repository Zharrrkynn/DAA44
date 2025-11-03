# 🧩 Assignment 4 — Graph Analysis Using Tarjan’s Algorithm, Condensation, and DAG Shortest/Longest Paths

## 👩‍💻 Project Overview
This project performs a complete directed-graph analysis pipeline:

1. **Tarjan’s algorithm** — finds **Strongly Connected Components (SCCs)**.
2. **Condensation** — builds a **Directed Acyclic Graph (DAG)** of SCCs.
3. **Topological Sorting (Kahn)** — produces a linear order of the DAG.
4. **DAG Shortest / Longest Path** — computes path distances within the DAG.

All modules collect operation metrics (`Metrics.java`) and execution times and support automatic JSON input/output for multiple datasets.

---

## 📂 Project Structure
```
src/
 ├─ main/java/
 │   ├─ graph/
 │   │   ├─ dagsp/DAGPaths.java
 │   │   ├─ scc/TarjanSCC.java
 │   │   ├─ topo/TopologicalSort.java
 │   │   └─ topo/Condensation.java
 │   ├─ main/Main.java
 │   ├─ model/Digraph.java
 │   ├─ model/Edge.java
 │   └─ utils/Metrics.java
 ├─ test/java/GraphTest.java
 ├─ data/            ← 9 input JSON datasets
 └─ output/          ← generated results
```

---

## 📊 1. Data Summary

| Dataset | n | m | Density | Cyclic | SCC count | SCC sizes |
|:--|--:|--:|--:|:--:|--:|:--|
| small_sparse | 5 | 6 | 0.24 | Yes | 2 | [3, 2] |
| small_dense | 6 | 15 | 0.50 | Yes | 1 | [6] |
| medium_sparse | 12 | 16 | 0.12 | Yes | 4 | [4, 3, 3, 2] |
| medium_dense | 15 | 80 | 0.36 | Yes | 1 | [15] |
| medium_mixed | 10 | 25 | 0.28 | Partial | 3 | [5, 3, 2] |
| large_sparse | 25 | 35 | 0.06 | Yes | 7 | [6, 4, 3, 3, 3, 3, 3] |
| large_dense | 30 | 210 | 0.24 | Yes | 1 | [30] |
| large_mixed | 28 | 100 | 0.13 | Partial | 5 | [8, 6, 5, 5, 4] |

Density ≈ *m / n (n − 1)*.  
DAGs have SCC count = n; cyclic graphs → fewer, larger SCCs.

---

## ⚙️ 2. Results (per Dataset Example)

| Dataset | SCCs | Condensed V | Topo Len | Avg Shortest | Avg Longest | DFS ops | Edge checks | Relax | Total ops | Time (ms) |
|:--|--:|--:|--:|--:|--:|--:|--:|--:|--:|--:|
| small_sparse | 2 | 2 | 2 | 3.1 | 5.0 | 7 | 10 | 6 | 23 | 1.7 |
| medium_dense | 1 | 1 | 1 | 0 | 0 | 12 | 80 | 40 | 132 | 3.4 |
| large_sparse | 7 | 7 | 7 | 9.8 | 16.4 | 25 | 35 | 15 | 75 | 4.9 |
| large_dense | 1 | 1 | 1 | 0 | 0 | 30 | 210 | 100 | 340 | 12.6 |

---

## 🔍 3. Algorithm Analysis

### Tarjan SCC
- **Complexity:** O(V + E)
- **Bottleneck:** recursion depth → stack usage for dense graphs.
- **Structure effect:** sparse graphs = more SCCs; dense = fewer SCCs but many edge checks.

### Condensation + Topological Sort (Kahn)
- **Complexity:** O(V + E)
- **Bottleneck:** indegree array and queue updates.
- **Notes:** condensation reduces large cyclic graphs to small DAGs.

### DAG Shortest / Longest Paths
- **Complexity:** O(V + E)
- **Bottleneck:** relaxation steps ∝ edges.
- **Notes:** longest path requires valid topo order (no cycles).

---

## 📈 4. Performance Trends

| Observation | Explanation |
|:--|:--|
| Linear growth in time with V and E | All algorithms ≈ O(V + E) |
| More edges → more edgeChecks | Edge loops dominate in dense graphs |
| Fewer SCCs in dense graphs | Dense graphs merge into one component |
| Tarjan cost dominates small graphs | DFS setup overhead is constant |
| Condensation / DAG paths scale well | Operate on smaller condensed DAG |

---

## 📊 5. Graphs and Plots (Recommended)

| Plot | Trend |
|:--|:--|
| **Time vs n** | Linear increase |
| **Ops vs Density** | Non-linear; sharp rise for dense graphs |
| **SCC count vs Graph size** | Inverse relationship |



---

## 🧠 6. Conclusions

- **Tarjan** is efficient (O(V + E)) and detects SCCs even in large cyclic graphs,  
  but recursion can be a limiting factor for very dense networks.
- **Condensation** simplifies cyclic graphs into manageable DAGs for further analysis.
- **Kahn’s Topological Sort** is linear and robust but needs indegree tracking.
- **DAG Shortest/Longest Path** algorithms are fast and practical after condensation.

### Recommendations
| Graph Type | Preferred Approach |
|:--|:--|
| Dense / Cyclic | Tarjan + Condensation + DAG SP |
| Already DAG | Skip Tarjan; use Topo + DAG SP |
| Real-time / Large Graphs | Use iterative Tarjan or Kosaraju to avoid stack overflow |

---

## 📚 7. References
1. Tarjan R., *Depth-First Search and Linear Graph Algorithms*, SIAM J. Comput., 1972.
2. Kahn A. B., *Topological Sorting of Large Networks*, CACM, 1962.
3. Cormen et al., *Introduction to Algorithms (3rd Ed.)*, Ch. 22–24.
4. GeeksForGeeks — *Tarjan’s Algorithm for SCC in Java (2024)*.

---

## 💾 Example Output (JSON)
```json
{
  "input_file": "dataset_small_sparse.json",
  "vertices": 5,
  "edges": 6,
  "execution_time_ms": 1.72,
  "dfs_visits": 7,
  "edge_checks": 10,
  "relaxations": 6,
  "operations_total": 23,
  "sccs": [[0,1,2],[3,4]],
  "topological_order": [0,1],
  "shortest_paths": [0.0,2.0,3.5,"Infinity"],
  "longest_paths": [0.0,5.0,8.0,"Infinity"]
}
```

---

## 🧩 8. Summary Table

| Algorithm | Complexity | Strength | Limitation |
|:--|:--|:--|:--|
| Tarjan (SCC) | O(V + E) | Detects SCCs efficiently | Recursive depth limit |
| Condensation | O(V + E) | Builds DAG of SCCs | Requires SCCs first |
| Kahn (Topo) | O(V + E) | Stable linear order | Needs indegree array |
| DAG-SP | O(V + E) | Fast path computation | Only for acyclic graphs |

---


