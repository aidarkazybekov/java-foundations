package com.aidar.foundations.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyThreadPoolTest {

    @Test
    void runsAllSubmittedTasks() throws InterruptedException {
        MyThreadPool pool = new MyThreadPool(3);
        AtomicInteger counter = new AtomicInteger();
        int tasks = 1000;

        for (int i = 0; i < tasks; i++) {
            pool.submit(counter::incrementAndGet);
        }

        pool.shutdown();
        pool.awaitTermination();

        assertEquals(tasks, counter.get());
    }

    @Test
    void reusesAFixedSetOfThreads() throws InterruptedException {
        int poolSize = 3;
        MyThreadPool pool = new MyThreadPool(poolSize);
        Set<String> threadNames = ConcurrentHashMap.newKeySet();

        for (int i = 0; i < 500; i++) {
            pool.submit(() -> threadNames.add(Thread.currentThread().getName()));
        }

        pool.shutdown();
        pool.awaitTermination();
        System.out.println(threadNames);
        assertTrue(threadNames.size() <= poolSize,
                "expected at most " + poolSize + " threads but got " + threadNames.size());
    }
}
