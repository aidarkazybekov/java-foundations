# java-foundations

Core Java and concurrency primitives **built from scratch**, test-driven. Not a
library to use — a proof of understanding how the building blocks actually work.

## What this demonstrates

- **Data structure internals** — hashing, collisions, resizing, eviction
- **Concurrency** — race conditions, synchronization, `Atomic*`, `wait/notify`, thread pools
- **Framework internals** — how dependency injection works under the hood

## Katas

| # | Kata | Concept |
|---|------|---------|
| 1 | `hashmap` | Own `HashMap`: buckets, collision chaining, load factor, resize |
| 2 | `lru` | LRU cache: hash map + doubly linked list, O(1) eviction |
| 3 | `counter` | Thread-safe counter: `synchronized` vs `AtomicLong` |
| 4 | `producerconsumer` | Producer–Consumer with a bounded blocking queue |
| 5 | `threadpool` | Own thread pool: task queue + worker threads |
| 6 | `di` | Mini DI container: constructor injection by reflection |

Each kata lives in its own package under `src/main/java/com/aidar/foundations/`
with tests under `src/test/java/...`.

## Requirements

- Java 21 (Corretto). Maven uses `JAVA_HOME`; point it at a JDK 21.

## Run

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn test
```
