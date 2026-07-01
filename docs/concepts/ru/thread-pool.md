
# Пул потоков (ThreadPool / ExecutorService)

> Почему в проде не делают `new Thread()` на каждую задачу, и как устроен пул внутри. Под капотом любого веб-сервера, `@Async`, parallel-обработки. На собесе — обязательный вопрос про `ExecutorService`.

Связано: producer-consumer-blocking-queue (очередь задач), thread-safety-race-conditions.

---

## Зачем пул

Создавать поток дорого: ресурс ОС + ~1 МБ стека + работа планировщика. `new Thread()` на каждую задачу при тысячах задач → память взрывается, context switching захлёбывается, упираешься в лимиты ОС. И нет контроля над уровнем параллелизма.

**Пул:** создать **фиксированное** число воркеров **один раз** и **переиспользовать** под задачи.

---

## Модель

Пул = **N воркеров** + **очередь задач**. Это **producer-consumer**:
- отправитель (`submit`) = producer кладёт задачу в очередь;
- воркеры = consumers крутят цикл: `take()` задачу → выполнить → повторить;
- задач нет → воркеры блокируются на очереди (не busy-wait).

```java
// воркер
while (true) {
    Runnable task = queue.take();   // блокируется, если пусто
    if (task == POISON) break;      // сигнал стоп
    try { task.run(); }             // выполнить В ЭТОМ потоке (переиспользование!)
    catch (RuntimeException e) { /* не убивать воркера */ }
}
```

🔑 `task.run()`, а НЕ `new Thread(task).start()` — задача исполняется на **существующем** воркере; в этом весь смысл пула.

**Задача = `Runnable`** (`void run()`); `submit(Runnable)` → `queue.put(task)`. (`Callable<T>` возвращает результат через `Future` — следующий уровень.)

---

## Остановка пула

Воркеры крутят вечный цикл — просто бросить нельзя (повиснут на `take()`). Два режима (как у `ExecutorService`):
- **`shutdown()`** — graceful: не принимать новые, доделать очередь, выйти;
- **`shutdownNow()`** — прервать сразу, бросить остаток.

Graceful через **poison pill**: `shutdown()` кладёт по одной таблетке-маркеру на воркера; воркер, взяв её, выходит. FIFO → таблетки в хвосте, реальные задачи доделываются первыми. `awaitTermination()` = `join` всех воркеров.

---

## Изоляция исключений

Если задача бросит `RuntimeException` и `run()` не обёрнут — воркер-поток **умрёт**, пул «худеет». Поэтому `task.run()` оборачивают в try/catch (настоящий `ThreadPoolExecutor` так и делает). Классический вопрос: «что будет, если задача упадёт?».

---

## Реальный мир: `ExecutorService`

Руками пул не пишут — `Executors.newFixedThreadPool(n)` → `ExecutorService`. Под капотом `ThreadPoolExecutor` с ручками: core/max размер, keep-alive, тип очереди (bounded/unbounded), **политика отказа** при переполнении (`AbortPolicy`/`CallerRunsPolicy`/...). `submit` возвращает `Future`.

## Interview-traps

- «Почему не `new Thread` на запрос?» → дорого + неуправляемый параллелизм; пул переиспользует.
- «`task.run()` или `start()` в воркере?» → `run()` (на воркере), иначе теряется смысл пула.
- «Как корректно остановить?» → shutdown (доделать) vs shutdownNow (прервать); poison pill / interrupt.
- «Задача кинула исключение?» → без обёртки воркер умирает; надо ловить.
- «`submit` после `shutdown`?» → `RejectedExecutionException`.

## Connected

- producer-consumer-blocking-queue
- thread-safety-race-conditions

## Где встречалось

`java-foundations` kata #10: `MyThreadPool` поверх `MyBlockingQueue<Runnable>` — N воркеров, `submit`/`shutdown` (poison pills)/`awaitTermination`, try/catch вокруг `task.run()`. Тесты: все задачи выполнены; ≤ poolSize потоков (переиспользование).
