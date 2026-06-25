package com.aidar.foundations.collections;

import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

public class MyStackTest {

    // ---------- new stack ----------

    @Test
    void newStackIsEmpty() {
        MyStack<Integer> stack = new MyStack<>();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    // ---------- push ----------

    @Test
    void pushMakesStackNonEmpty() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        assertFalse(stack.isEmpty());
        assertEquals(1, stack.size());
    }

    @Test
    void pushGrowsSize() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.size());
    }

    // ---------- peek ----------

    @Test
    void peekReturnsTopWithoutRemoving() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.peek());
        assertEquals(2, stack.peek());   // idempotent
        assertEquals(2, stack.size());   // size unchanged
    }

    @Test
    void peekReflectsLatestPush() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        assertEquals(1, stack.peek());
        stack.push(2);
        assertEquals(2, stack.peek());
    }

    @Test
    void peekThrowsOnEmpty() {
        MyStack<Integer> stack = new MyStack<>();
        assertThrows(EmptyStackException.class, stack::peek);
    }

    // ---------- pop ----------

    @Test
    void popReturnsAndRemovesTop() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.pop());
        assertEquals(1, stack.size());
        assertEquals(1, stack.peek());
    }

    @Test
    void popFollowsLifoOrder() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void popDownToEmpty() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        stack.pop();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    void popThrowsOnEmpty() {
        MyStack<Integer> stack = new MyStack<>();
        assertThrows(EmptyStackException.class, stack::pop);
    }

    @Test
    void popThrowsAfterDraining() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        stack.pop();
        assertThrows(EmptyStackException.class, stack::pop);
    }

    // ---------- reuse after emptying ----------

    @Test
    void canPushAgainAfterEmptying() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        stack.push(2);
        stack.pop();
        stack.pop();
        assertTrue(stack.isEmpty());

        stack.push(9);
        assertEquals(9, stack.peek());
        assertEquals(1, stack.size());
    }

    // ---------- mixed sequence ----------

    @Test
    void mixedPushPopSequence() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.pop());   // [1]
        stack.push(3);                  // [1, 3]
        stack.push(4);                  // [1, 3, 4]
        assertEquals(4, stack.pop());
        assertEquals(3, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }
}
