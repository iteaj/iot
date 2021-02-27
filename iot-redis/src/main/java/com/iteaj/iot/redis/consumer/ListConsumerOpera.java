package com.iteaj.iot.redis.consumer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ListConsumerOpera implements BlockConsumerOpera {

    private List<ListConsumer> consumers;

    public ListConsumerOpera(List<ListConsumer> consumers) {
        this.consumers = consumers;
    }

    @Override
    public List invoker(String key, long timeout) {
        Object o = template().opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
        if(o != null) {
            return Arrays.asList(o);
        } else {
            return null;
        }
    }

    @Override
    public List invoker(String key, int size) {
        return template().opsForList().range(key, 0, size);
    }

    @Override
    public List deserialize(List<?> value, Class clazz) {
        if(CollectionUtils.isEmpty(value)) {
            return null;
        } else {
            return (List) value.stream().map(item -> {
                if(item instanceof JSONObject) {
                    return ((JSONObject) item).toJavaObject(clazz);
                } else {
                    return item;
                }
            }).collect(Collectors.toList());
        }
    }

    @Override
    public void remove(String key, int size) {
        template().opsForList().trim(key, size, -1);
    }

    @Override
    public List<RedisConsumer> blocks() {
        return consumers.stream().filter(item -> item.block()).collect(Collectors.toList());
    }

    @Override
    public List<RedisConsumer> consumers() {
        return consumers.stream().filter(item -> !item.block()).collect(Collectors.toList());
    }
}
