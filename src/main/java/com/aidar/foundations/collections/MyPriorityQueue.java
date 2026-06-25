package com.aidar.foundations.collections;


import java.util.NoSuchElementException;

public class MyPriorityQueue<E extends Comparable<E>> {
    private final MyArrayList<E> heap;

    public MyPriorityQueue() {
        heap = new MyArrayList<>();
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    private boolean less(int i, int j) {
        return heap.get(i).compareTo(heap.get(j)) < 0;
    }

    private void swap(int i, int j) {
        E temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public void add(E e) {
        heap.add(e);
        siftUp(heap.size() - 1);
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if(!less(i, parent)) {
                break;
            }
            swap(i, parent);
            i = parent;
        }
    }

    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        return heap.get(0);
    }

    public E poll() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        E min = heap.get(0);
        int last = heap.size() - 1;
        swap(0, last);
        heap.remove(last);
        if(!isEmpty()) {
            siftDown(0);
        }
        return min;
    }

    private void siftDown(int i) {
        int n = heap.size();
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;
            if(left < n && less(left, smallest)) {
                smallest = left;
            }
            if(right < n && less(right, smallest)) {
                smallest = right;
            }
            if(smallest == i) {
                break;
            }
            swap(i, smallest);
            i = smallest;
        }
    }
}
