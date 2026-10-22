package org.example;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Kahn sorter.
 */
public class KahnTopologicalSort implements TopologicalSort {

    /**
     * Sorts vertices.
     *
     * @param graph input graph
     * @return topological order
     * @throws IllegalArgumentException if graph contains a cycle
     */
    @Override
    public List<Integer> sort(Graph graph) {
        Objects.requireNonNull(graph, "graph");
        List<Integer> vertices = graph.getVertices();
        Map<Integer, Integer> incoming = new HashMap<>();

        for (int vertex : vertices) {
            incoming.put(vertex, 0);
        }
        for (int vertex : vertices) {
            for (int neighbor : graph.getNeighbors(vertex)) {
                incoming.put(neighbor, incoming.get(neighbor) + 1);
            }
        }

        Deque<Integer> ready = new ArrayDeque<>();
        for (int vertex : vertices) {
            if (incoming.get(vertex) == 0) {
                ready.addLast(vertex);
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            int vertex = ready.removeFirst();
            order.add(vertex);
            for (int neighbor : graph.getNeighbors(vertex)) {
                int degree = incoming.get(neighbor) - 1;
                incoming.put(neighbor, degree);
                if (degree == 0) {
                    ready.addLast(neighbor);
                }
            }
        }

        if (order.size() != vertices.size()) {
            throw new IllegalArgumentException("Graph contains a directed cycle");
        }

        return List.copyOf(order);
    }
}
