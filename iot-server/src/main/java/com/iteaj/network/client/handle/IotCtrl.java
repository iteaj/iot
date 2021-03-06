package com.iteaj.network.client.handle;

import com.iteaj.network.client.AppClientServerProtocol;
import com.iteaj.network.client.ParamResolver;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 客户端请求方法映射控制器
 * @see com.iteaj.network.client.ClientRelation 如果返回此实例, 则将自动发起协议调用
 * @see com.iteaj.network.client.AppClientServerHandle#doBusiness(AppClientServerProtocol)
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface IotCtrl {

    /**
     * 名称
     * @return
     */
    String value();

    /**
     * 此处理方法的参数解析
     */
    Class<? extends ParamResolver> resolver() default JsonResolver.class;
}
