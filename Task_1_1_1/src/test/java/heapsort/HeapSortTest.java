package heapsort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeapSortTest {
    @Test
    void testGeneral() {
        int[] input = {4, 10, 3, 5, 1, 2};
        int[] expected = {1, 2, 3, 4, 5, 10};

        HeapSort.heapSort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testAleradySorted() {
        int[] input = {1, 2, 3, 4, 5, 10};
        int[] expected = {1, 2, 3, 4, 5, 10};
        HeapSort.heapSort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testReverseSorted() {
        int[] input = {10, 5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5, 10};

        HeapSort.heapSort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testWithDuplicates() {
        int[] input = {4, 10, 3, 5, 1, 2, 5};
        int[] expected = {1, 2, 3, 4, 5, 5, 10};

        HeapSort.heapSort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testSingleElement() {
        int[] input = {5};
        int[] expected = {5};

        HeapSort.heapSort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testEmptyArray() {
        int[] input = {};
        int[] expected = {};

        HeapSort.heapSort(input);
        assertArrayEquals(expected, input);
    }

}
