package org.example;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Depth-first sorter.
 */
public class DepthFirstTopologicalSort implements TopologicalSort {

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
        Map<Integer, Integer> colors = new HashMap<>();
        List<Integer> order = new ArrayList<>();

        for (int vertex : graph.getVertices()) {
            if (colors.containsKey(vertex)) {
                continue;
            }
            Deque<Frame> stack = new ArrayDeque<>();
            colors.put(vertex, 1);
            stack.push(new Frame(vertex, graph.getNeighbors(vertex).iterator()));
            while (!stack.isEmpty()) {
                Frame frame = stack.peek();
                if (!frame.neighbors.hasNext()) {
                    colors.put(frame.vertex, 2);
                    order.add(frame.vertex);
                    stack.pop();
                    continue;
                }
                int neighbor = frame.neighbors.next();
                int color = colors.getOrDefault(neighbor, 0);
                if (color == 1) {
                    throw new IllegalArgumentException("Graph contains a directed cycle");
                }
                if (color == 0) {
                    colors.put(neighbor, 1);
                    stack.push(new Frame(neighbor, graph.getNeighbors(neighbor).iterator()));
                }
            }
        }

        Collections.reverse(order);
        return List.copyOf(order);
    }

    private static class Frame {

        private final int vertex;
        private final Iterator<Integer> neighbors;

        private Frame(int vertex, Iterator<Integer> neighbors) {
            this.vertex = vertex;
            this.neighbors = neighbors;
        }
    }
}
