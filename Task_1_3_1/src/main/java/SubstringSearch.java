import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.LongConsumer;

/**
 * Searches UTF-8 files using the Knuth-Morris-Pratt algorithm.
 * Indices count Unicode code points, starting from zero.
 */
public final class SubstringSearch {

    private SubstringSearch() {
    }

    /**
     * Finds all occurrences, including overlapping matches.
     * Only the pattern, a fixed-size input buffer, and the results are stored.
     *
     * @param filename the file to search
     * @param substring the non-empty string to find
     * @return the starting indices in ascending order
     * @throws IOException if reading fails or the file contains invalid UTF-8
     * @throws IllegalArgumentException if the substring is empty
     */
    public static List<Long> find(String filename, String substring) throws IOException {
        List<Long> positions = new ArrayList<>();
        find(filename, substring, positions::add);
        return positions;
    }

    /**
     * Reports matches without storing them, using memory independent of file size.
     *
     * @param filename the file to search
     * @param substring the non-empty string to find
     * @param onMatch the action to perform for each starting index
     * @throws IOException if reading fails or the file contains invalid UTF-8
     * @throws IllegalArgumentException if the substring is empty
     */
    public static void find(String filename, String substring, LongConsumer onMatch)
            throws IOException {
        Objects.requireNonNull(filename, "filename");
        Objects.requireNonNull(substring, "substring");
        Objects.requireNonNull(onMatch, "onMatch");
        int[] pattern = substring.codePoints().toArray();
        if (pattern.length == 0) {
            throw new IllegalArgumentException("Substring must not be empty");
        }

        int[] prefix = prefixFunction(pattern);
        int matched = 0;
        long position = 0;
        try (BufferedReader reader = Files.newBufferedReader(
                Path.of(filename), StandardCharsets.UTF_8)) {
            int symbol;
            while ((symbol = readCodePoint(reader)) != -1) {
                while (matched > 0 && symbol != pattern[matched]) {
                    matched = prefix[matched - 1];
                }
                if (symbol == pattern[matched]) {
                    matched++;
                }
                if (matched == pattern.length) {
                    onMatch.accept(position - pattern.length + 1);
                    matched = prefix[matched - 1];
                }
                position++;
            }
        }
    }

    private static int[] prefixFunction(int[] pattern) {
        int[] prefix = new int[pattern.length];
        for (int i = 1; i < pattern.length; i++) {
            int matched = prefix[i - 1];
            while (matched > 0 && pattern[i] != pattern[matched]) {
                matched = prefix[matched - 1];
            }
            if (pattern[i] == pattern[matched]) {
                matched++;
            }
            prefix[i] = matched;
        }
        return prefix;
    }

    private static int readCodePoint(BufferedReader reader) throws IOException {
        int first = reader.read();
        if (first == -1 || !Character.isHighSurrogate((char) first)) {
            return first;
        }
        int second = reader.read();
        if (second == -1 || !Character.isLowSurrogate((char) second)) {
            throw new IOException("Invalid surrogate pair");
        }
        return Character.toCodePoint((char) first, (char) second);
    }
}
