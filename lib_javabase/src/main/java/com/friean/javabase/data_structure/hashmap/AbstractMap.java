package com.friean.javabase.data_structure.hashmap;


import java.util.Collection;
import java.util.Set;

public class AbstractMap<K,V> implements Map<K,V> {

    Set<K> keySet;
    Collection<V> values;

    @Override
    public int size() {
        return 0;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public boolean containValue(V value) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void putAll(java.util.Map<K, V> map) {

    }
}
