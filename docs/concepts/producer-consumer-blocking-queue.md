
# Producer–consumer and the blocking queue

> How threads **coordinate** via `wait`/`notify` (rather than just protecting data). The pattern under the hood of thread pools, loggers, and task queues. In interviews it tests whether you understand inter-thread signaling and why `while`+`notifyAll`.

Related: thread-safety-race-conditions, ring-buffer-queue (the buffer inside).

---

## The pattern

Two kinds of threads work at different speeds:
- **producers** put items into a shared buffer;
- **consumers** take and process them.

The buffer **decouples** them — a producer doesn't wait for each item to be processed. The buffer is **bounded** → backpressure: it can't grow indefinitely.

From the boundedness come two wait conditions:
- buffer is **full** → the producer waits for space;
- buffer is **empty** → the consumer waits for an item.

Busy-wait (`while(full){}`) burns CPU. You need to put the thread to sleep and wake it on an event → `wait`/`notify`.

---

## wait / notify

Every object, besides a lock, has a "waiting room" (wait-set):
- `lock.wait()` (holding the lock): **releases the lock** and sleeps. It releases so that another thread can change the condition.
- `lock.notify()` / `notifyAll()`: wakes one / all; the woken thread re-acquires the lock.
- Only inside `synchronized(lock)`.

```java
public void put(E e) throws InterruptedException {
    synchronized (lock) {
        while (queue.size() == capacity) lock.wait();  // full → wait
        queue.enqueue(e);
        lock.notifyAll();                              // wake consumers
    }
}
public E take() throws InterruptedException {
    synchronized (lock) {
        while (queue.isEmpty()) lock.wait();           // empty → wait
        E e = queue.dequeue();
        lock.notifyAll();                             // wake producers
        return e;
    }
}
```

---

## Three pitfalls (interview classics)

1. **`while`, and NOT `if`** around `wait()`. Reasons: (a) **spurious wakeup** — the JVM may wake a thread without a notify; (b) while the woken thread was re-acquiring the lock, another thread could have taken the slot → you must **re-check** the condition.
2. **`notifyAll`, and NOT `notify`.** Both producers and consumers wait on the same lock. `notify` wakes a random one — it may wake "the wrong one" → missed signal / deadlock. `notifyAll` is safer.
3. **`wait()` releases the lock, `sleep()` doesn't.** `sleep` sleeps while holding the lock (blocking everyone); `wait` gives up the lock.

---

## How to stop consumers (poison pill)

`take()` on an empty queue blocks forever. To finish consumers cleanly — a **poison pill**: a special marker in the queue, upon seeing which a consumer exits. Put in **one pill per consumer**, **after** all producers have finished (FIFO → the pills are at the tail, real items are consumed first).

You test it like this: N items through P producers / C consumers, an atomic counter of what's been taken, at the end `count == N` (nothing lost/duplicated).

---

## Modern alternatives

- `java.util.concurrent.ArrayBlockingQueue` / `LinkedBlockingQueue` — ready-made blocking queues (in production you use these).
- `Lock` + `Condition` (`await`/`signal`) — more precise than wait/notify: you can set up **two** waiting rooms (`notFull`, `notEmpty`) and wake only the needed side instead of `notifyAll` on everyone.

## Interview traps

- "Why `while`, not `if`?" → spurious wakeup + condition re-check.
- "`notify` or `notifyAll`?" → notifyAll, when different roles on the lock wait for the same event.
- "`wait` vs `sleep`?" → wait gives up the lock, sleep doesn't.
- "Why is Lock+Condition better?" → separate conditions → no unnecessary wakeups.

## Connected

- thread-safety-race-conditions
- ring-buffer-queue

## Where it appeared

`java-foundations` kata #9: `MyBlockingQueue<E>` on top of `MyQueue` (a ring buffer) + `synchronized`/wait/notify; a test with 4 producers / 4 consumers and poison-pill shutdown confirms no losses.
