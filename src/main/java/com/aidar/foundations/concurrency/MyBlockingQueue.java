package com.aidar.foundations.concurrency;

import com.aidar.foundations.collections.MyQueue;

public class MyBlockingQueue<E> {
    private final MyQueue<E> queue;
    private final int capacity;
    private final Object lock = new Object();

    public MyBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new MyQueue<>(capacity);
    }

    public MyBlockingQueue() {
        this(10);
    }

    public void put(E e) throws InterruptedException {
        synchronized (lock) {
            while(queue.size() == capacity) {
                lock.wait();
            }
            queue.enqueue(e);
            lock.notifyAll();
        }
    }

    public E take() throws InterruptedException {
        synchronized (lock) {
            while(queue.isEmpty()) {
                lock.wait();
            }
            E e = queue.dequeue();
            lock.notifyAll();
            return e;
        }
    }
}
