package com.iteaj.network.client.handle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface IotMapping {

    String value();

    /**
     * 是否同步
     * @return
     */
    boolean sync() default false;
}
