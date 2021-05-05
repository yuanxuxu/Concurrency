package executorSer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CallableT {

    final private Lock lock = new ReentrantLock();
    final private Condition conditionMet = lock.newCondition();
    private boolean firstDone = false;

    public static void main(String[] args) {
        ExecutorService service = new ThreadPoolExecutor(
                10,
                100,
                120,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(300),
                // Executors.defaultThreadFactory(),
                // new SimpleThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        List<Future<String>> allFuture = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Future<String> future = service.submit(new TaskA());
            allFuture.add(future);
        }

        List<String> ans = new ArrayList<>();
        for (Future<String> f : allFuture) {
            try {
                ans.add(f.get()); // blocking, what if can't get?
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        service.shutdown();

        for (String a : ans) System.out.println(a);

        System.out.println("Thread Name: " + Thread.currentThread());
    }

    private static class TaskA implements Callable<String> {

        @Override
        public String call() throws Exception {
            //Thread.currentThread().setName("StringThread");
            // Thread.sleep(1000);
            // String threadId = "1";
            String name =  "Thread Name: " + Thread.currentThread();
            return name;
        }
    }

}
