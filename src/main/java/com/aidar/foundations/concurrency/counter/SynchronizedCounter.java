package com.aidar.foundations.concurrency.counter;

public class SynchronizedCounter implements Counter {
    private long counter;

    public SynchronizedCounter() {
        this.counter = 0L;
    }

    public synchronized void increment() {
        this.counter++;
    }

    public synchronized long get() {
        return this.counter;
    }
}
