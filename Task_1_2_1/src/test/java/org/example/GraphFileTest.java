package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** 
 * File loading tests. 
*/
public class GraphFileTest {

    @Test
    void readGraphTest() throws IOException {
        Graph graph = new IncidenceMatrixGraph();
        graph.addVertex(99);
        graph.readFromFile(Path.of("src/test/resources/graph.txt"));

        assertEquals(List.of(10, 20, 30), graph.getVertices());
        assertEquals(List.of(20), graph.getNeighbors(10));
        assertEquals(2, graph.getEdgeCount());
    }

    @Test
    void invalidFileTest() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex(99);

        assertThrows(IOException.class,
                () -> graph.readFromFile(Path.of("src/test/resources/invalid-graph.txt")));
        assertEquals(List.of(99), graph.getVertices());
    }
}
