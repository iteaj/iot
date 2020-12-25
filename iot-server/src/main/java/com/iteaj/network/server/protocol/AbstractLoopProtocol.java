package com.iteaj.network.server.protocol;

import com.iteaj.network.Message;
import com.iteaj.network.server.message.LocalLoopMessage;
import com.iteaj.network.server.service.PlatformLoopService;

/**
 * <p>本地到本地的协议,此协议平台不向外发送报文
 * ,也不会有设备请求此协议的报文</p>
 *
 * @see LocalLoopMessage  此协议使用的报文
 * @see PlatformLoopService  此协议使用的业务基类
 * Create Date By 2017-09-29
 * @author iteaj
 * @since 1.7
 */
public abstract class AbstractLoopProtocol extends PlatformRequestProtocol<LocalLoopMessage> {

    private LocalLoopMessage loopMessage;

    public AbstractLoopProtocol(String equipCode, LocalLoopMessage loopMessage) {
        super(equipCode);
        this.loopMessage = loopMessage;
    }

    @Override
    public Object relationKey() {
        throw null;
    }

    @Override
    public boolean isRelation() {
        return false;
    }

    @Override
    public Message requestMessage() {
        return loopMessage;
    }

    @Override
    public Message responseMessage() {
        return loopMessage;
    }

    @Override
    public String desc() {
        return "Loop Protocol 本地回环协议";
    }
}
