
# Why `new T[]` is forbidden in Java

> A small but catchy question: "create a generic array" — and a junior writes `new T[n]`, gets a compile error, and flounders. The answer shows you understand the difference between reified arrays and erased generics.

Related: collections-list-set-map, hashmap-internals (inside — an array of buckets).

---

## The gist

`new T[10]` or `new Node<K,V>[10]` is a **compile error**. You can't do it.

The reason is a conflict between two mechanisms:

- **Arrays are reified:** an array remembers its element type at runtime and **checks** every write (`ArrayStoreException` if you put in the wrong thing).
- **Generics are erased:** type information is erased at compile time — at runtime `List<String>` and `List<Integer>` are just `List`.

A generic array would break this check: the runtime check would pass even though the static type lies. That's why Java forbids the creation.

---

## How people work around it (the idiom)

You create a **raw** array and cast, localizing `@SuppressWarnings` in one place:

```java
@SuppressWarnings("unchecked")
private Node<K, V>[] newTable(int capacity) {
    return (Node<K, V>[]) new Node[capacity];
}
```

It's safe **if** the array doesn't "leak" outward and holds only `Node<K,V>`. That's what `ArrayList` does internally too — it keeps an `Object[]` and casts the element on `get`.

---

## Interview traps

- "Why can't you do `new T[]`?" → arrays are reified, generics are erased; a generic array would bypass the runtime write check.
- "How does ArrayList store elements then?" → internally an `Object[]`, cast on access.
- "Why `@SuppressWarnings("unchecked")`?" → you consciously acknowledge an unchecked cast that is safe by construction.

## Connected

- hashmap-internals
- arraylist-vs-linkedlist

## Where it appeared

`java-foundations` — the bucket array in MyHashMap and the backing array in MyArrayList are created via such a cast.
