package heapsort;

import java.util.Arrays;
import java.util.Random;

/**
 * The main class to demonstrate the HeapSort algorithm.
 */
public class Main {

    /**
     * The entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        int[] arr = new int[10];
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(100);
        }
        System.out.println("Generated array: " + Arrays.toString(arr));
        HeapSort.heapSort(arr);
        System.out.println("Sorted array: " + Arrays.toString(arr));
    }
}