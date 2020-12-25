package com.iteaj.network.message;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.utils.MessageUtil;

/**
 * <p>默认报文</p>
 * @see #head 类型 {@link DeviceMessageHead}
 * @see #body 类型 {@link DeviceMessageBody}
 * Create Date By 2017-09-12
 * @author iteaj
 * @since 1.7
 */
public class DeviceDefaultMessage extends AbstractMessage {

    /**报文头*/
    private DeviceMessageHead head;
    /**报文体*/
    private DeviceMessageBody body;

    /**设备默认的报文头长度*/
    public static final int DEVICE_HEAD_LENGTH = 89;

    public DeviceDefaultMessage(byte[] message) {
        super(message);
        this.head = MessageUtil.byteArrayToMessageHead(message);
        this.body = new DeviceMessageBody(ByteUtil.subBytes(message, DEVICE_HEAD_LENGTH));
    }

    public DeviceDefaultMessage(UnParseBodyMessage unParseBodyMessage) {
        this(unParseBodyMessage.getMessage());
        this.body = (DeviceMessageBody) unParseBodyMessage.getBody();
        this.head = (DeviceMessageHead) unParseBodyMessage.getHead();
    }

    public DeviceDefaultMessage(DeviceMessageHead head, DeviceMessageBody body) {
        this.head = head;
        this.body = body;

        if(null == head || null == body)
            throw new IllegalArgumentException("参数 head or body 为空");

        message = new byte[head.getHeadLength()+body.getBodyLength()];
        ByteUtil.addBytes(message, head.getHeadMessage(), 0);
        ByteUtil.addBytes(message, body.getBodyMessage(), head.getHeadLength());
    }

    @Override
    public DeviceMessageHead getHead() {
        return head;
    }

    @Override
    public DeviceMessageBody getBody() {
        return body;
    }
}
