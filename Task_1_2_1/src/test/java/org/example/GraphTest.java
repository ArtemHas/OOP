package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Graph tests.
 */
public class GraphTest {

    @Test
    void adjacencyListTest() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(1);
        graph.addEdge(1, 2);
        graph.addEdge(1, 2);

        assertEquals(2, graph.getVertexCount());
        assertEquals(List.of(2), graph.getNeighbors(1));
        assertFalse(graph.containsEdge(2, 1));
        graph.removeEdge(1, 2);
        assertEquals(0, graph.getEdgeCount());
        graph.addEdge(1, 2);
        graph.removeVertex(2);
        assertEquals(List.of(), graph.getNeighbors(1));
    }

    @Test
    void adjacencyMatrixTest() {
        Graph graph = new AdjacencyMatrixGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        graph.addVertex(3);
        graph.addEdge(2, 3);
        graph.addEdge(1, 3);
        graph.removeEdge(1, 2);
        graph.removeVertex(2);

        assertEquals(List.of(1, 3), graph.getVertices());
        assertEquals(List.of(3), graph.getNeighbors(1));
        assertTrue(graph.containsEdge(1, 3));
        assertEquals(1, graph.getEdgeCount());
    }

    @Test
    void incidenceMatrixTest() {
        Graph graph = new IncidenceMatrixGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        graph.addVertex(3);
        graph.addEdge(1, 3);
        graph.addEdge(2, 2);

        assertEquals(List.of(2), graph.getNeighbors(2));
        graph.removeEdge(2, 2);
        graph.removeVertex(2);
        assertEquals(List.of(1, 3), graph.getVertices());
        assertEquals(List.of(3), graph.getNeighbors(1));
        assertTrue(graph.containsEdge(1, 3));
        assertEquals(1, graph.getEdgeCount());
    }

    @Test
    void equalityTest() {
        Graph first = new AdjacencyListGraph();
        Graph second = new AdjacencyMatrixGraph();
        first.addVertex(1);
        second.addVertex(1);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertEquals("Graph{1=[]}", first.toString());
        second.addVertex(2);
        assertNotEquals(first, second);
    }
}
