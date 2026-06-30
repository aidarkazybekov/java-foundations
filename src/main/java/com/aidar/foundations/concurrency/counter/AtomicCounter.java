package com.aidar.foundations.concurrency.counter;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicCounter implements Counter {

    private final AtomicLong counter;

    public AtomicCounter() {
        counter = new AtomicLong(0);
    }

    public void increment() {
        counter.incrementAndGet();
    }
    public long get() {
        return counter.get();
    }
}
