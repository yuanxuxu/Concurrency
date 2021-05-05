package condition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class PrintInOrderSemaphores {
    static Semaphore[] semaphores; // permits, only thread acquired permit can run

    public static void main(String[] args) {
        long nano_startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(8);

        semaphores = new Semaphore[2];
        semaphores[0] = new Semaphore(0);
        semaphores[1] = new Semaphore(0);

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
        A.run();
        semaphores[0].release();
    }

    public static void second(Runnable B) throws InterruptedException {
        semaphores[0].acquire();
        B.run();
        semaphores[0].release();
        semaphores[1].release();
    }

    public static void third(Runnable C) throws InterruptedException {
        semaphores[1].acquire();
        C.run();
        semaphores[1].release();
    }
}
