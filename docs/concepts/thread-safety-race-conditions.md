
# Thread safety and race conditions

> The foundation of concurrency on a single example — a counter. If you understand why `count++` breaks under threads and the three ways to fix it (synchronized / Atomic / why volatile doesn't help), you'll close half of a concurrency interview.

Related: jmm-visibility-volatile.

---

## Threads and shared memory

A process can have multiple **threads** — independent lines of execution. On a multi-core CPU they run **truly simultaneously**.

- **Local variables** (the thread's stack) are private, safe.
- **Object fields** (the heap) are **shared** between threads. This is where all the bugs are.

`Thread` is a low-level tool: `new Thread(runnable)` + `start()` (run in parallel; `run()` would execute it in the current thread — a common mistake), `join()` (wait for completion). In real code, instead of raw threads you use pools (`ExecutorService`).

---

## Why `count++` breaks

`count++` is **not** one operation but three (read-modify-write): read → +1 → write. The OS can preempt the thread between them. Two threads interleave:

```
count = 5
A: reads 5
B: reads 5         ← both read the same value
A: writes 6
B: writes 6        ← A's increment is overwritten (lost update)
count = 6  (should be 7)
```

This is a **race condition**: the result depends on unpredictable timing. With 10 threads × 10000 increments, a naive counter gives ~20000 instead of 100000 — and a different value every time.

---

## Two DIFFERENT enemies

| Enemy | What it is | How to cure |
|---|---|---|
| **Atomicity** | a compound operation was preempted midway | synchronized, Atomic |
| **Visibility** | a thread doesn't see another's write (it's in the core's cache) | synchronized, volatile, Atomic |

They must be kept separate: `volatile` gives visibility but NOT atomicity.

---

## Three solutions for the counter

### 1. `synchronized` (pessimism: we lock)
Every object has a built-in lock. `synchronized` takes it on entry, releases it on exit — inside, only one thread.
```java
public synchronized void increment() { count++; }
public synchronized long get() { return count; }   // get TOO — for visibility!
```
🔑 Both the writer and the reader must lock on the same lock — otherwise the reader sees a stale value. Best practice: a private `final Object lock` instead of `this` (so the lock doesn't stick out publicly).

### 2. `AtomicLong` (optimism: CAS, lock-free)
```java
private final AtomicLong count = new AtomicLong();
public void increment() { count.incrementAndGet(); }
```
Inside is **CAS** (compare-and-swap), a hardware atomic instruction: `write the new value, only if the value is still == the expected one`. The increment is an optimistic loop:
```
do { cur = get(); next = cur + 1; } while (!compareAndSet(cur, next));
```
If someone cut in — `compareAndSet` returns false → re-read the fresh value → retry. The write goes through only when it's confirmed nobody changed it → a lost update is impossible. No thread ever **sleeps** (lock-free).

### 3. `volatile` — NOT a solution for a counter ❌
`volatile long count; count++` is still a race. `volatile` guarantees only the visibility of a single write, not the atomicity of read-modify-write. Good for a flag (`volatile boolean running`), not for a counter.

---

## synchronized vs Atomic — when to use which

| | synchronized | Atomic (CAS) |
|---|---|---|
| approach | pessimism: lock, wait | optimism: try-retry |
| blocking | yes | no (lock-free) |
| scope | any logic, **multiple fields** consistently | **one** variable, simple operations |
| speed (moderate load) | slower | faster |

Rule: a single counter/flag → Atomic; multiple fields changed consistently → a lock.

---

## How to test concurrency

A race is non-deterministic — a single run may pass by luck. Load it: N threads × M operations, `start()` all of them, then `join()` all of them (two **separate** loops — otherwise the threads run one after another and there's no parallelism), check the total == N*M. A naive one almost always undercounts, a thread-safe one is stable.

## Interview traps

- "Is `count++` atomic?" → no, read-modify-write, a race.
- "Will `volatile` make the counter thread-safe?" → no, only visibility; you need Atomic/synchronized.
- "synchronized only on increment, get is bare — ok?" → no, a visibility hole; the reader must lock too (or the field must be volatile).
- "CAS vs lock?" → optimism vs pessimism; CAS is lock-free, but under fierce contention there are many retries.
- The ABA problem of CAS → `AtomicStampedReference`.

## Connected

- jmm-visibility-volatile
- ring-buffer-queue

## Where it appeared

`java-foundations` kata #8: a `Counter` interface + `NaiveCounter` (racy, a demo), `AtomicCounter`, `SynchronizedCounter`; a multithreaded test harness catches the naive one's race and confirms the correctness of the other two.
