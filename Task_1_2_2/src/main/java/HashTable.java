import java.util.AbstractMap;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A resizable hash table that stores key-value pairs and resolves collisions with chains.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public final class HashTable<K, V> implements Iterable<Map.Entry<K, V>> {
    private static final int MIN_CAPACITY = 8;
    private static final int MAX_CAPACITY = 1 << 30;

    private static final class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] buckets = newBuckets(0);
    private int size;
    private int modCount;

    /**
     * Creates an empty hash table.
     */
    public HashTable() {
    }

    /**
     * Returns the number of stored pairs.
     *
     * @return the number of pairs
     */
    public int size() {
        return size;
    }

    /**
     * Inserts a pair or replaces the value for an existing key.
     *
     * @param key the key to store
     * @param value the value to associate with the key
     * @return the previous value, or {@code null} if the key was absent
     */
    public V put(K key, V value) {
        int hash = hash(key);
        Node<K, V> node = find(key, hash);
        if (node != null) {
            return replaceValue(node, value);
        }

        int capacity = buckets.length;
        if (capacity == 0) {
            resize(MIN_CAPACITY);
        } else if (capacity < MAX_CAPACITY && size >= capacity - capacity / 4) {
            resize(capacity * 2);
        }

        int index = bucketIndex(hash, buckets.length);
        buckets[index] = new Node<>(hash, key, value, buckets[index]);
        size++;
        modCount++;
        return null;
    }

    /**
     * Looks up a value by its key.
     *
     * @param key the key to look up
     * @return the stored value, or {@code null} if the key is absent
     */
    public V get(K key) {
        Node<K, V> node = find(key, hash(key));
        return node == null ? null : node.value;
    }

    /**
     * Checks whether the given key is present, including keys mapped to {@code null}.
     *
     * @param key the key to check
     * @return {@code true} if the key is present
     */
    public boolean containsKey(K key) {
        return find(key, hash(key)) != null;
    }

    /**
     * Replaces the value for an existing key.
     *
     * @param key the key to update
     * @param value the new value
     * @return the previous value
     * @throws NoSuchElementException if the key is absent
     */
    public V update(K key, V value) {
        Node<K, V> node = find(key, hash(key));
        if (node == null) {
            throw new NoSuchElementException("Key is absent");
        }
        return replaceValue(node, value);
    }

    /**
     * Removes the pair for the given key.
     *
     * @param key the key to remove
     * @return the removed value, or {@code null} if the key was absent
     */
    public V remove(K key) {
        Node<K, V> node = removeNode(key, null, false);
        return node == null ? null : node.value;
    }

    /**
     * Removes a pair only if both its key and value match.
     *
     * @param key the key to remove
     * @param value the expected value
     * @return {@code true} if a pair was removed
     */
    public boolean remove(K key, V value) {
        return removeNode(key, value, true) != null;
    }

    /**
     * Returns an iterator over the pairs in unspecified order.
     * Its traversal methods throw {@link ConcurrentModificationException}
     * after a key is added or removed.
     *
     * @return a fail-fast iterator that returns immutable entries
     */
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new Iterator<Map.Entry<K, V>>() {
            private final int expectedModCount = modCount;
            private int nextBucket;
            private Node<K, V> next = advanceBucket();

            @Override
            public boolean hasNext() {
                checkForModification();
                return next != null;
            }

            @Override
            public Map.Entry<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Node<K, V> current = next;
                next = current.next == null ? advanceBucket() : current.next;
                return new AbstractMap.SimpleImmutableEntry<>(current.key, current.value);
            }

            private Node<K, V> advanceBucket() {
                while (nextBucket < buckets.length) {
                    Node<K, V> head = buckets[nextBucket++];
                    if (head != null) {
                        return head;
                    }
                }
                return null;
            }

            private void checkForModification() {
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    /**
     * Compares this table with another hash table by its stored pairs.
     *
     * @param object the object to compare with
     * @return {@code true} if both tables contain the same pairs
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof HashTable<?, ?>)) {
            return false;
        }
        HashTable<?, ?> other = (HashTable<?, ?>) object;
        if (size != other.size) {
            return false;
        }
        for (Node<K, V> head : buckets) {
            for (Node<K, V> node = head; node != null; node = node.next) {
                Node<?, ?> match = other.find(node.key, node.hash);
                if (match == null || !Objects.equals(node.value, match.value)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Computes an order-independent hash from the stored pairs.
     *
     * @return the hash code of this table
     */
    @Override
    public int hashCode() {
        int result = 0;
        for (Node<K, V> head : buckets) {
            for (Node<K, V> node = head; node != null; node = node.next) {
                result += Objects.hashCode(node.key) ^ Objects.hashCode(node.value);
            }
        }
        return result;
    }

    /**
     * Formats the pairs as comma-separated entries in braces.
     *
     * @return a string such as {@code {one=1, two=2}}
     */
    @Override
    public String toString() {
        StringJoiner result = new StringJoiner(", ", "{", "}");
        for (Map.Entry<K, V> entry : this) {
            result.add(entry.getKey() + "=" + entry.getValue());
        }
        return result.toString();
    }

    private static int hash(Object key) {
        int hash = Objects.hashCode(key);
        return hash ^ (hash >>> 16);
    }

    private static int bucketIndex(int hash, int capacity) {
        return hash & (capacity - 1);
    }

    private Node<K, V> find(Object key, int hash) {
        if (buckets.length == 0) {
            return null;
        }
        for (Node<K, V> node = buckets[bucketIndex(hash, buckets.length)];
             node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(key, node.key)) {
                return node;
            }
        }
        return null;
    }

    private V replaceValue(Node<K, V> node, V value) {
        V previousValue = node.value;
        node.value = value;
        return previousValue;
    }

    private Node<K, V> removeNode(K key, V value, boolean matchValue) {
        if (buckets.length == 0) {
            return null;
        }
        int hash = hash(key);
        int index = bucketIndex(hash, buckets.length);
        Node<K, V> previous = null;
        for (Node<K, V> node = buckets[index]; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(key, node.key)) {
                if (matchValue && !Objects.equals(value, node.value)) {
                    return null;
                }
                if (previous == null) {
                    buckets[index] = node.next;
                } else {
                    previous.next = node.next;
                }

                size--;
                modCount++;
                if (size == 0) {
                    resize(0);
                } else if (buckets.length > MIN_CAPACITY && size <= buckets.length / 4) {
                    resize(buckets.length / 2);
                }
                return node;
            }
            previous = node;
        }
        return null;
    }

    private void resize(int capacity) {
        Node<K, V>[] newBuckets = newBuckets(capacity);
        for (Node<K, V> head : buckets) {
            for (Node<K, V> node = head; node != null; ) {
                Node<K, V> next = node.next;
                int index = bucketIndex(node.hash, capacity);
                node.next = newBuckets[index];
                newBuckets[index] = node;
                node = next;
            }
        }
        buckets = newBuckets;
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Node<K, V>[] newBuckets(int capacity) {
        return (Node<K, V>[]) new Node<?, ?>[capacity];
    }
}