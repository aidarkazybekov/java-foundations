package com.aidar.foundations.collections;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {

    // ---------- new list ----------

    @Test
    void newListIsEmpty() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    // ---------- addFirst ----------

    @Test
    void addFirstOnEmptySetsBothEnds() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addFirst(1);
        assertEquals(1, list.getFirst());
        assertEquals(1, list.getLast());
        assertEquals(1, list.size());
        assertFalse(list.isEmpty());
    }

    @Test
    void addFirstInsertsAtHeadInReverseOrder() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addFirst(1);
        list.addFirst(2);
        list.addFirst(3);              // [3, 2, 1]
        assertEquals(3, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(1, list.get(2));
        assertEquals(3, list.size());
    }

    // ---------- addLast ----------

    @Test
    void addLastOnEmptySetsBothEnds() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        assertEquals(1, list.getFirst());
        assertEquals(1, list.getLast());
        assertEquals(1, list.size());
    }

    @Test
    void addLastAppendsInOrder() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);              // [1, 2, 3]
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(2));
        assertEquals(3, list.getLast());
        assertEquals(3, list.size());
    }

    @Test
    void mixedAddFirstAndAddLast() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        list.addFirst(0);
        list.addLast(2);             // [0, 1, 2]
        assertEquals(0, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
    }

    // ---------- get bounds ----------

    @Test
    void getThrowsOnNegativeIndex() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    void getThrowsOnIndexEqualToSize() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    // ---------- getFirst / getLast on empty ----------

    @Test
    void getFirstThrowsOnEmpty() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        assertThrows(NoSuchElementException.class, list::getFirst);
    }

    @Test
    void getLastThrowsOnEmpty() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        assertThrows(NoSuchElementException.class, list::getLast);
    }

    // ---------- removeFirst ----------

    @Test
    void removeFirstMovesHead() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.removeFirst();          // [2, 3]
        assertEquals(2, list.getFirst());
        assertEquals(2, list.get(0));
        assertEquals(2, list.size());
    }

    @Test
    void removeFirstDownToEmpty() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        list.removeFirst();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void removeFirstThrowsOnEmpty() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        assertThrows(NoSuchElementException.class, list::removeFirst);
    }

    // ---------- removeLast ----------

    @Test
    void removeLastMovesTail() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.removeLast();           // [1, 2]
        assertEquals(2, list.getLast());
        assertEquals(2, list.size());
    }

    @Test
    void removeLastDownToEmpty() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        list.removeLast();
        assertTrue(list.isEmpty());
    }

    @Test
    void removeLastThrowsOnEmpty() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        assertThrows(NoSuchElementException.class, list::removeLast);
    }

    // ---------- remove(index) ----------

    @Test
    void removeAtHeadIndex() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.remove(0);              // [2, 3]
        assertEquals(2, list.get(0));
        assertEquals(2, list.size());
    }

    @Test
    void removeAtLastIndex() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.remove(2);              // [1, 2]
        assertEquals(2, list.getLast());
        assertEquals(2, list.size());
    }

    @Test
    void removeAtMiddleStitchesNeighbours() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);
        list.remove(1);              // remove value 2 -> [1, 3, 4]
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
        assertEquals(4, list.get(2));
        assertEquals(3, list.size());
    }

    @Test
    void removeThrowsOnOutOfBounds() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    // ---------- rebuild after emptying (head/tail reset) ----------

    @Test
    void canAddAgainAfterEmptying() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.removeFirst();
        list.removeLast();
        assertTrue(list.isEmpty());

        list.addLast(9);             // must work after both ends went null
        assertEquals(9, list.getFirst());
        assertEquals(9, list.getLast());
        assertEquals(1, list.size());
    }
}
