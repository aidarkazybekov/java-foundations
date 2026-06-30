# Concepts — java-foundations

Подробные разборы концептов, стоящих за каждой ката (рус., в стиле «junior vs middle»,
с разбором internals и interview-traps).

## Структуры данных
- [HashMap internals](hashmap-internals.md) — корзины, коллизии, load factor, resize, bit-spread индекс
- [ArrayList vs LinkedList](arraylist-vs-linkedlist.md) — массив vs ссылки, амортизированный рост, cache locality
- [Ring buffer (circular queue)](ring-buffer-queue.md) — кольцевой буфер, заворот, resize завёрнутого
- [Binary heap & priority queue](binary-heap-priority-queue.md) — дерево в массиве, sift-up/down
- [LRU cache](lru-cache.md) — HashMap + двусвязный список, O(1) get/put + выселение
- [Generic arrays](generic-arrays.md) — почему `new T[]` запрещён (reified vs erased)

## Конкурентность
- [Thread safety & race conditions](thread-safety-race-conditions.md) — гонки, атомарность vs видимость, synchronized vs Atomic/CAS
- [Producer–consumer & blocking queue](producer-consumer-blocking-queue.md) — wait/notify, poison pill
- [Thread pool](thread-pool.md) — воркеры + очередь задач, shutdown, ExecutorService

## Фреймворк
- [Dependency Injection & IoC](dependency-injection-ioc.md) — DI/IoC, singleton scope, рефлексия (как Spring создаёт бины)

> 🎮 Интерактивные визуализации этих концептов — в [`../visualizations/`](../visualizations/).
