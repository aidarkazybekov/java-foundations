package com.aidar.foundations.concurrency;

public class MyThreadPool {

    private static final Runnable POISON = () -> {};
    private static final int QUEUE_CAPACITY = 1024;

    private final MyBlockingQueue<Runnable> taskQueue;
    private final Thread[] workers;



    public MyThreadPool(int poolSize) {
        this.taskQueue = new MyBlockingQueue<>(QUEUE_CAPACITY);
        this.workers = new Thread[poolSize];
        for (int i = 0; i < poolSize; i++) {
            workers[i] = new Thread(this::workerLoop);
            workers[i].start();
        }
    }

    private void workerLoop() {
        try {
            while (true) {
                Runnable task = taskQueue.take();
                if(task == POISON) {
                    break;
                }
                try { task.run(); }
                catch (RuntimeException ex) { /* залогировать, но не убивать воркера */ }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void submit(Runnable task) throws InterruptedException {
        taskQueue.put(task);
    }

    public void shutdown() throws InterruptedException {
        for (int i = 0; i < workers.length; i++) {
            taskQueue.put(POISON);
        }
    }

    public void awaitTermination() throws InterruptedException {
        for (Thread worker : workers) {
            worker.join();
        }
    }
}
