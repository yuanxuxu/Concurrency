package parallelSorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sort {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        int n = 10_000_000;

        for (int i = 0; i < n; i++) {
            list.add(i);
        }

        Collections.shuffle(list);
        int[] array = new int[n];
        for (int i = 0; i < n; i++) array[i] = list.get(i);

        long nano_startTime = System.nanoTime();
        Arrays.sort(array); // 0.841425848 s
        // Arrays.parallelSort(array); // 0.32352153 s
        long nano_endTime = System.nanoTime();
        System.out.println("Time consumed: " + ((double)(nano_endTime - nano_startTime) / 1_000_000_000) + " s");

    }
}
