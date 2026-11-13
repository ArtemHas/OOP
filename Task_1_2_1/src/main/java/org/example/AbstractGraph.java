package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * Graph base.
 */
public abstract class AbstractGraph implements Graph {

    /**
     * Checks edge.
     *
     * @param source source vertex
     * @param target target vertex
     * @return whether edge exists
     * @throws IllegalArgumentException if a vertex is missing
     */
    @Override
    public boolean containsEdge(int source, int target) {
        if (!containsVertex(target)) {
            throw new IllegalArgumentException("Unknown vertex: " + target);
        }
        return getNeighbors(source).contains(target);
    }

    /**
     * Counts vertices.
     *
     * @return vertex count
     */
    @Override
    public int getVertexCount() {
        return getVertices().size();
    }

    /**
     * Counts edges.
     *
     * @return edge count
     */
    @Override
    public int getEdgeCount() {
        int count = 0;

        for (int vertex : getVertices()) {
            count += getNeighbors(vertex).size();
        }

        return count;
    }

    /**
     * Reads graph.
     *
     * @param file graph file
     * @throws IOException if loading fails
     */
    @Override
    public void readFromFile(Path file) throws IOException {
        GraphFileReader.readInto(this, file);
    }

    /**
     * Sorts vertices.
     *
     * @return topological order
     * @throws IllegalArgumentException if graph contains a cycle
     */
    @Override
    public List<Integer> topologicalSort() {
        return topologicalSort(new KahnTopologicalSort());
    }

    /**
     * Sorts vertices.
     *
     * @param algorithm sorting algorithm
     * @return topological order
     * @throws IllegalArgumentException if graph contains a cycle
     */
    @Override
    public List<Integer> topologicalSort(TopologicalSort algorithm) {
        return Objects.requireNonNull(algorithm, "algorithm").sort(this);
    }

    /**
     * Compares graphs.
     *
     * @param other other object
     * @return whether graphs are equal
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Graph)) {
            return false;
        }
        Graph graph = (Graph) other;
        if (!new HashSet<>(getVertices()).equals(new HashSet<>(graph.getVertices()))) {
            return false;
        }
        for (int vertex : getVertices()) {
            Set<Integer> neighbors = new HashSet<>(getNeighbors(vertex));
            Set<Integer> otherNeighbors = new HashSet<>(graph.getNeighbors(vertex));

            if (!neighbors.equals(otherNeighbors)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates hash.
     *
     * @return graph hash
     */
    @Override
    public int hashCode() {
        int hash = 0;

        for (int vertex : getVertices()) {
            hash += Objects.hash(vertex, new HashSet<>(getNeighbors(vertex)));
        }

        return hash;
    }

    /**
     * Returns string.
     *
     * @return graph string
     */
    @Override
    public String toString() {
        Map<Integer, List<Integer>> adjacency = new TreeMap<>();

        for (int vertex : getVertices()) {
            List<Integer> neighbors = new ArrayList<>(getNeighbors(vertex));
            Collections.sort(neighbors);
            adjacency.put(vertex, neighbors);
        }

        return "Graph" + adjacency;
    }
}
