package org.example;

import java.util.List;

/**
 * Topological sorter.
 */
public interface TopologicalSort {

    /**
     * Sorts vertices.
     *
     * @param graph input graph
     * @return topological order
     * @throws IllegalArgumentException if graph contains a cycle
     */
    List<Integer> sort(Graph graph);
}
