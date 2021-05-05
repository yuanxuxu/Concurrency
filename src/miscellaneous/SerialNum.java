package miscellaneous;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class SerialNum {

    private static final AtomicLong nextSerialNum = new AtomicLong(0);

    public static void main(String[] args) {
        long nano_startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 10; i++) {
            Runnable worker = new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 50_000_000; j++) {
                        nextSerialNum.getAndIncrement();
                    }
                }
            };
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {}
        long nano_endTime = System.nanoTime();

        System.out.println("Time consumed: " + ((double)(nano_endTime - nano_startTime) / 1_000_000_000) + " s");
        System.out.println(String.format("%,d", nextSerialNum.get())); // 500,000,000
    }

}
