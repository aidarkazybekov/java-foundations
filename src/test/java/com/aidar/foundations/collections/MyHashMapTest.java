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

    @Test
    void removeReturnsValueAndShrinks() {
        MyHashMap<Integer, String> map = new MyHashMap<>();
        map.put(1, "a");
        assertEquals("a", map.remove(1));
        assertEquals(0, map.size());
        assertNull(map.get(1));
    }

    @Test
    void removeHeadOfChain() {
        MyHashMap<Integer, String> map = new MyHashMap<>();
        map.put(1, "a");
        map.put(17, "b");          // 1 and 17 collide (same bucket); 1 is chain head
        assertEquals("a", map.remove(1));   // remove the head node
        assertNull(map.get(1));
        assertEquals("b", map.get(17));     // tail still reachable
        assertEquals(1, map.size());
    }

    @Test
    void removeFromMiddleOfChain() {
        MyHashMap<Integer, String> map = new MyHashMap<>();
        map.put(1, "a");
        map.put(17, "b");
        map.put(33, "c");          // chain: 1 -> 17 -> 33
        assertEquals("b", map.remove(17));  // remove the middle node
        assertEquals("a", map.get(1));
        assertEquals("c", map.get(33));
        assertNull(map.get(17));
        assertEquals(2, map.size());
    }

    @Test
    void removeAbsentReturnsNull() {
        MyHashMap<Integer, String> map = new MyHashMap<>();
        map.put(1, "a");
        assertNull(map.remove(2));
        assertEquals(1, map.size());
    }
}
