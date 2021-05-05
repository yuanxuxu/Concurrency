package condition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class PrintInOrderReentrantLock {

    final private static Lock lock = new ReentrantLock();
    final private static Condition conditionMet = lock.newCondition();
    final private static Condition conditionMet2 = lock.newCondition();
    private static boolean  firstDone = false;
    private static boolean  secondDone = false;

    public static void main(String[] args){
        long nano_startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(8);

        Runnable A = () -> { System.out.printf("a, "); };
        Runnable B = () -> { System.out.printf("b, "); };
        Runnable C = () -> { System.out.printf("c, "); };

        List<Runnable> list = new ArrayList<>();
        list.add(() -> {
            try {
                third(C);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        list.add(() -> {
            try {
                first(A);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        list.add(() -> {
            try {
                second(B);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Collections.shuffle(list);
        for (Runnable R : list) {
            executor.execute(R);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {}
        long nano_endTime = System.nanoTime();

        System.out.println("Time consumed: " + ((double)(nano_endTime - nano_startTime) / 1_000_000_000) + " s");

    }

    public static void first(Runnable A) throws InterruptedException {
        lock.lock();
        try {
            A.run();
            firstDone = true;
            conditionMet.signal();
        } finally {
            lock.unlock();
        }
    }

    public static void second(Runnable B) throws InterruptedException {
        lock.lock();
        try {
            while (!firstDone) conditionMet.await();
            B.run();
            secondDone = true;
            conditionMet2.signal();
        } finally {
            lock.unlock();
        }
    }

    public static void third(Runnable C) throws InterruptedException {
        lock.lock();
        try {
            while (!secondDone) conditionMet2.await();
            C.run();
            secondDone = true;
            conditionMet2.signal();
        } finally {
            lock.unlock();
        }
    }
}
