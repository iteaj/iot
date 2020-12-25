package com.iteaj.network;

/**
 * <p>存储管理</p>
 * Create Date By 2017-09-17
 * @author iteaj
 * @since 1.7
 */
public interface StorageManager<K, V> {

    V get(K key);

    V add(K key, V val);

    V remove(K key);

    boolean isExists(K key);

    Object getStorage();
}
