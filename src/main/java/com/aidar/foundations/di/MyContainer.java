package com.aidar.foundations.di;

import com.aidar.foundations.collections.MyHashMap;

import java.lang.reflect.Constructor;

public class MyContainer {

    private final MyHashMap<Class<?>, Object> cache = new MyHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        Object cached = cache.get(type);
        if (cached != null) {
            return (T) cached;
        }
        T instance = create(type);
        cache.put(type, instance);
        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T> T create(Class<T> type) {
        try {
            Constructor<?> constructor = type.getConstructors()[0];
            Class<?>[] paramsTypes = constructor.getParameterTypes();

            Object[] dependencies = new Object[paramsTypes.length];
            for (int i = 0; i < paramsTypes.length; i++) {
                dependencies[i] = resolve(paramsTypes[i]);
            }

            return (T) constructor.newInstance(dependencies);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Cannot create " + type.getName(), e);
        }
    }
}
