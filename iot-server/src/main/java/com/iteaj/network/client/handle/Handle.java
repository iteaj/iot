package com.iteaj.network.client.handle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface Handle {

    String name();
}
