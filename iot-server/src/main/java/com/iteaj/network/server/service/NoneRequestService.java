package com.iteaj.network.server.service;

import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.protocol.ProtocolType;
import com.iteaj.network.server.protocol.NoneDealProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NoneRequestService extends PlatformRequestService<NoneDealProtocol> {

    private Logger logger = LoggerFactory.getLogger(NoneRequestService.class);

    @Override
    public Object protocolType() {
        return ProtocolType.NoneMap;
    }

    @Override
    protected Object doSuccess(NoneDealProtocol protocol) {
        return null;
    }

    @Override
    protected Object doFailed(NoneDealProtocol protocol, ExecStatus status) {
        logger.error("protocol");
        return null;
    }
}
