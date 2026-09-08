package heapsort;

/*
Main heapsort class
 */
public class HeapSort {
    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static void heapify(int[] arr, int size, int i) {
        int largest = i;
        int son1 = i * 2 + 1;
        int son2 = i * 2 + 2;

        if (son1 < size && arr[son1] > arr[largest]) {
            largest = son1;
        }
        if (son2 < size && arr[son2] > arr[largest]) {
            largest = son2;
        }
        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, size, largest);
        }
    }

    static void heapSort(int[] arr) {
        int size = arr.length;

        for (int i = size / 2 - 1; i >= 0; i--) {
            heapify(arr, size, i);
        }
        for (int i = size - 1; i > 0; i--) {
            swap(arr, 0, i);
            heapify(arr, i, 0);
        }
    }
}