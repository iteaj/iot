package com.iteaj.network.server;

import com.iteaj.network.ProtocolHandle;
import org.springframework.core.GenericTypeResolver;

public interface ServerProtocolHandle<T extends AbstractServerProtocol> extends ProtocolHandle<T> {

    default Class<T> protocolClass() {
        return (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), ServerProtocolHandle.class);
    }
}
