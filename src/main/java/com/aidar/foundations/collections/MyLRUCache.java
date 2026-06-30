package com.aidar.foundations.collections;


public class MyLRUCache<K, V> {

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final MyHashMap<K, Node<K,V>> map;
    private final Node<K,V> head;
    private final Node<K,V> tail;
    private int size;

    public MyLRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new MyHashMap<>();
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public int size() {
        return size;
    }

    private void addFront(Node<K, V> node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void unlink(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public V get(K key) {
        Node<K, V> node = map.get(key);
        if (node == null) {
            return null;
        }
        unlink(node);
        addFront(node);
        return node.value;
    }

    public void put(K key, V value) {
        Node<K, V> existing = map.get(key);
        if (existing != null) {
            existing.value = value;
            unlink(existing);
            addFront(existing);
            return;
        }

        Node<K, V> node = new Node<>(key, value);
        map.put(key, node);
        addFront(node);
        size++;

        if(size > capacity) {
            Node<K, V> lru = tail.prev;
            unlink(lru);
            map.remove(lru.key);
            size--;
        }
    }

}
