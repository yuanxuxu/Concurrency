package condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * Leetcode 1114
 * */
public class Foo {

    final private Lock lock;
    final private Condition conditionMet;
    final private Condition conditionMet2;
    private boolean firstDone;
    private boolean secondDone;

    public Foo() {
        this.lock = new ReentrantLock();
        this.conditionMet = lock.newCondition();
        this.conditionMet2 = lock.newCondition();
        this.firstDone = false;
        this.secondDone = false;
    }

    public void first(Runnable printFirst) throws InterruptedException {
        lock.lock();
        try {
            printFirst.run();
            firstDone = true;
            conditionMet.signal();
        } finally {
            lock.unlock();
        }
        // printFirst.run() outputs "first". Do not change or remove this line.

    }

    public void second(Runnable printSecond) throws InterruptedException {
        lock.lock();
        try {
            while (!firstDone) conditionMet.await();
            printSecond.run();
            secondDone = true;
            conditionMet2.signal();
        } finally {
            lock.unlock();
        }
        // printSecond.run() outputs "second". Do not change or remove this line.

    }

    public void third(Runnable printThird) throws InterruptedException {
        lock.lock();
        try {
            while (!secondDone) conditionMet2.await();
            printThird.run();
            conditionMet2.signal();
        } finally {
            lock.unlock();
        }
        // printThird.run() outputs "third". Do not change or remove this line.

    }
}