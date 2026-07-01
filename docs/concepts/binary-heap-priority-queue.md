
# Binary heap and PriorityQueue

> How `PriorityQueue` works internally and why add/poll are O(log n). The engine behind Dijkstra, top-K, k-way merge, and schedulers. In interviews they ask about sift-up/down and how a tree lives inside an array.

Related: arraylist-vs-linkedlist (an array as storage).

---

## What it is

A regular queue is FIFO. A **priority queue** always hands back the **minimum** (or maximum) element, regardless of insertion order. The implementation is a **binary heap**: a complete binary tree stored in an array.

**Invariant (min-heap):** every parent ≤ its children ⇒ the minimum is always at the root. The heap is **not** fully sorted — there's no order between siblings.

```
        1
      /   \
     3     2
    / \   /
   7   5 4
```

---

## The tree lives in an array (no pointers)

A complete tree lays out by levels into an array with no gaps; the links are arithmetic. For index `i`:

```
parent = (i - 1) / 2
left   = 2i + 1
right  = 2i + 2
```

```
tree:   1   3 2   7 5 4
index:  0   1 2   3 4 5
```

Node `i=1` (value 3): children `2·1+1=3` (7) and `2·1+2=4` (5). Matches the tree.

---

## Operations — O(log n) (the tree's height)

**add → sift-up (bubble up):** put it at the end of the array, then while it's less than its parent — swap with it and rise.

```
while i > 0 and heap[i] < heap[(i-1)/2]:
    swap(i, parent);  i = parent
```

**poll → sift-down (sink):** the answer = the root. Move the **last** element to the root, cut off the end, and sink it down:

```
repeat:
    l = 2i+1, r = 2i+2, smallest = i
    if l < size and heap[l] < heap[smallest]: smallest = l
    if r < size and heap[r] < heap[smallest]: smallest = r
    if smallest == i: stop
    swap(i, smallest);  i = smallest
```

**peek → `heap[0]`** in O(1).

---

## Traps (middle level)

- **sift-down swaps with the SMALLER of the children.** If you swap with the larger one — you break the invariant with the other child.
- **Check the bounds:** `l < size`, `r < size` (a node may have no children).
- **heapify in O(n):** building a heap from a ready array "bottom-up" is cheaper than n insertions at O(log n) = O(n log n).
- **top-K in O(n log K):** keep a min-heap of size K, drop the root when a larger element arrives.

## Interview traps

- "How is PriorityQueue implemented?" → a binary heap in an array; add/poll O(log n), peek O(1).
- "Why an array and not nodes?" → a complete tree = an array with no gaps, links by indices, cache-friendly.
- "Is a heap sorted?" → no, only parent ≤ children; only the path to the root is sorted.

## Connected

- ring-buffer-queue
- arraylist-vs-linkedlist
- hashmap-internals

## Where it appeared

Implemented in `java-foundations` (kata MyPriorityQueue): a min-heap on top of a custom ArrayList, `less`/`swap`, verified with a stress test on 500 random numbers.
