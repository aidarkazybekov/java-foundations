
# Thread pool (ThreadPool / ExecutorService)

> Why in production you don't do `new Thread()` per task, and how a pool works internally. Under the hood of any web server, `@Async`, and parallel processing. In interviews — a mandatory question about `ExecutorService`.

Related: producer-consumer-blocking-queue (the task queue), thread-safety-race-conditions.

---

## Why a pool

Creating a thread is expensive: an OS resource + ~1 MB of stack + scheduler work. `new Thread()` per task, with thousands of tasks → memory blows up, context switching chokes, you hit OS limits. And there's no control over the level of parallelism.

**The pool:** create a **fixed** number of workers **once** and **reuse** them for tasks.

---

## The model

A pool = **N workers** + **a task queue**. This is **producer-consumer**:
- the submitter (`submit`) = a producer puts a task into the queue;
- the workers = consumers run a loop: `take()` a task → execute → repeat;
- no tasks → the workers block on the queue (not busy-wait).

```java
// worker
while (true) {
    Runnable task = queue.take();   // blocks if empty
    if (task == POISON) break;      // stop signal
    try { task.run(); }             // execute IN THIS thread (reuse!)
    catch (RuntimeException e) { /* don't kill the worker */ }
}
```

🔑 `task.run()`, and NOT `new Thread(task).start()` — the task executes on an **existing** worker; that's the whole point of a pool.

**A task = `Runnable`** (`void run()`); `submit(Runnable)` → `queue.put(task)`. (`Callable<T>` returns a result via `Future` — the next level.)

---

## Stopping the pool

Workers run an infinite loop — you can't just abandon them (they'd hang on `take()`). Two modes (like `ExecutorService`):
- **`shutdown()`** — graceful: don't accept new ones, finish the queue, exit;
- **`shutdownNow()`** — interrupt immediately, discard the rest.

Graceful via a **poison pill**: `shutdown()` puts one marker pill per worker; a worker, upon taking it, exits. FIFO → the pills are at the tail, real tasks are finished first. `awaitTermination()` = `join` all workers.

---

## Exception isolation

If a task throws a `RuntimeException` and `run()` isn't wrapped — the worker thread **dies** and the pool "thins out". That's why `task.run()` is wrapped in try/catch (the real `ThreadPoolExecutor` does exactly this). A classic question: "what happens if a task fails?".

---

## The real world: `ExecutorService`

You don't write a pool by hand — `Executors.newFixedThreadPool(n)` → `ExecutorService`. Under the hood a `ThreadPoolExecutor` with knobs: core/max size, keep-alive, queue type (bounded/unbounded), a **rejection policy** on overflow (`AbortPolicy`/`CallerRunsPolicy`/...). `submit` returns a `Future`.

## Interview traps

- "Why not `new Thread` per request?" → expensive + unmanaged parallelism; a pool reuses.
- "`task.run()` or `start()` in a worker?" → `run()` (on the worker), otherwise the point of the pool is lost.
- "How to stop it cleanly?" → shutdown (finish) vs shutdownNow (interrupt); poison pill / interrupt.
- "A task threw an exception?" → without a wrapper the worker dies; you must catch it.
- "`submit` after `shutdown`?" → `RejectedExecutionException`.

## Connected

- producer-consumer-blocking-queue
- thread-safety-race-conditions

## Where it appeared

`java-foundations` kata #10: `MyThreadPool` on top of `MyBlockingQueue<Runnable>` — N workers, `submit`/`shutdown` (poison pills)/`awaitTermination`, try/catch around `task.run()`. Tests: all tasks executed; ≤ poolSize threads (reuse).
