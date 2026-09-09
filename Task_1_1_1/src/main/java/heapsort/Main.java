package heapsort;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String [] args){
        int [] arr = new int[10];
        Random rand = new Random();
        for(int i = 0; i < arr.length; i++){
            arr[i] = rand.nextInt(100);
        }
        System.out.println("Сгенерированный массив: " + Arrays.toString(arr));
        HeapSort.heapSort(arr);
        System.out.println("Отсортированный массив: " + Arrays.toString(arr));
    }
}