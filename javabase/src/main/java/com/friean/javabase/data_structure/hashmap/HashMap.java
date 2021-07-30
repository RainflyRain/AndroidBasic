package com.friean.javabase.data_structure.hashmap;

import java.util.LinkedHashMap;

public class HashMap<K,V> extends AbstractMap<K,V>{

    Node<K,V>[] table;

    public HashMap() {
        loadFactor = DEFAULT_LOAD_FACTOR;
    }

    @Override
    public V put(K key, V value) {
        int hash = hash(key);

        Node<K,V>[] tab;
        Node<K,V> p;
        int n;
        if ((tab = table) == null || (n = tab.length) == 0){
            tab = resize();
        }

        return super.put(key, value);
    }

    /**
     * 默认容器容量为 16
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    /**
     * 默认扩容比例
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 指定扩容比例
     */
    final float loadFactor;

    static final int TREEIFY_THRESHOLD = 8;

    /**
     * 下一次扩容量
     */
    int threshold;

    /**
     * 一个独立的方法逻辑需要严密判断，重新创建 或 扩充容量 元素数组
     * @return 元素数组
     */
    private Node<K, V>[] resize() {
        Node<K,V>[] oldTab = table;
        //原来数组容量
        int oldCap = (oldTab == null)?0:oldTab.length;
        int oldThr = threshold;
        int newCap = 0,newThr = 0;
        if (oldCap > 0){

        }else {
            //第一次创建容量为 16
            newCap = DEFAULT_INITIAL_CAPACITY;
            //第二次 扩容 16*0.75
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }

        if (newThr == 0){
            float ft = (float) newCap*loadFactor;
        }

        threshold = newThr;

        Node<K,V>[] newTab = new Node[newCap];
        table = newTab;

        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e = oldTab[j];
                if (e.nest == null){
                    newTab[e.hash & (newCap-1)] = e;
                }else if (e instanceof TreeNode){
                    //链表
                }else{

                }
            }
        }

        return new Node[0];
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }


    /**
     * HashMap 存储的具体元素
     * @param <K> key
     * @param <V> value
     */
    static class Node<K,V> implements Map.Entry<K,V>{
        final int hash;
        final K key;
        final V value;
        final Node<K,V> nest;

        public Node(int hash,K key, V value, Node<K, V> nest) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.nest = nest;
        }

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public V getValue() {
            return null;
        }

        @Override
        public void setKey(K key) {

        }

        @Override
        public void setValue(V v) {

        }
    }

    static final class TreeNode<K,V> extends Node<K,V>{
        TreeNode<K,V> parent;  // red-black tree links
        TreeNode<K,V> left;
        TreeNode<K,V> right;
        TreeNode<K,V> prev;    // needed to unlink next upon deletion
        boolean red;

        public TreeNode(int hash, K key, V value, Node<K, V> nest) {
            super(hash, key, value, nest);
        }
    }
}
