package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Adjacency matrix graph.
 */
public class AdjacencyMatrixGraph extends AbstractGraph {

    private final List<Integer> vertices = new ArrayList<>();
    private boolean[][] matrix = new boolean[0][0];

    /**
     * Adds vertex.
     *
     * @param vertex vertex label
     */
    @Override
    public void addVertex(int vertex) {
        if (containsVertex(vertex)) {
            return;
        }
        int size = vertices.size();
        boolean[][] expanded = new boolean[size + 1][size + 1];

        for (int row = 0; row < size; row++) {
            System.arraycopy(matrix[row], 0, expanded[row], 0, size);
        }

        matrix = expanded;
        vertices.add(vertex);
    }

    /**
     * Removes vertex.
     *
     * @param vertex vertex label
     */
    @Override
    public void removeVertex(int vertex) {
        int index = vertices.indexOf(vertex);
        if (index < 0) {
            return;
        }
        int size = vertices.size();
        boolean[][] reduced = new boolean[size - 1][size - 1];
        int newRow = 0;

        for (int row = 0; row < size; row++) {
            if (row == index) {
                continue;
            }
            int newColumn = 0;
            for (int column = 0; column < size; column++) {
                if (column == index) {
                    continue;
                }
                reduced[newRow][newColumn] = matrix[row][column];
                newColumn++;
            }
            newRow++;
        }

        matrix = reduced;
        vertices.remove(index);
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
        int row = indexOf(source);
        int column = indexOf(target);
        matrix[row][column] = true;
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
        int row = indexOf(source);
        int column = indexOf(target);
        matrix[row][column] = false;
    }

    /**
     * Checks vertex.
     *
     * @param vertex vertex label
     * @return whether vertex exists
     */
    @Override
    public boolean containsVertex(int vertex) {
        return vertices.contains(vertex);
    }

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
        return matrix[indexOf(source)][indexOf(target)];
    }

    /**
     * Returns vertices.
     *
     * @return vertex list
     */
    @Override
    public List<Integer> getVertices() {
        return List.copyOf(vertices);
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
        int row = indexOf(vertex);
        List<Integer> neighbors = new ArrayList<>();

        for (int column = 0; column < vertices.size(); column++) {
            if (matrix[row][column]) {
                neighbors.add(vertices.get(column));
            }
        }

        return List.copyOf(neighbors);
    }

    private int indexOf(int vertex) {
        int index = vertices.indexOf(vertex);
        if (index < 0) {
            throw new IllegalArgumentException("Unknown vertex: " + vertex);
        }
        return index;
    }
}
