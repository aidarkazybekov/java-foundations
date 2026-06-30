package com.aidar.foundations.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyLRUCacheTest {

    // ---------- basics ----------

    @Test
    void newCacheIsEmpty() {
        MyLRUCache<Integer, String> cache = new MyLRUCache<>(2);
        assertEquals(0, cache.size());
    }

    @Test
    void putThenGetReturnsValue() {
        MyLRUCache<Integer, String> cache = new MyLRUCache<>(2);
        cache.put(1, "a");
        assertEquals("a", cache.get(1));
        assertEquals(1, cache.size());
    }

    @Test
    void getMissReturnsNull() {
        MyLRUCache<Integer, String> cache = new MyLRUCache<>(2);
        assertNull(cache.get(42));
    }

    @Test
    void putGrowsSizeUpToCapacity() {
        MyLRUCache<Integer, String> cache = new MyLRUCache<>(3);
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(3, "c");
        assertEquals(3, cache.size());
    }

    // ---------- eviction ----------

    @Test
    void evictsLeastRecentlyUsedOnOverflow() {
        MyLRUCache<Integer, String> cache = new MyLRUCache<>(2);
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(3, "c");        // capacity exceeded -> evict 1 (LRU)

        assertNull(cache.get(1)); // evicted
        assertEquals("b", cache.get(2));
        assertEquals("c", cache.get(3));
        assertEquals(2, cache.size());
    }

    @Test
    void sizeNeverExceedsCapacity() {
        MyLRUCache<Integer, String> cache = new MyLRUCache<>(2);
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(3, "c");
        cache.put(4, "d");
        assertEquals(2, cache.size());
    }

    // ---------- recency refresh (the heart of LRU) ----------

    @Test
    void getRefreshesRecencySoItSurvivesEviction() {
        MyLRUCache<Integer, String> cache = new MyLRUCache<>(2);
        cache.put(1, "a");
        cache.put(2, "b");
        cache.get(1);             // 1 is now most-recently-used; 2 becomes LRU
        cache.put(3, "c");        // evicts 2, NOT 1

        assertEquals("a", cache.get(1)); // survived because refreshed
        assertNull(cache.get(2));        // evicted
        assertEquals("c", cache.get(3));
    }

    @Test
    void classicThreeKeyRefreshScenario() {
        MyLRUCache<Integer, String> cache = new MyLRUCache<>(3);
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(3, "c");
        cache.get(1);             // order by recency: 1, 3, 2  (2 is LRU)
        cache.put(4, "d");        // evicts 2

        assertNull(cache.get(2));
        assertEquals("a", cache.get(1));
        assertEquals("c", cache.get(3));
        assertEquals("d", cache.get(4));
    }

    // ---------- updating an existing key ----------

    @Test
    void putExistingUpdatesValueWithoutGrowing() {
        MyLRUCache<Integer, String> cache = new MyLRUCache<>(2);
        cache.put(1, "a");
        cache.put(1, "z");        // update, not insert
        assertEquals("z", cache.get(1));
        assertEquals(1, cache.size());
    }

    @Test
    void putExistingRefreshesRecency() {
        MyLRUCache<Integer, String> cache = new MyLRUCache<>(2);
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(1, "a2");       // re-put 1 -> refreshes it; 2 becomes LRU
        cache.put(3, "c");        // evicts 2, not 1

        assertEquals("a2", cache.get(1));
        assertNull(cache.get(2));
        assertEquals("c", cache.get(3));
    }

    // ---------- edge: capacity 1 ----------

    @Test
    void capacityOneKeepsOnlyLatest() {
        MyLRUCache<Integer, String> cache = new MyLRUCache<>(1);
        cache.put(1, "a");
        cache.put(2, "b");        // evicts 1 immediately

        assertNull(cache.get(1));
        assertEquals("b", cache.get(2));
        assertEquals(1, cache.size());
    }
}
