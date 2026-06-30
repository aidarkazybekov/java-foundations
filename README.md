# java-foundations

Core Java data structures and concurrency primitives **built from scratch**,
test-driven. Not a library to use — a proof of understanding how the building
blocks actually work, with the edge cases and complexity tradeoffs a senior cares about.

## What this demonstrates

- **Data structure internals** — hashing, collisions, resizing, amortized growth,
  ring buffers, binary heaps
- **Algorithmic reasoning** — amortized O(1), O(log n) heap operations, complexity tradeoffs
- **Concurrency** (in progress) — race conditions, synchronization, `Atomic*`,
  `wait/notify`, thread pools

## Katas

Data structures — `src/main/java/com/aidar/foundations/collections/`:

| Kata | Idea | Key concept |
|------|------|-------------|
| ✅ `MyHashMap` | hash map from scratch | buckets, separate chaining, load factor, resize, bit-spread index |
| ✅ `MyArrayList` | dynamic array | capacity vs size, amortized-O(1) append, doubling resize |
| ✅ `MyLinkedList` | doubly linked list | prev/next relinking, empty/single-element edge cases |
| ✅ `MyStack` | LIFO | composition — thin facade over `MyLinkedList` |
| ✅ `MyQueue` | FIFO ring buffer | `(head+size)%cap` wrap-around, logical-order resize |
| ✅ `MyPriorityQueue` | min-heap | array-mapped complete tree, sift-up / sift-down |
| ✅ `MyLRUCache` | LRU cache | hash map + doubly linked list, O(1) get/put + eviction |

Concurrency — `src/main/java/com/aidar/foundations/concurrency/`:

| Kata | Idea | Key concept |
|------|------|-------------|
| ✅ thread-safe counter | one counter, three implementations | race conditions, atomicity vs visibility, `synchronized` vs `AtomicLong`/CAS |
| ⬜ producer–consumer | bounded blocking queue | `wait`/`notify` inter-thread signaling |
| ⬜ thread pool | task queue + worker threads | |
| ⬜ mini DI container | constructor injection by reflection | |

Each type has a focused JUnit suite covering happy paths and edge cases
(collisions, wrap-around, resize, empty/single-element, thread-safety). **93 tests green.**

## Requirements

- Java 21 (Corretto). Maven picks the JDK from `JAVA_HOME`.

## Run

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn test
```
