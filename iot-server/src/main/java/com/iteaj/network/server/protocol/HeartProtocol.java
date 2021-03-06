package com.iteaj.network.server.protocol;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.Message;
import com.iteaj.network.protocol.CommonProtocolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create time: 2021/3/6
 *  通用的心跳协议
 * @author iteaj
 * @since 1.0
 */
public class HeartProtocol extends DeviceRequestProtocol<AbstractMessage> {

    private static Logger logger = LoggerFactory.getLogger(HeartProtocol.class);

    protected HeartProtocol(AbstractMessage requestMessage) {
        super(requestMessage);
    }

    public static HeartProtocol getInstance(AbstractMessage requestMessage) {
        return new HeartProtocol(requestMessage);
    }

    @Override
    public CommonProtocolType protocolType() {
        return CommonProtocolType.Common_Heart;
    }

    @Override
    protected AbstractMessage doBuildResponseMessage() {
        return null;
    }

    @Override
    protected void resolverRequestMessage(AbstractMessage requestMessage) {
        if(logger.isTraceEnabled()) {
            Message.MessageHead head = requestMessage.getHead();
            logger.trace("心跳报文 设备编号：{} - 报文类型：{}"
                    , head != null ? head.getEquipCode() : ""
                    , requestMessage.getClass().getName());
        }
    }
}
