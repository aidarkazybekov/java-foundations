
# Producer–consumer и блокирующая очередь

> Как потоки **координируются** через `wait`/`notify` (а не просто защищают данные). Паттерн под капотом thread-pool'ов, логгеров, очередей задач. На собесе проверяет, понимаешь ли межпоточную сигнализацию и почему `while`+`notifyAll`.

Связано: thread-safety-race-conditions, ring-buffer-queue (буфер внутри).

---

## Паттерн

Два сорта потоков работают с разной скоростью:
- **производители** кладут элементы в общий буфер;
- **потребители** забирают и обрабатывают.

Буфер их **развязывает** — производитель не ждёт обработки каждого элемента. Буфер **ограничен** (bounded) → backpressure: нельзя расти бесконечно.

Из ограниченности — два условия ожидания:
- буфер **полон** → производитель ждёт места;
- буфер **пуст** → потребитель ждёт элемента.

Busy-wait (`while(full){}`) сжигает CPU. Нужно усыпить поток и разбудить по событию → `wait`/`notify`.

---

## wait / notify

У каждого объекта, кроме замка, есть «комната ожидания» (wait-set):
- `lock.wait()` (держа замок): **отпускает замок** и засыпает. Отпускает — чтобы другой смог изменить условие.
- `lock.notify()` / `notifyAll()`: будит одного / всех; разбуженный заново берёт замок.
- Только внутри `synchronized(lock)`.

```java
public void put(E e) throws InterruptedException {
    synchronized (lock) {
        while (queue.size() == capacity) lock.wait();  // полно → ждём
        queue.enqueue(e);
        lock.notifyAll();                              // будим потребителей
    }
}
public E take() throws InterruptedException {
    synchronized (lock) {
        while (queue.isEmpty()) lock.wait();           // пусто → ждём
        E e = queue.dequeue();
        lock.notifyAll();                             // будим производителей
        return e;
    }
}
```

---

## Три грабли (классика собеса)

1. **`while`, а НЕ `if`** вокруг `wait()`. Причины: (а) **spurious wakeup** — JVM может разбудить без notify; (б) пока разбуженный заново брал замок, другой поток мог забрать слот → надо **перепроверить** условие.
2. **`notifyAll`, а НЕ `notify`.** На одном замке висят и producers, и consumers. `notify` будит случайного одного — может разбудить «не того» → missed signal / deadlock. `notifyAll` безопаснее.
3. **`wait()` отпускает замок, `sleep()` — нет.** `sleep` спит, держа замок (блокирует всех); `wait` отдаёт замок.

---

## Как остановить потребителей (poison pill)

`take()` на пустой блокируется навсегда. Чтобы завершить потребителей корректно — **poison pill**: спец-маркер в очереди, увидев который потребитель выходит. Кладут **по одной таблетке на каждого** потребителя, **после** того как все производители закончили (FIFO → таблетки в хвосте, реальные элементы разберутся первыми).

Тестируют так: N элементов через P producers / C consumers, атомарный счётчик забранного, в конце `count == N` (ничего не потеряно/не задублировано).

---

## Современные альтернативы

- `java.util.concurrent.ArrayBlockingQueue` / `LinkedBlockingQueue` — готовые блокирующие очереди (в проде берут их).
- `Lock` + `Condition` (`await`/`signal`) — точнее wait/notify: можно завести **две** комнаты ожидания (`notFull`, `notEmpty`) и будить только нужную сторону вместо `notifyAll` всех.

## Interview-traps

- «Почему `while`, не `if`?» → spurious wakeup + перепроверка условия.
- «`notify` или `notifyAll`?» → notifyAll, когда на замке разные роли ждут одно событие.
- «`wait` vs `sleep`?» → wait отдаёт замок, sleep — нет.
- «Чем Lock+Condition лучше?» → отдельные условия → нет лишних пробуждений.

## Connected

- thread-safety-race-conditions
- ring-buffer-queue

## Где встречалось

`java-foundations` kata #9: `MyBlockingQueue<E>` поверх `MyQueue` (кольцевой буфер) + `synchronized`/wait/notify; тест с 4 producers / 4 consumers и poison-pill остановкой подтверждает отсутствие потерь.
