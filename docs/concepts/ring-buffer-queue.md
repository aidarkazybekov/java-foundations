
# Ring buffer — an array-backed queue without shifts

> How to build a FIFO queue on an array so that both `enqueue` and `dequeue` are O(1). The naive version that shifts elements is O(n) and immediately gives away a junior. The ring buffer is what sits under `ArrayDeque`, and a frequent interview question.

Related: arraylist-vs-linkedlist (amortized array resize).

---

## The problem with a naive array-backed queue

A queue is FIFO: add to the end, take from the front. If you store it in an array and shift all elements left on `dequeue` — every removal is O(n). On a large queue that's a disaster.

The ring idea: **don't move the array — move the start pointer**, and "wrap" indices with `% capacity`. Freed cells on the left get reused.

---

## Structure

Three fields: the array, `head` (start index), and `size`. The tail (where to write) is **computed**, not stored:

```
write position = (head + size) % capacity
```

```
capacity = 6,  head = 3,  size = 4
index:    0    1    2    3    4    5
        ┌────┬────┬────┬────┬────┬────┐
        │ 0  │ ·  │ ·  │ 3  │ 4  │ 5  │
        └────┴────┴────┴────┴────┴────┘
          ↑              ↑
        write          head (start)
   logical order: 3 → 4 → 5 → 0   (wrapped past the edge!)
```

- **enqueue:** `array[(head + size) % capacity] = e; size++`
- **dequeue:** `e = array[head]; array[head] = null; head = (head + 1) % capacity; size--`
- **peek:** `array[head]`

`% capacity` automatically wraps the index from the end of the array back to the beginning — hence "ring".

---

## Two traps (this is what separates a middle)

### 1. "Full or empty?" when `head == tail`

If you store only `head` and `tail`, then when they coincide it's unclear: is the queue empty or full — both states give `head == tail`. Solutions:
- **keep `size`** (simple and unambiguous): `empty = size==0`, `full = size==capacity`;
- or sacrifice one cell (capacity−1 usable).

The first option is cleaner — that's why you store `size` rather than `tail`.

### 2. Resize when the region is wrapped

When the buffer is full and needs to grow — you **can't** copy the array as-is (`System.arraycopy` wholesale): the logical order may be broken across the edge (like `3→4→5→0` above). You have to walk **in logical order** and rewrite into the new array from scratch:

```
for k from 0 to size-1:
    newArray[k] = array[(head + k) % capacity]
head = 0
```

After this the order is "straightened out", with the start at index 0.

---

## Interview traps

- "A queue on an array without O(n) dequeue?" → a ring buffer, move head, not the elements.
- "How to distinguish full from empty?" → a `size` counter (or sacrifice a cell).
- "What's special about resize?" → the wrapped region is copied in logical order, not as a block.
- `% capacity` on `head` or on the write — forget it, and the index runs off the array.

## Connected

- arraylist-vs-linkedlist
- binary-heap-priority-queue
- hashmap-internals

## Where it appeared

Implemented in the pet project `java-foundations` (kata MyQueue): `(head+size)%length`, wrapping, resize in logical order.
