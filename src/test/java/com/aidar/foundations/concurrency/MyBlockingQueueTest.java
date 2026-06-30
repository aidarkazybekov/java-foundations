package com.aidar.foundations.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class MyBlockingQueueTest {

    // ---------- single-threaded FIFO ----------

    @Test
    void putThenTakeIsFifo() throws InterruptedException {
        MyBlockingQueue<Integer> q = new MyBlockingQueue<>(4);
        q.put(1);
        q.put(2);
        assertEquals(1, q.take());
        assertEquals(2, q.take());
    }

    // ---------- take blocks on empty, resumes after put ----------

    @Test
    void takeBlocksUntilSomethingIsPut() throws InterruptedException {
        MyBlockingQueue<Integer> q = new MyBlockingQueue<>(4);
        AtomicReference<Integer> taken = new AtomicReference<>();

        Thread consumer = new Thread(() -> {
            try { taken.set(q.take()); } catch (InterruptedException ignored) { }
        });
        consumer.start();

        Thread.sleep(100);                 // let the consumer reach wait() on empty
        assertNull(taken.get());           // still blocked — nothing taken yet

        q.put(42);
        consumer.join();
        assertEquals(42, taken.get());     // unblocked and received the value
    }

    // ---------- put blocks on full, resumes after take ----------

    @Test
    void putBlocksUntilSpaceIsFree() throws InterruptedException {
        MyBlockingQueue<Integer> q = new MyBlockingQueue<>(1);
        q.put(1);                          // queue is now full
        AtomicBoolean putFinished = new AtomicBoolean(false);

        Thread producer = new Thread(() -> {
            try { q.put(2); putFinished.set(true); } catch (InterruptedException ignored) { }
        });
        producer.start();

        Thread.sleep(100);                 // let the producer reach wait() on full
        assertFalse(putFinished.get());    // still blocked — queue was full

        assertEquals(1, q.take());         // free a slot
        producer.join();
        assertTrue(putFinished.get());     // producer completed its put
        assertEquals(2, q.take());
    }

    // ---------- many producers + consumers, nothing lost ----------

    @Test
    void multipleProducersAndConsumersLoseNothing() throws InterruptedException {
        int producers = 4;
        int consumers = 4;
        int perProducer = 1000;
        int total = producers * perProducer;
        int POISON = -1;

        MyBlockingQueue<Integer> q = new MyBlockingQueue<>(16);
        AtomicLong sum = new AtomicLong();
        AtomicInteger consumedCount = new AtomicInteger();

        Thread[] producerThreads = new Thread[producers];
        for (int p = 0; p < producers; p++) {
            producerThreads[p] = new Thread(() -> {
                try {
                    for (int j = 0; j < perProducer; j++) q.put(1);   // each item contributes 1
                } catch (InterruptedException ignored) { }
            });
        }

        Thread[] consumerThreads = new Thread[consumers];
        for (int c = 0; c < consumers; c++) {
            consumerThreads[c] = new Thread(() -> {
                try {
                    while (true) {
                        int v = q.take();
                        if (v == POISON) return;           // shutdown signal
                        sum.addAndGet(v);
                        consumedCount.incrementAndGet();
                    }
                } catch (InterruptedException ignored) { }
            });
        }

        for (Thread t : consumerThreads) t.start();
        for (Thread t : producerThreads) t.start();

        for (Thread t : producerThreads) t.join();          // all real items produced
        for (int c = 0; c < consumers; c++) q.put(POISON);  // one poison pill per consumer
        for (Thread t : consumerThreads) t.join();

        assertEquals(total, consumedCount.get());           // no item lost or duplicated
        assertEquals(total, sum.get());                     // integrity of values
    }
}
