
# LRU cache — O(1) get/put with eviction

> "Design an LRU cache" is a direct interview question (LeetCode 146). It tests whether you can combine two data structures so that both operations are O(1). The answer shows you think not in terms of individual structures but of their composition against a requirement.

Related: hashmap-internals, arraylist-vs-linkedlist (doubly-linked list).

---

## What it is

A cache of bounded capacity. When it's full and a new key arrives — we evict the **least recently used** (the one untouched the longest). The idea is temporal locality: the recent will soon be needed again.

The requirement that makes the task interesting: **both `get` and `put` are O(1)**. And `get` is also a "use" → it refreshes the element's recency.

---

## Why HashMap + doubly-linked list

We need three things in O(1), and no single structure provides them all alone:

| Needed | HashMap | Doubly-linked list |
|---|---|---|
| find a value by key | ✅ | ❌ O(n) |
| know the recency order | ❌ | ✅ |
| move the recent to the front / evict the old | ❌ | ✅ (if there's a reference to the node + `prev`) |

**The combination:** `HashMap<K, Node>` gives O(1) "key → node", the doubly-linked list gives O(1) "unlink/re-place this node". Together — both operations are O(1).

```
HashMap:  key → ───────────┐
                            ▼
head ⇄ (MRU) ⇄ ... ⇄ (LRU) ⇄ tail
       front              tail = eviction candidate
```

---

## Operations

- **get(key):** not in map → `null`. Present → `unlink(node)` + `addFront(node)` (refresh) → return value.
- **put(key, value):**
  - key present → update value, `unlink` + `addFront` (refresh);
  - absent → create node, `map.put`, `addFront`, `size++`; if `size > capacity` → evict `tail.prev` (LRU): `unlink` + `map.remove(lru.key)` + `size--`.

```
cap=3
put A,B,C : [C B A]
get A     : [A C B]     ← A refreshed, B became LRU
put D     : [D A C]     ← full → B is evicted
get B     : null
```

---

## Three places where people fail

1. **The node stores the `key`.** When evicting the tail, you have to remove the entry from the `map` — you take the key from the node (the value alone isn't enough).
2. **The list must be doubly-linked.** You can unlink an arbitrary node in O(1) only if you have `prev`. Singly-linked → O(n) to find the predecessor.
3. **Sentinel nodes (dummy head + tail).** Any real node always has both neighbors → `addFront`/`unlink` are written without null checks. Dramatically simplifies the code.

---

## Interview traps

- "Why exactly these two structures?" → map for O(1) lookup, list for O(1) reorder/eviction; individually neither is enough.
- "Why store the key in the node?" → to remove it from the map on eviction.
- "Singly or doubly?" → doubly: O(1) unlink of a known node.
- Out of the box in Java: `LinkedHashMap(capacity, 0.75f, true)` + override `removeEldestEntry` = a ready-made LRU.

## Connected

- hashmap-internals
- arraylist-vs-linkedlist
- ring-buffer-queue

## Where it appeared

`java-foundations` (kata MyLRUCache): `MyHashMap<K,Node>` + a custom doubly-linked list with sentinels, `addFront`/`unlink`, eviction of `tail.prev`. Required adding `MyHashMap.remove`.
