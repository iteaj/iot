package com.iteaj.iot.client.modbus;

import com.iteaj.network.ConcurrentStorageManager;

import java.util.concurrent.ConcurrentHashMap;

public class ModbusClientManager extends ConcurrentStorageManager<String, ModbusMultiServerClient> {

    public ModbusClientManager() {
        super(new ConcurrentHashMap<>(64));
    }
}
