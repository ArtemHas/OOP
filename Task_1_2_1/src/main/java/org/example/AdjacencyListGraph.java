package org.example;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Adjacency list graph.
 */
public class AdjacencyListGraph extends AbstractGraph {

    private final Map<Integer, Set<Integer>> adjacency = new LinkedHashMap<>();

    /**
     * Adds vertex.
     *
     * @param vertex vertex label
     */
    @Override
    public void addVertex(int vertex) {
        if (!containsVertex(vertex)) {
            adjacency.put(vertex, new LinkedHashSet<>());
        }
    }

    /**
     * Removes vertex.
     *
     * @param vertex vertex label
     */
    @Override
    public void removeVertex(int vertex) {
        if (adjacency.remove(vertex) == null) {
            return;
        }
        for (Set<Integer> neighbors : adjacency.values()) {
            neighbors.remove(vertex);
        }
    }

    /**
     * Adds edge.
     *
     * @param source source vertex
     * @param target target vertex
     * @throws IllegalArgumentException if a vertex is missing
     */
    @Override
    public void addEdge(int source, int target) {
        requireVertex(source);
        requireVertex(target);
        adjacency.get(source).add(target);
    }

    /**
     * Removes edge.
     *
     * @param source source vertex
     * @param target target vertex
     * @throws IllegalArgumentException if a vertex is missing
     */
    @Override
    public void removeEdge(int source, int target) {
        requireVertex(source);
        requireVertex(target);
        adjacency.get(source).remove(target);
    }

    /**
     * Checks vertex.
     *
     * @param vertex vertex label
     * @return whether vertex exists
     */
    @Override
    public boolean containsVertex(int vertex) {
        return adjacency.containsKey(vertex);
    }

    /**
     * Returns vertices.
     *
     * @return vertex list
     */
    @Override
    public List<Integer> getVertices() {
        return List.copyOf(adjacency.keySet());
    }

    /**
     * Returns neighbors.
     *
     * @param vertex vertex label
     * @return neighbor list
     * @throws IllegalArgumentException if a vertex is missing
     */
    @Override
    public List<Integer> getNeighbors(int vertex) {
        requireVertex(vertex);
        return List.copyOf(adjacency.get(vertex));
    }

    private void requireVertex(int vertex) {
        if (!containsVertex(vertex)) {
            throw new IllegalArgumentException("Unknown vertex: " + vertex);
        }
    }
}
