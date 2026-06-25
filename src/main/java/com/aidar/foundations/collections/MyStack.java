package com.aidar.foundations.collections;

import java.util.EmptyStackException;

public class MyStack<E> {
    private final MyLinkedList<E> myLinkedList;

    public MyStack() {
        myLinkedList = new MyLinkedList<>();
    }

    public int size() {
        return myLinkedList.size();
    }

    public boolean isEmpty() {
        return myLinkedList.isEmpty();
    }

    public void push(E e) {
        myLinkedList.addLast(e);
    }

    public E pop() {
        if(isEmpty()) {
            throw new EmptyStackException();
        }
        E e = myLinkedList.getLast();
        myLinkedList.removeLast();
        return e;
    }

    public E peek() {
        if(isEmpty()) {
            throw new EmptyStackException();
        }
        return myLinkedList.getLast();
    }
}
