package com.aidar.foundations.collections;

import java.util.NoSuchElementException;

public class MyQueue<E> {
    private Object[] array;
    private int head;
    private int size;
    private static final int CAPACITY = 16;

    public MyQueue() {
        this(CAPACITY);
    }

    public MyQueue(int capacity) {
        array = new Object[capacity];
    }

    public void resize() {
        Object[] newArray = new Object[array.length * 2];
        for(int i = 0; i < size; i++) {
            newArray[i] = array[(head+i)%array.length];
        }
        head = 0;
        array = newArray;
    }

    public void enqueue(E e) {
        if(size == array.length) {
            resize();
        }
        array[(head+size)%array.length] = e;
        size++;
    }

    @SuppressWarnings("unchecked")
    public E dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        E e = (E) array[head];
        array[head] = null;
        head = (head+1)%array.length;
        size--;
        return e;
    }

    @SuppressWarnings("unchecked")
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return (E) array[head];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
