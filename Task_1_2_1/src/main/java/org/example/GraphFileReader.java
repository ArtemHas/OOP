package org.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Graph file reader.
 */
public final class GraphFileReader {

    private GraphFileReader() {
    }

    /**
     * Reads graph.
     *
     * @param destination target graph
     * @param file graph file
     * @throws IOException if loading fails
     */
    public static void readInto(Graph destination, Path file) throws IOException {
        Objects.requireNonNull(destination, "destination");
        Objects.requireNonNull(file, "file");
        String content = Files.readString(file, StandardCharsets.UTF_8).strip();
        String[] tokens = content.isEmpty() ? new String[0] : content.split("\\s+");
        if (tokens.length < 2) {
            throw new IOException("Expected vertex and edge counts");
        }
        int vertexCount = parseInteger(tokens[0]);
        int edgeCount = parseInteger(tokens[1]);
        if (vertexCount < 0 || edgeCount < 0) {
            throw new IOException("Vertex and edge counts must be non-negative");
        }
        if (tokens.length != 2L + vertexCount + 2L * edgeCount) {
            throw new IOException("Token count does not match vertex and edge counts");
        }

        Graph parsed = new AdjacencyListGraph();
        int position = 2;
        for (int index = 0; index < vertexCount; index++) {
            int vertex = parseInteger(tokens[position++]);
            if (parsed.containsVertex(vertex)) {
                throw new IOException("Duplicate vertex: " + vertex);
            }
            parsed.addVertex(vertex);
        }
        for (int index = 0; index < edgeCount; index++) {
            int source = parseInteger(tokens[position++]);
            int target = parseInteger(tokens[position++]);
            if (!parsed.containsVertex(source) || !parsed.containsVertex(target)) {
                throw new IOException("Undeclared endpoint: " + source + " -> " + target);
            }
            if (parsed.containsEdge(source, target)) {
                throw new IOException("Duplicate edge: " + source + " -> " + target);
            }
            parsed.addEdge(source, target);
        }

        for (int vertex : destination.getVertices()) {
            destination.removeVertex(vertex);
        }
        for (int vertex : parsed.getVertices()) {
            destination.addVertex(vertex);
        }
        for (int source : parsed.getVertices()) {
            for (int target : parsed.getNeighbors(source)) {
                destination.addEdge(source, target);
            }
        }
    }

    private static int parseInteger(String token) throws IOException {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException exception) {
            throw new IOException("Invalid integer: " + token, exception);
        }
    }
}
