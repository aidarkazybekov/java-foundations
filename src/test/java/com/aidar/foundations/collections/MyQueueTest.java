package com.aidar.foundations.collections;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class MyQueueTest {

    // ---------- new queue ----------

    @Test
    void newQueueIsEmpty() {
        MyQueue<Integer> q = new MyQueue<>(4);
        assertTrue(q.isEmpty());
        assertEquals(0, q.size());
    }

    // ---------- enqueue ----------

    @Test
    void enqueueMakesNonEmpty() {
        MyQueue<Integer> q = new MyQueue<>(4);
        q.enqueue(1);
        assertFalse(q.isEmpty());
        assertEquals(1, q.size());
    }

    @Test
    void enqueueGrowsSize() {
        MyQueue<Integer> q = new MyQueue<>(4);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        assertEquals(3, q.size());
    }

    // ---------- peek ----------

    @Test
    void peekReturnsFrontWithoutRemoving() {
        MyQueue<Integer> q = new MyQueue<>(4);
        q.enqueue(1);
        q.enqueue(2);
        assertEquals(1, q.peek());   // front is the oldest
        assertEquals(1, q.peek());   // idempotent
        assertEquals(2, q.size());
    }

    @Test
    void peekThrowsOnEmpty() {
        MyQueue<Integer> q = new MyQueue<>(4);
        assertThrows(NoSuchElementException.class, q::peek);
    }

    // ---------- dequeue ----------

    @Test
    void dequeueReturnsAndRemovesFront() {
        MyQueue<Integer> q = new MyQueue<>(4);
        q.enqueue(1);
        q.enqueue(2);
        assertEquals(1, q.dequeue());
        assertEquals(1, q.size());
        assertEquals(2, q.peek());
    }

    @Test
    void dequeueFollowsFifoOrder() {
        MyQueue<Integer> q = new MyQueue<>(4);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        assertEquals(1, q.dequeue());
        assertEquals(2, q.dequeue());
        assertEquals(3, q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    void dequeueDownToEmpty() {
        MyQueue<Integer> q = new MyQueue<>(4);
        q.enqueue(1);
        q.dequeue();
        assertTrue(q.isEmpty());
        assertEquals(0, q.size());
    }

    @Test
    void dequeueThrowsOnEmpty() {
        MyQueue<Integer> q = new MyQueue<>(4);
        assertThrows(NoSuchElementException.class, q::dequeue);
    }

    // ---------- wrap-around ----------

    @Test
    void wrapsAroundWithoutResize() {
        MyQueue<Integer> q = new MyQueue<>(4);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);          // [1,2,3,_]
        assertEquals(1, q.dequeue());
        assertEquals(2, q.dequeue());   // head now at index 2
        q.enqueue(4);
        q.enqueue(5);          // 5 wraps to index 0
        assertEquals(3, q.dequeue());
        assertEquals(4, q.dequeue());
        assertEquals(5, q.dequeue());   // came from the wrapped slot
        assertTrue(q.isEmpty());
    }

    // ---------- resize ----------

    @Test
    void resizePreservesFifoOrder() {
        MyQueue<Integer> q = new MyQueue<>(4);
        for (int i = 1; i <= 10; i++) {   // forces at least one resize past capacity 4
            q.enqueue(i);
        }
        assertEquals(10, q.size());
        for (int i = 1; i <= 10; i++) {
            assertEquals(i, q.dequeue());  // order must survive resize
        }
    }

    @Test
    void resizePreservesOrderWhenWrapped() {
        MyQueue<Integer> q = new MyQueue<>(4);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        assertEquals(1, q.dequeue());
        assertEquals(2, q.dequeue());     // head advanced, region will wrap
        q.enqueue(4);
        q.enqueue(5);                     // wraps
        q.enqueue(6);                     // now full (3,4,5,6) -> next enqueue resizes a wrapped buffer
        q.enqueue(7);
        assertEquals(3, q.dequeue());
        assertEquals(4, q.dequeue());
        assertEquals(5, q.dequeue());
        assertEquals(6, q.dequeue());
        assertEquals(7, q.dequeue());
        assertTrue(q.isEmpty());
    }

    // ---------- reuse ----------

    @Test
    void canEnqueueAgainAfterEmptying() {
        MyQueue<Integer> q = new MyQueue<>(4);
        q.enqueue(1);
        q.enqueue(2);
        q.dequeue();
        q.dequeue();
        assertTrue(q.isEmpty());

        q.enqueue(9);
        assertEquals(9, q.peek());
        assertEquals(1, q.size());
    }
}
