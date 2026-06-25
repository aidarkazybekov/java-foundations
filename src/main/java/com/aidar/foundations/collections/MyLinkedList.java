package com.aidar.foundations.collections;

import java.util.NoSuchElementException;

public class MyLinkedList<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    static class Node<E> {
        E element;
        Node<E> next;
        Node<E>  prev;

        Node(E element) {
            this.element = element;
        }
    }

    public void addFirst(E e) {
        if (isEmpty()) {
            head = tail = new Node<>(e);
            size++;
            return;
        }
        Node<E> newNode = new Node<>(e);
        newNode.next = head;
        head.prev = newNode;
        head = newNode;
        size++;
    }

    public void addLast(E e) {
        if(isEmpty()) {
            head = tail = new Node<>(e);
            size++;
            return;
        }
        Node<E> newNode = new Node<>(e);
        newNode.prev = tail;
        tail.next = newNode;
        tail = newNode;
        size++;
    }

    public void removeFirst() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        if(head == tail) {
            head = tail = null;
            size--;
            return;
        }
        head = head.next;
        head.prev = null;
        size--;
    }

    public void removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if(tail == head) {
            head = tail = null;
            size--;
            return;
        }
        tail = tail.prev;
        tail.next = null;
        size--;
    }

    public void remove(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if(index == 0) {
            removeFirst();
            return;
        }
        if(index == size - 1) {
            removeLast();
            return;
        }
        int i = 0;
        Node<E> node = head;
        while(i < index) {
            node = node.next;
            i++;
        }
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    public E get(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> node = head;
        for(int i = 0; i < index; i++) {
            node = node.next;
        }
        return node.element;
    }

    public E getFirst() {
        if(head == null) {
            throw new NoSuchElementException();
        }
        return head.element;
    }

    public E getLast() {
        if(tail == null) {
            throw new NoSuchElementException();
        }
        return tail.element;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
