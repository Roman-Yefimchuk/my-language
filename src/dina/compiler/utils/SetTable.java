package dina.compiler.utils;

import java.util.*;

public class SetTable<K, V> {

    private Hashtable<K, V> table;
    private ArrayList<K> list;

    public SetTable() {
        table = new Hashtable<K, V>();
        list = new ArrayList<K>();
    }

    public void put(K key, V value) {
        table.put(key, value);
        list.add(key);
    }

    public V getValue(K key) {
        return table.get(key);
    }

    public V getValue(int index) {
        return table.get(list.get(index));
    }

    public K getKey(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    public Enumeration<V> elements() {
        return table.elements();
    }

    public void clear() {
        table.clear();
        list.clear();
    }

    public int indexOfKey(K key) {
        return list.indexOf(key);
    }
}
