import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

/**
 * Tests hash table operations, collision handling, resizing, and iteration.
 */
public class HashTableTest {
    /**
     * Checks the size, lookup, and iterator of a new table.
     */
    @Test
    void emptyTableTest() {
        HashTable<String, Integer> table = new HashTable<>();

        assertEquals(0, table.size());
        assertNull(table.get("one"));
        assertFalse(table.containsKey("one"));
        assertFalse(table.iterator().hasNext());
    }

    /**
     * Checks insertion, lookup, and replacement through put.
     */
    @Test
    void putAndGetTest() {
        HashTable<String, Integer> table = new HashTable<>();

        assertNull(table.put("one", 1));
        assertTrue(table.containsKey("one"));
        assertEquals(1, table.get("one"));
        assertEquals(1, table.put("one", 2));
        assertEquals(2, table.get("one"));
        assertEquals(1, table.size());
    }

    /**
     * Checks value updates and the exception for a missing key.
     */
    @Test
    void updateTest() {
        HashTable<String, Number> table = new HashTable<>();
        table.put("one", 1);

        assertEquals(1, table.update("one", 1.0));
        assertEquals(Double.valueOf(1.0), table.get("one"));
        assertEquals(1, table.size());
        assertThrows(NoSuchElementException.class, () -> table.update("two", 2));
    }

    /**
     * Checks removal by key and by matching key-value pair.
     */
    @Test
    void removeTest() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);

        assertFalse(table.remove("one", 2));
        assertTrue(table.remove("one", 1));
        assertFalse(table.containsKey("one"));
        assertNull(table.remove("one"));
        assertEquals(2, table.remove("two"));
        assertEquals(0, table.size());
    }

    /**
     * Checks that a null key can be stored and removed with a null value.
     */
    @Test
    void nullKeyAndValueTest() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put(null, null);

        assertTrue(table.containsKey(null));
        assertNull(table.get(null));
        assertTrue(table.remove(null, null));
        assertFalse(table.containsKey(null));
    }

    /**
     * Checks that colliding keys can be retrieved and removed independently.
     */
    @Test
    void collisionTest() {
        HashTable<String, Integer> table = new HashTable<>();
        // У строк "Aa" и "BB" одинаковый hashCode().
        table.put("Aa", 1);
        table.put("BB", 2);

        assertEquals(1, table.get("Aa"));
        assertEquals(2, table.get("BB"));
        assertEquals(1, table.remove("Aa"));
        assertNull(table.get("Aa"));
        assertEquals(2, table.get("BB"));
    }

    /**
     * Checks that entries survive table growth and shrinkage.
     */
    @Test
    void resizeTest() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 100; i++) {
            table.put(i, i * 2);
        }
        for (int i = 0; i < 99; i++) {
            assertEquals(i * 2, table.remove(i));
        }

        assertEquals(1, table.size());
        assertEquals(198, table.get(99));
    }

    /**
     * Checks that iteration returns every pair once and detects exhaustion.
     */
    @Test
    void iteratorTest() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Aa", 1);
        table.put("BB", 2);
        table.put("one", 3);

        Map<String, Integer> entries = new HashMap<>();
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            assertNull(entries.put(entry.getKey(), entry.getValue()));
        }

        assertEquals(3, entries.size());
        assertEquals(1, entries.get("Aa"));
        assertEquals(2, entries.get("BB"));
        assertEquals(3, entries.get("one"));
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    /**
     * Checks that adding or removing a key invalidates an existing iterator.
     */
    @Test
    void concurrentModificationTest() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);

        Iterator<Map.Entry<String, Integer>> beforeInsert = table.iterator();
        table.put("two", 2);
        assertThrows(ConcurrentModificationException.class, beforeInsert::hasNext);

        Iterator<Map.Entry<String, Integer>> beforeRemove = table.iterator();
        table.remove("two");
        assertThrows(ConcurrentModificationException.class, beforeRemove::next);
    }

    /**
     * Checks equality and matching hash codes across insertion orders.
     */
    @Test
    void equalityTest() {
        HashTable<String, Integer> first = new HashTable<>();
        first.put("Aa", 1);
        first.put("BB", 2);
        HashTable<String, Integer> second = new HashTable<>();
        second.put("BB", 2);
        second.put("Aa", 1);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());

        second.update("Aa", 3);
        assertNotEquals(first, second);
    }

    /**
     * Checks string formatting for empty and populated tables.
     */
    @Test
    void toStringTest() {
        HashTable<String, Integer> table = new HashTable<>();
        assertEquals("{}", table.toString());

        table.put("one", 1);
        assertEquals("{one=1}", table.toString());

        table.put("two", 2);
        String result = table.toString();
        assertTrue(result.equals("{one=1, two=2}") || result.equals("{two=2, one=1}"));
    }
}