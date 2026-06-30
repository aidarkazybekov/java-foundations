# Interactive visualizations

Автономные HTML-страницы, объясняющие структуры и конкурентность «на пальцах» — открой
любую в браузере и поиграй.

| Визуализация | Что показывает |
|---|---|
| [ring-buffer.html](ring-buffer.html) | кольцевой буфер: enqueue/dequeue, движение head, заворот через край |
| [min-heap.html](min-heap.html) | бинарная куча: add (sift-up) / poll (sift-down), дерево ↔ массив |
| [lru-cache.html](lru-cache.html) | LRU: put/get двигают свежесть, вылет самого старого |
| [race-condition.html](race-condition.html) | гонка `count++`: шагаешь потоками вручную → ловишь lost update |
| [thread-pool.html](thread-pool.html) | пул: задачи в очередь, 3 воркера разбирают, простаивают на пустой |

## Как смотреть
- **Локально:** открой `.html` файл прямо в браузере (двойной клик).
- **Онлайн:** через GitHub Pages (если включён) — иначе GitHub покажет исходник, а не страницу.

Все используют общий [`style.css`](style.css). Без внешних зависимостей.
