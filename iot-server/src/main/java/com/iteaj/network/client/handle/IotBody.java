package com.iteaj.network.client.handle;

import com.iteaj.network.client.app.AppClientMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * create time: 2021/3/5
 *  用来声明此方法再注入参数的时候注入的对象是报文的body
 *  1. 如果是实体类则直接反序列化后注入
 *  2. 如果是Json对象则直接注入json对象
 * @see AppClientMessage#getBody()
 * @author iteaj
 * @since 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IotBody {

}
