package com.aidar.foundations.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyHashMapTest {

    @Test
    void getReturnsValuePreviouslyPut() {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put("age", 30);

        assertEquals(30, map.get("age"));
    }

    @Test
    void putWithCollisionKeepsBothEntries() {
        MyHashMap<Integer, String> map = new MyHashMap<>();
        map.put(1, "one");
        map.put(17, "seventeen");

        assertEquals("one", map.get(1));
        assertEquals("seventeen", map.get(17));
    }

    @Test
    void getReturnsNullWhenKeyAbsentInNonEmptyBucket() {
        MyHashMap<Integer, String> map = new MyHashMap<>();
        map.put(1, "one");
        map.put(17, "seventeen");
        map.put(3, "three");
        assertNull(map.get(33));
    }

    @Test
    void putWithSameKeyOverwritesValue() {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("aidar", 22);
        map.put("aidar", 23);
        assertEquals(23, map.get("aidar"));
    }

    @Test
    void incrementSizeWhenPutInTheSameBucket() {
        MyHashMap<Integer, String> map = new MyHashMap<>();
        map.put(1, "one");
        map.put(17, "seventeen");
        map.put(3, "three");
        assertEquals(3,map.size());
    }

    @Test
    void resizeTheBucket() {
        MyHashMap<Integer, String> map = new MyHashMap<>();
        for (int i = 1; i <= 100; i++) {
            map.put(i, "num: "+i);
        }
        for (int i = 1; i <= 100; i++) {
            assertEquals("num: "+i, map.get(i));
        }
    }

    @Test
    void supportsNullKey() {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put(null, 42);
        assertEquals(42, map.get(null));

        map.put(null, 43);          // overwrite the null key
        assertEquals(43, map.get(null));
        assertEquals(1, map.size()); // overwrite must not grow size
    }
}
