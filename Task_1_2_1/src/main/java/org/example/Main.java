package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * 
 * Main class.
 */
public class Main {

    /**
     * Program entry point.
     *
     * @param args program arguments
     * @throws IOException if loading fails
     */
    public static void main(String[] args) throws IOException {
        List<Graph> graphs = List.of(
                new AdjacencyMatrixGraph(),
                new IncidenceMatrixGraph(),
                new AdjacencyListGraph()
        );

        for (Graph graph : graphs) {
            if (args.length > 0) {
                graph.readFromFile(Path.of(args[0]));
            } else {
                for (int vertex = 1; vertex <= 4; vertex++) {
                    graph.addVertex(vertex);
                }
                graph.addEdge(1, 2);
                graph.addEdge(1, 3);
                graph.addEdge(2, 4);
                graph.addEdge(3, 4);
            }
            System.out.println(graph.getClass().getSimpleName() + ": " + graph);
            System.out.println("Kahn: " + graph.topologicalSort());
            System.out.println("DFS: " + graph.topologicalSort(new DepthFirstTopologicalSort()));
        }
    }
}
