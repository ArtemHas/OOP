package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Graph interface.
 */
public interface Graph {

    /**
     * Adds vertex.
     *
     * @param vertex vertex label
     */
    void addVertex(int vertex);

    /**
     * Removes vertex.
     *
     * @param vertex vertex label
     */
    void removeVertex(int vertex);

    /**
     * Adds edge.
     *
     * @param source source vertex
     * @param target target vertex
     * @throws IllegalArgumentException if a vertex is missing
     */
    void addEdge(int source, int target);

    /**
     * Removes edge.
     *
     * @param source source vertex
     * @param target target vertex
     * @throws IllegalArgumentException if a vertex is missing
     */
    void removeEdge(int source, int target);

    /**
     * Returns vertices.
     *
     * @return vertex list
     */
    List<Integer> getVertices();

    /**
     * Returns neighbors.
     *
     * @param vertex vertex label
     * @return neighbor list
     * @throws IllegalArgumentException if a vertex is missing
     */
    List<Integer> getNeighbors(int vertex);

    /**
     * Checks vertex.
     *
     * @param vertex vertex label
     * @return whether vertex exists
     */
    boolean containsVertex(int vertex);

    /**
     * Checks edge.
     *
     * @param source source vertex
     * @param target target vertex
     * @return whether edge exists
     * @throws IllegalArgumentException if a vertex is missing
     */
    boolean containsEdge(int source, int target);

    /**
     * Counts vertices.
     *
     * @return vertex count
     */
    int getVertexCount();

    /**
     * Counts edges.
     *
     * @return edge count
     */
    int getEdgeCount();

    /**
     * Reads graph.
     *
     * @param file graph file
     * @throws IOException if loading fails
     */
    void readFromFile(Path file) throws IOException;

    /**
     * Sorts vertices.
     *
     * @return topological order
     * @throws IllegalArgumentException if graph contains a cycle
     */
    List<Integer> topologicalSort();

    /**
     * Sorts vertices.
     *
     * @param algorithm sorting algorithm
     * @return topological order
     * @throws IllegalArgumentException if graph contains a cycle
     */
    List<Integer> topologicalSort(TopologicalSort algorithm);
}
