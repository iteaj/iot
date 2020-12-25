package com.iteaj.iot.redis.handle;

import com.iteaj.iot.redis.consumer.ListConsumer;
import com.iteaj.iot.redis.producer.ListProducer;
import com.iteaj.network.Protocol;
import com.iteaj.network.ProtocolHandle;
import org.springframework.data.redis.core.ListOperations;

import java.util.List;

/**
 * Redis List opera
 * @see ListOperations
 * @param <T>
 * @param <V>
 */
public interface RedisListHandle<T extends Protocol, V> extends ProtocolHandle<T>, ListProducer<T>, ListConsumer<V> {

    @Override
    Integer consumer(List<V> vs);

    @Override
    Object business(T protocol);

    @Override
    default ListOperations operation() {
        return template().opsForList();
    }
}
