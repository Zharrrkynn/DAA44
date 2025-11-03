package main;

import com.google.gson.*;
import graph.*;
import graph.dagsp.DAGPaths;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import model.*;
import utils.Metrics;

import java.io.*;
import java.nio.file.*;


public class Main {
    public static void main(String[] args) throws Exception {

        Path inputDir = Paths.get("data");
        Path outputDir = Paths.get("output");

        if (!Files.exists(outputDir))
            Files.createDirectories(outputDir);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeSpecialFloatingPointValues()
                .create();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir, "*.json")) {
            for (Path inputFile : stream) {

                System.out.println("🔹 Processing: " + inputFile.getFileName());
                Metrics m = new Metrics();
                m.start();

                JsonObject obj = JsonParser.parseReader(new FileReader(inputFile.toFile())).getAsJsonObject();
                int n = obj.get("n").getAsInt();
                int src = obj.get("source").getAsInt();
                JsonArray edgeArr = obj.getAsJsonArray("edges");

                Digraph g = new Digraph(n);
                for (JsonElement e : edgeArr) {
                    JsonObject edge = e.getAsJsonObject();
                    g.addEdge(edge.get("u").getAsInt(),
                            edge.get("v").getAsInt(),
                            edge.get("w").getAsDouble());
                }

               
                TarjanSCC tarjan = new TarjanSCC();
                var sccs = tarjan.findSCCs(g, m);

                Digraph cond = Condensation.build(g, sccs);
                var topoOrder = TopologicalSort.kahn(cond);
                var shortest = DAGPaths.shortest(cond, src, m);
                var longest = DAGPaths.longest(cond, src, m);


                m.stop();

                
                JsonObject result = new JsonObject();
                result.addProperty("input_file", inputFile.getFileName().toString());
                result.addProperty("vertices", n);
                result.addProperty("edges", edgeArr.size());
                result.addProperty("execution_time_ms", m.getTimeMs());
                result.addProperty("dfs_visits", m.dfsVisits);
                result.addProperty("edge_checks", m.edgeChecks);
                result.addProperty("relaxations", m.relaxations);
                result.addProperty("operations_total", m.getTotalOps());

                result.add("sccs", gson.toJsonTree(sccs));
                result.add("topological_order", gson.toJsonTree(topoOrder));
                result.add("shortest_paths", gson.toJsonTree(shortest));
                result.add("longest_paths", gson.toJsonTree(longest));

                String outputName = inputFile.getFileName().toString().replace(".json", "_output.json");
                Path outputFile = outputDir.resolve(outputName);
                try (Writer w = new FileWriter(outputFile.toFile())) {
                    gson.toJson(result, w);
                }

                System.out.printf("✅ Saved: %s (%.2f ms, ops=%d)\n",
                        outputFile.getFileName(), m.getTimeMs(), m.getTotalOps());
            }
        }

        System.out.println("\n🎯 All datasets processed successfully!");
    }
}
