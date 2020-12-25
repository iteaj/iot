package com.iteaj.network.client;

import com.iteaj.network.Protocol;
import com.iteaj.network.ProtocolHandle;
import org.springframework.core.GenericTypeResolver;

public interface ClientProtocolHandle<T extends Protocol> extends ProtocolHandle<T> {

    default Class<T> protocolClass() {
        return (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), ClientProtocolHandle.class);
    }
    
}
