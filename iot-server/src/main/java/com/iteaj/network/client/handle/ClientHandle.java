package com.iteaj.network.client.handle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明目标类是一个客户端处理器
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientHandle {

    String name();

    String value() default  "";

    /**
     * 序列化
     */
    Class<?> deserializer() default Void.class;
}
