import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests streaming search, Unicode indices, and overlapping occurrences.
 */
public class SubstringSearchTest {
    @TempDir
    Path directory;

    /**
     * Checks the example from the assignment.
     */
    @Test
    void exampleTest() throws IOException {
        String filename = writeFile("абракадабра");

        assertEquals(List.of(1L, 8L), SubstringSearch.find(filename, "бра"));
    }

    /**
     * Checks occurrences sharing characters.
     */
    @Test
    void overlappingMatchesTest() throws IOException {
        String filename = writeFile("ааааа");

        assertEquals(List.of(0L, 1L, 2L), SubstringSearch.find(filename, "ааа"));
    }

    /**
     * Checks fallback after several characters have already matched.
     */
    @Test
    void prefixFallbackTest() throws IOException {
        String filename = writeFile("abababacababaca");

        assertEquals(List.of(2L, 8L), SubstringSearch.find(filename, "ababaca"));
    }

    /**
     * Checks matches at both ends of the file and a one-character pattern.
     */
    @Test
    void fileEdgesTest() throws IOException {
        String filename = writeFile("бра-бра");

        assertEquals(List.of(0L, 4L), SubstringSearch.find(filename, "бра"));
        assertEquals(List.of(0L, 4L), SubstringSearch.find(filename, "б"));
        assertEquals(List.of(0L), SubstringSearch.find(filename, "бра-бра"));
    }

    /**
     * Checks an absent pattern, a longer pattern, and an empty file.
     */
    @Test
    void noMatchesTest() throws IOException {
        String filename = writeFile("абра");

        assertEquals(List.of(), SubstringSearch.find(filename, "кадабра"));
        assertEquals(List.of(), SubstringSearch.find(filename, "бро"));
        assertEquals(List.of(), SubstringSearch.find(writeFile(""), "а"));
    }

    /**
     * Checks that letters from different alphabets are decoded as UTF-8.
     */
    @Test
    void differentAlphabetsTest() throws IOException {
        String filename = writeFile("я猫α猫αж");

        assertEquals(List.of(1L, 3L), SubstringSearch.find(filename, "猫α"));
    }

    /**
     * Checks that a supplementary Unicode letter occupies one index.
     */
    @Test
    void supplementaryCharactersTest() throws IOException {
        String letter = "\uD801\uDC00";
        String filename = writeFile("a" + letter + "бра" + letter + "бра");

        assertEquals(List.of(2L, 6L), SubstringSearch.find(filename, "бра"));
        assertEquals(List.of(1L, 5L), SubstringSearch.find(filename, letter + "бра"));
    }

    /**
     * Checks that line separators are preserved and may be part of a match.
     */
    @Test
    void lineSeparatorsTest() throws IOException {
        String filename = writeFile("аб\r\nра\nбра");

        assertEquals(List.of(2L), SubstringSearch.find(filename, "\r\nра\n"));
        assertEquals(List.of(7L), SubstringSearch.find(filename, "бра"));
    }

    /**
     * Checks a match and a surrogate pair across the reader buffer boundary.
     */
    @Test
    void bufferBoundaryTest() throws IOException {
        String letter = "\uD801\uDC00";
        String filename = writeFile("a".repeat(8191) + letter + "бра");

        assertEquals(List.of(8191L), SubstringSearch.find(filename, letter + "бра"));
        assertEquals(List.of(8192L), SubstringSearch.find(filename, "бра"));
    }

    /**
     * Checks that the callback receives matches in ascending order.
     */
    @Test
    void callbackTest() throws IOException {
        String filename = writeFile("абракадабра");
        List<Long> positions = new ArrayList<>();

        SubstringSearch.find(filename, "бра", positions::add);

        assertEquals(List.of(1L, 8L), positions);
    }

    /**
     * Generates a 96 MiB file, larger than the test JVM's 64 MiB heap.
     */
    @Test
    void largeFileTest() throws IOException {
        Path file = directory.resolve("large.txt");
        String block = "a".repeat(8192);
        int blockCount = 12 * 1024;
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (int i = 0; i < blockCount; i++) {
                writer.write(block);
            }
            writer.write("бра");
        }

        assertEquals(List.of((long) block.length() * blockCount),
                SubstringSearch.find(file.toString(), "бра"));
    }

    /**
     * Checks that an empty pattern is rejected explicitly.
     */
    @Test
    void emptySubstringTest() throws IOException {
        String filename = writeFile("абра");

        assertThrows(IllegalArgumentException.class,
                () -> SubstringSearch.find(filename, ""));
    }

    /**
     * Checks validation of null arguments.
     */
    @Test
    void nullArgumentsTest() {
        assertThrows(NullPointerException.class, () -> SubstringSearch.find(null, "а"));
        assertThrows(NullPointerException.class, () -> SubstringSearch.find("input.txt", null));
        assertThrows(NullPointerException.class,
                () -> SubstringSearch.find("input.txt", "а", null));
    }

    /**
     * Checks that a missing file is reported to the caller.
     */
    @Test
    void missingFileTest() {
        String filename = directory.resolve("missing.txt").toString();

        assertThrows(IOException.class, () -> SubstringSearch.find(filename, "а"));
    }

    /**
     * Checks that malformed UTF-8 is not silently replaced.
     */
    @Test
    void invalidEncodingTest() throws IOException {
        Path file = directory.resolve("invalid.txt");
        Files.write(file, new byte[] {(byte) 0xc3, 0x28});

        assertThrows(IOException.class, () -> SubstringSearch.find(file.toString(), "а"));
    }

    private String writeFile(String content) throws IOException {
        Path file = Files.createTempFile(directory, "input-", ".txt");
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file.toString();
    }
}
