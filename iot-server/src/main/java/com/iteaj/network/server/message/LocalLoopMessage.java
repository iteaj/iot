package com.iteaj.network.server.message;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.Message;
import com.iteaj.network.protocol.ProtocolType;

/**
 * <p>本地回环报文</p>
 * @see com.iteaj.network.server.protocol.AbstractLoopProtocol  使用此报文的协议基类
 * @see com.iteaj.network.server.service.PlatformLoopService 此协议的业务基类
 * Create Date By 2017-09-29
 * @author iteaj
 * @since 1.7
 */
public class LocalLoopMessage extends AbstractMessage {

    private Object param; //本地回环参数
    private ProtocolType type; //本地回环协议类型

    public LocalLoopMessage(ProtocolType type, Object param) {
        this.param = param;
        this.type = type;
    }

    @Override
    public MessageHead getHead() {
        return new LocalLoopHead();
    }

    @Override
    public MessageBody getBody() {
        return new LocalLoopBody();
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public ProtocolType getType() {
        return type;
    }

    public void setType(ProtocolType type) {
        this.type = type;
    }

    public class LocalLoopHead implements Message.MessageHead {

        @Override
        public String getEquipCode() {
            return null;
        }

        @Override
        public String getMessageId() {
            return null;
        }

        @Override
        public ProtocolType getTradeType() {
            return LocalLoopMessage.this.type;
        }

        @Override
        public int getHeadLength() {
            return 0;
        }

        @Override
        public byte[] getHeadMessage() {
            return null;
        }

        public ProtocolType getType() {
            return type;
        }
    }

    public class LocalLoopBody implements Message.MessageBody {

        @Override
        public int getBodyLength() {
            return 0;
        }

        @Override
        public byte[] getBodyMessage() {
            return null;
        }
    }
}
