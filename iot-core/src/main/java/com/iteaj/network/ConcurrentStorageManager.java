package com.iteaj.network;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Create Date By 2017-09-17
 *
 * @author iteaj
 * @since 1.7
 */
public abstract class ConcurrentStorageManager<K, V>
        implements StorageManager<K, V>{

    private volatile ConcurrentHashMap<K, V> mapper;

    public ConcurrentStorageManager() {
        this(new ConcurrentHashMap<K, V>());
    }

    public ConcurrentStorageManager(ConcurrentHashMap<K, V> mapper) {
        this.mapper = mapper;
    }

    @Override
    public V get(K key) {
        return mapper.get(key);
    }

    @Override
    public V add(K key, V val) {
        return mapper.put(key, val);
    }

    @Override
    public V remove(K key) {
        return mapper.remove(key);
    }

    @Override
    public boolean isExists(K key) {
        return mapper.containsKey(key);
    }

    public int size(){
        return mapper.size();
    }

    @Override
    public ConcurrentHashMap getStorage() {
        return mapper;
    }

    public ConcurrentHashMap<K, V> getMapper() {
        return mapper;
    }

    public void setMapper(ConcurrentHashMap<K, V> mapper) {
        this.mapper = mapper;
    }
}
