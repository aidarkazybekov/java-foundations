package com.aidar.foundations.collections;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class MyArrayListTest {

    @Test
    public void testAdd() {
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        myArrayList.add(1);
        assertEquals(1, myArrayList.get(0));
    }

    @Test
    public void getByIndex() {
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        myArrayList.add(1);
        myArrayList.add(2);
        myArrayList.add(3);
        myArrayList.add(4);
        myArrayList.add(5);
        myArrayList.add(6);
        assertEquals(5, myArrayList.get(4));
    }

    @Test
    public void testRemove() {
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        myArrayList.add(1);
        myArrayList.add(2);
        myArrayList.add(3);
        myArrayList.add(4);
        myArrayList.add(5);
        myArrayList.remove(4);
        assertThrows(IndexOutOfBoundsException.class, () -> myArrayList.get(4));
    }

    @Test
    public void getSize() {
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        myArrayList.add(1);
        myArrayList.add(2);
        myArrayList.add(3);
        myArrayList.add(4);
        myArrayList.add(5);
        assertEquals(5, myArrayList.size());
    }

    @Test
    public void testIsEmpty() {
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        myArrayList.add(1);
        myArrayList.add(2);
        myArrayList.add(3);
        myArrayList.remove(2);
        myArrayList.remove(1);
        myArrayList.remove(0);
        assertTrue(myArrayList.isEmpty());
    }

    @Test
    public void testSetAtIndex() {
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        myArrayList.add(1);
        myArrayList.add(2);
        myArrayList.add(67);
        myArrayList.add(4);
        myArrayList.add(5);
        myArrayList.add(6);
        assertEquals(67, myArrayList.set(2, 3));
    }

    @Test
    public void testAddMoreThanCapacity() {
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        for (int i = 1; i <= 11; i++) {
            myArrayList.add(i);
        }
        assertEquals(11, myArrayList.get(10));
        assertEquals(11, myArrayList.size());
    }

}