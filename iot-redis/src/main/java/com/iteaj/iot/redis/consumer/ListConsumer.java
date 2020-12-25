package com.iteaj.iot.redis.consumer;

import java.util.List;

public interface ListConsumer<V> extends RedisConsumer<V>, BlockConsumer {

    @Override
    Integer consumer(List<V> vs);

}
