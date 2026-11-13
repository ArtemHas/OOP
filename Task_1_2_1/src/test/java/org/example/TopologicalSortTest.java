package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Sorting tests.
 */
public class TopologicalSortTest {

    @Test
    void kahnTest() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);

        assertEquals(List.of(1, 2, 3), graph.topologicalSort());
        assertEquals(3, graph.getEdgeCount());
    }

    @Test
    void depthFirstTest() {
        Graph graph = new AdjacencyMatrixGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);

        assertEquals(List.of(1, 2, 3), graph.topologicalSort(new DepthFirstTopologicalSort()));
        assertEquals(3, graph.getEdgeCount());
    }

    @Test
    void cycleTest() {
        Graph graph = new IncidenceMatrixGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        graph.addEdge(2, 1);

        assertThrows(IllegalArgumentException.class, graph::topologicalSort);
        assertThrows(IllegalArgumentException.class,
                () -> graph.topologicalSort(new DepthFirstTopologicalSort()));
    }
}
