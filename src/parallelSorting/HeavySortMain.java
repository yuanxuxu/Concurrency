package parallelSorting;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Sorting in Parallel in Java with Executors, Ross Anderson
 * */
public class HeavySortMain {

    public static void main(String[] args) {
        int problemSize = 20_000_000;
        int numThreads = 6;
        Random random = new Random();
        Integer[] sortArray = new Integer[problemSize];
        for (int i = 0; i < problemSize; i++) {
            sortArray[i] = random.nextInt(Integer.MAX_VALUE);
        }
        long startTime2 = System.nanoTime();
        Arrays.sort(sortArray);
        System.out.println("Single Threaded parallelSorting.Sort: time taken " +
                ((double)(System.nanoTime() - startTime2) / 1_000_000_000));
        Collections.shuffle(Arrays.asList(sortArray));

        final ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        long startTime = System.currentTimeMillis();
        HeavySort.ArrayFactory<Integer> factory = new HeavySort.ArrayFactory<>() {

            @Override
            public Integer[] buildArray(int length) {
                return new Integer[length];
            }
        };
        HeavySort.sort(sortArray, executor, numThreads, factory);
        System.out.println("Multi-Threaded sort: time taken " +
                (double)(System.currentTimeMillis() - startTime) / 1000);

        for (int i = 0; i < sortArray.length - 1; i++) {
            if (sortArray[i] > sortArray[i + 1]) {
                System.err.println("Error: element at " + i
                        + " : " + sortArray[i]  );
                System.err.println("Error: element at " +
                        (i+1) + " : " + sortArray[i+1]  );
            }
        }
        executor.shutdown();
    }
}











