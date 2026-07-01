# CoreCraft <sup><sub>(java-foundations)</sub></sup>

[![CI](https://github.com/aidarkazybekov/java-foundations/actions/workflows/ci.yml/badge.svg)](https://github.com/aidarkazybekov/java-foundations/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Live site](https://img.shields.io/badge/live-CoreCraft-4f46e5)](https://aidarkazybekov.github.io/java-foundations/)
![Java](https://img.shields.io/badge/Java-21-orange.svg)

Core Java data structures and concurrency primitives **built from scratch**,
test-driven. Not a library to use — a proof of understanding how the building
blocks actually work, with the edge cases and complexity tradeoffs a senior cares about.

**🌐 Live site (concepts + interactive visualizations): https://aidarkazybekov.github.io/java-foundations/**

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
| ✅ producer–consumer | bounded blocking queue over `MyQueue` | `wait`/`notify` signaling, poison-pill shutdown |
| ✅ thread pool | worker threads draining a task queue | reuse over `new Thread`, graceful shutdown, exception isolation |

Framework internals — `src/main/java/com/aidar/foundations/di/`:

| Kata | Idea | Key concept |
|------|------|-------------|
| ✅ mini DI container | constructor injection by reflection | DI/IoC, singleton scope, recursive dependency resolution |

Each type has a focused JUnit suite covering happy paths and edge cases
(collisions, wrap-around, resize, empty/single-element, thread-safety, no-loss under
contention, dependency graphs). **104 tests green.**

## Requirements

- Java 21 (Corretto). Maven picks the JDK from `JAVA_HOME`.

## Run

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn test
```

## Documentation

- 🌐 **[Live site](https://aidarkazybekov.github.io/java-foundations/)** — landing page with all
  concepts and interactive visualizations (auto-deployed from `docs/` via GitHub Actions).
- 📖 **[Concept deep-dives](docs/concepts/)** — how each structure works internally, with
  interview angles (HashMap, heaps, ring buffers, locks/CAS, wait-notify, DI, ...).
- 🎮 **[Interactive visualizations](docs/visualizations/)** — open in a browser and play:
  [ring buffer](docs/visualizations/ring-buffer.html), [min-heap](docs/visualizations/min-heap.html),
  [LRU cache](docs/visualizations/lru-cache.html),
  [race condition](docs/visualizations/race-condition.html),
  [thread pool](docs/visualizations/thread-pool.html).

The site is bilingual (EN/RU toggle) with light/dark themes, and is **data-driven**: the
landing page renders from `docs/content.json`, auto-generated from the files on each deploy.

## Contributing

New concepts and visualizations are welcome — **drop a file, open a PR, and it appears on the
site automatically** (no manual index editing). See **[CONTRIBUTING.md](CONTRIBUTING.md)**.
