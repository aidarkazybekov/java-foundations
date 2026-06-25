package com.aidar.foundations.collections;

public class MyHashMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = newTable(INITIAL_CAPACITY);
    }

    static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public void put(K key, V value) {
        if (putVal(key, value)) {
            size++;
            resize();
        }
    }

    public V get(K key) {
        Node<K, V> current = table[getIndex(key)];
        while (current != null) {
            if (keysEqual(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public int size() {
        return size;
    }

    /** Inserts the mapping. Returns true if a new node was added, false on overwrite. */
    private boolean putVal(K key, V value) {
        int index = getIndex(key);
        Node<K, V> current = table[index];

        if (current == null) {
            table[index] = new Node<>(key, value);
            return true;
        }

        while (true) {
            if (keysEqual(current.key, key)) {
                current.value = value;
                return false;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value);
                return true;
            }
            current = current.next;
        }
    }

    private void resize() {
        if (size < table.length * DEFAULT_LOAD_FACTOR) {
            return;
        }
        Node<K, V>[] oldTable = table;
        table = newTable(oldTable.length * 2);
        for (Node<K, V> node : oldTable) {
            for (; node != null; node = node.next) {
                putVal(node.key, node.value);
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        h ^= (h >>> 16);
        return h & (table.length - 1);
    }

    private boolean keysEqual(K a, K b) {
        return a == b || (a != null && a.equals(b));
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] newTable(int capacity) {
        return (Node<K, V>[]) new Node[capacity];
    }
}
