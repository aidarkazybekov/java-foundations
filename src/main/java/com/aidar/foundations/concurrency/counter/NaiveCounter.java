package com.aidar.foundations.concurrency.counter;

public class NaiveCounter implements Counter {
    private long count;

    public NaiveCounter() {
        this.count = 0L;
    }

    public void increment() {
        count ++;
    }

    public long get() {
        return count;
    }
}
