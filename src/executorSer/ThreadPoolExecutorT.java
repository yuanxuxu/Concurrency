package executorSer;

import java.util.concurrent.*;

public class ThreadPoolExecutorT {

    public static void main(String[] args) {
        // CPU intensive: coreCount;
        // IO intensive: much higher count, e.g.100
        int coreCount = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(coreCount);

        ExecutorService service2 = new ThreadPoolExecutor(
                10,
                100,
                120,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(300),
                // Executors.defaultThreadFactory(),
                // new SimpleThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        for (int i = 0; i < 100; i++) {
            service.execute(new Task());
        }

        System.out.println("Thread name: " +
                Thread.currentThread().getName());

    }

    static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println("Thread name: " +
                    Thread.currentThread().getName());
        }
    }

    private static class CustomRejectionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            // logging / operations to perform on rejection
        }
    }

    /**
     * This factory is used when the executor creates a new thread.
     * Using ThreadFactory you can customize the threads created by
     * executor so that they have proper thread names, priority or
     * even they can be set to be daemon also.
     * */
    private static class SimpleThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    }
}