package com.aidar.foundations.concurrency;

import com.aidar.foundations.concurrency.counter.AtomicCounter;
import com.aidar.foundations.concurrency.counter.Counter;
import com.aidar.foundations.concurrency.counter.NaiveCounter;
import com.aidar.foundations.concurrency.counter.SynchronizedCounter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CounterTest {

    @Test
    void singleThreadedIncrement() {
        Counter counter = new NaiveCounter();
        counter.increment();
        counter.increment();
        assertEquals(2, counter.get());
    }

    @Test
    void atomicCounterIsThreadSafe() throws InterruptedException {
        hammer(new AtomicCounter());
    }

    @Test
    void synchronizedCounterIsThreadSafe() throws InterruptedException {
        hammer(new SynchronizedCounter());
    }

    // Hammer the counter from many threads; a thread-safe counter ends at threads * increments.
    private void hammer(Counter counter) throws InterruptedException {
        int threads = 10;
        int incrementsPerThread = 10_000;

        Thread[] workers = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            workers[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
        }

        for (Thread w : workers) w.start();   // launch all
        for (Thread w : workers) w.join();    // wait for all

        assertEquals((long) threads * incrementsPerThread, counter.get());
    }
}
