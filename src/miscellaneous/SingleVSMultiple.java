package miscellaneous;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SingleVSMultiple {

    private static final long nanoToSecond = 1000_000_000L;
    private static final int numOfThread = 10;
    private static final int numOfTask = 10;
    private static final long N = 1000_000_000L;
    private static final long M = numOfTask * N;

    public static void main(String[] args) {
        Test test = new Test(new SingleThread());
        test.strategy.run();

        test.setStrategy(new MultipleThread());
        test.strategy.run();
    }

    static class Test {
        Strategy strategy;

        public Test(Strategy strategy) {
            this.strategy = strategy;
        }

        public void setStrategy(Strategy strategy) {
            this.strategy = strategy;
        }
    }

    interface Strategy {
        void run();
    }

    static class SingleThread implements Strategy {
        @Override
        public void run() {
            long nano_startTime = System.nanoTime();
            long sum = 0;
            for (long i = 0; i < M; i++) {
                sum += i;
            }
            System.out.printf("Sum is: " + sum + ", ");
            long nano_endTime = System.nanoTime();
            System.out.println("Time consumed: " + ((double)(nano_endTime - nano_startTime) / nanoToSecond) + " s");
        }
    }

    static class MultipleThread implements Strategy {
        @Override
        public void run() {
            long nano_startTime = System.nanoTime();

            ExecutorService executor = Executors.newFixedThreadPool(numOfThread);

            List<Callable<Long>> tasks = new ArrayList<>();
            for (int i = 0; i < numOfTask; i++) {
                final int j = i;
                tasks.add(() -> {
                    long sum = 0;
                    for (long k = j * N; k < (j + 1) * N; k++)
                    sum += k;
                    return sum;
                });
            }

            List<Future<Long>> resultList = new ArrayList<>();

            try {
                resultList = executor.invokeAll(tasks);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executor.shutdown();
            while (!executor.isTerminated()) {}

            long sum = 0;

            for (int i = 0; i < resultList.size(); i++) {
                Future<Long> future = resultList.get(i);
                try {
                    Long result = future.get();
                    sum += result;
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            System.out.printf("Sum is: " + sum + ", ");
            long nano_endTime = System.nanoTime();
            System.out.println("Time consumed: " + ((double)(nano_endTime - nano_startTime) / nanoToSecond) + " s");
        }
    }
}
