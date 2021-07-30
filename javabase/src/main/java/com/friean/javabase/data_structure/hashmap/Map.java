package com.friean.javabase.data_structure.hashmap;


public interface Map<K,V> {

    int size();

    V put(K key,V value);

    V get(K key);

    boolean isEmpty();

    boolean containsKey(K key);

    boolean containValue(V value);

    void clear();

    void putAll(java.util.Map<K,V> map);

    /**
     * map数据结构存储的元素的抽象
     * @param <K> key
     * @param <V> value
     */
    public interface Entry<K, V> {
        K getKey();
        V getValue();
        void setKey(K key);
        void setValue(V v);
    }
}
