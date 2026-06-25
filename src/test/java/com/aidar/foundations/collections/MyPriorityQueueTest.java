package com.aidar.foundations.collections;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MyPriorityQueueTest {

    // ---------- new queue ----------

    @Test
    void newQueueIsEmpty() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        assertTrue(pq.isEmpty());
        assertEquals(0, pq.size());
    }

    // ---------- add ----------

    @Test
    void addGrowsSize() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        pq.add(5);
        pq.add(1);
        pq.add(3);
        assertEquals(3, pq.size());
        assertFalse(pq.isEmpty());
    }

    // ---------- peek ----------

    @Test
    void peekReturnsMinWithoutRemoving() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        pq.add(5);
        pq.add(1);
        pq.add(3);
        assertEquals(1, pq.peek());
        assertEquals(1, pq.peek());   // idempotent
        assertEquals(3, pq.size());
    }

    @Test
    void peekMinUpdatesAsSmallerArrives() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        pq.add(5);
        assertEquals(5, pq.peek());
        pq.add(2);
        assertEquals(2, pq.peek());
        pq.add(9);
        assertEquals(2, pq.peek());   // 9 is not smaller, min stays 2
        pq.add(1);
        assertEquals(1, pq.peek());
    }

    @Test
    void peekThrowsOnEmpty() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        assertThrows(NoSuchElementException.class, pq::peek);
    }

    // ---------- poll ----------

    @Test
    void pollReturnsAndRemovesMin() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        pq.add(5);
        pq.add(1);
        pq.add(3);
        assertEquals(1, pq.poll());
        assertEquals(2, pq.size());
        assertEquals(3, pq.peek());   // next min after removing 1
    }

    @Test
    void pollReturnsAscendingOrder() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        int[] input = {5, 1, 3, 2, 4};
        for (int x : input) pq.add(x);

        assertEquals(1, pq.poll());
        assertEquals(2, pq.poll());
        assertEquals(3, pq.poll());
        assertEquals(4, pq.poll());
        assertEquals(5, pq.poll());
        assertTrue(pq.isEmpty());
    }

    @Test
    void pollHandlesDuplicates() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        pq.add(2);
        pq.add(2);
        pq.add(1);
        assertEquals(1, pq.poll());
        assertEquals(2, pq.poll());
        assertEquals(2, pq.poll());
        assertTrue(pq.isEmpty());
    }

    @Test
    void singleElementAddPoll() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        pq.add(42);
        assertEquals(42, pq.poll());
        assertTrue(pq.isEmpty());
    }

    @Test
    void pollDownToEmpty() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        pq.add(1);
        pq.add(2);
        pq.poll();
        pq.poll();
        assertTrue(pq.isEmpty());
        assertEquals(0, pq.size());
    }

    @Test
    void pollThrowsOnEmpty() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        assertThrows(NoSuchElementException.class, pq::poll);
    }

    // ---------- interleaved ----------

    @Test
    void interleavedAddAndPoll() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        pq.add(5);
        pq.add(3);
        assertEquals(3, pq.poll());   // min so far
        pq.add(1);
        pq.add(4);
        assertEquals(1, pq.poll());
        assertEquals(4, pq.poll());
        assertEquals(5, pq.poll());
        assertTrue(pq.isEmpty());
    }

    // ---------- stress: behaves like a sorted drain ----------

    @Test
    void largeRandomInputDrainsInNonDecreasingOrder() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        Random random = new Random(42);   // fixed seed = reproducible
        int count = 500;
        for (int i = 0; i < count; i++) {
            pq.add(random.nextInt(1000));
        }
        assertEquals(count, pq.size());

        int previous = Integer.MIN_VALUE;
        for (int i = 0; i < count; i++) {
            int current = pq.poll();
            assertTrue(current >= previous, "heap violated order at position " + i);
            previous = current;
        }
        assertTrue(pq.isEmpty());
    }

    // ---------- works with other Comparable types ----------

    @Test
    void worksWithStrings() {
        MyPriorityQueue<String> pq = new MyPriorityQueue<>();
        pq.add("banana");
        pq.add("apple");
        pq.add("cherry");
        assertEquals("apple", pq.poll());
        assertEquals("banana", pq.poll());
        assertEquals("cherry", pq.poll());
    }
}
