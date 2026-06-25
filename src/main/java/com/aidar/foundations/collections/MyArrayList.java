package com.aidar.foundations.collections;

public class MyArrayList<E> {
    private static final int DEFAULT_CAPACITY = 10;

    private Object[] array;
    private int size;

    public MyArrayList() {
        array = new Object[DEFAULT_CAPACITY];
    }

    private void resize() {
        Object[] newArray = new Object[array.length * 2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    public void add(E e) {
        if (size == array.length) {
            resize();
        }
        array[size] = e;
        size++;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        return (E) array[index];
    }

    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E old = (E) array[index];
        array[index] = element;
        return (E) old;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        System.arraycopy(array, index + 1, array, index, size - index - 1);
        array[--size] = null;
    }

    public int  size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
