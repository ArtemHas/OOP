package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Incidence matrix graph.
 */
public class IncidenceMatrixGraph extends AbstractGraph {

    private final List<Integer> vertices = new ArrayList<>();
    private final List<int[]> columns = new ArrayList<>();

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
        vertices.add(vertex);
        for (int edge = 0; edge < columns.size(); edge++) {
            columns.set(edge, Arrays.copyOf(columns.get(edge), vertices.size()));
        }
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
        for (int edge = columns.size() - 1; edge >= 0; edge--) {
            if (columns.get(edge)[index] != 0) {
                columns.remove(edge);
            }
        }

        vertices.remove(index);
        for (int edge = 0; edge < columns.size(); edge++) {
            int[] oldColumn = columns.get(edge);
            int[] reduced = new int[vertices.size()];
            System.arraycopy(oldColumn, 0, reduced, 0, index);
            System.arraycopy(oldColumn, index + 1, reduced, index, reduced.length - index);
            columns.set(edge, reduced);
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
        int sourceIndex = indexOf(source);
        int targetIndex = indexOf(target);
        if (findEdge(sourceIndex, targetIndex) >= 0) {
            return;
        }
        int[] column = new int[vertices.size()];
        if (sourceIndex == targetIndex) {
            column[sourceIndex] = 2;
        } else {
            column[sourceIndex] = -1;
            column[targetIndex] = 1;
        }
        columns.add(column);
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
        int edge = findEdge(indexOf(source), indexOf(target));
        if (edge < 0) {
            return;
        }
        columns.remove(edge);
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
        return findEdge(indexOf(source), indexOf(target)) >= 0;
    }

    /**
     * Counts edges.
     *
     * @return edge count
     */
    @Override
    public int getEdgeCount() {
        return columns.size();
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
        int index = indexOf(vertex);
        List<Integer> neighbors = new ArrayList<>();

        for (int[] column : columns) {
            if (column[index] == 2) {
                neighbors.add(vertex);
            } else if (column[index] == -1) {
                for (int row = 0; row < vertices.size(); row++) {
                    if (column[row] == 1) {
                        neighbors.add(vertices.get(row));
                        break;
                    }
                }
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

    private int findEdge(int source, int target) {
        for (int edge = 0; edge < columns.size(); edge++) {
            int[] column = columns.get(edge);
            if (source == target) {
                if (column[source] == 2) {
                    return edge;
                }
            } else if (column[source] == -1 && column[target] == 1) {
                return edge;
            }
        }
        return -1;
    }
}
