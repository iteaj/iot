package com.iteaj.network.message;

import com.iteaj.network.AbstractMessage;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * <p>此报文未进行报文体解析</p>
 * Create Date By 2017-09-11
 * @author iteaj
 * @since 1.7
 */
public abstract class UnParseBodyMessage extends AbstractMessage {

    protected MessageBody messageBody;
    protected MessageHead messageHead;

    public UnParseBodyMessage(byte[] message) {
        super(message);
    }

    public UnParseBodyMessage(ByteBuf byteBuf) {
        super(byteBuf);
    }

    public UnParseBodyMessage(MessageHead head, MessageBody body) {
        this.messageHead = head;
        this.messageBody = body;
    }

    public abstract UnParseBodyMessage build() throws IOException;

    @Override
    public MessageHead getHead() {
        return messageHead;
    }

    /**
     * 返回 {@link VoidMessageBody}
     * @return
     */
    @Override
    public MessageBody getBody() {
        return messageBody;
    }

    public void setBody(MessageBody messageBody) {
        this.messageBody = messageBody;
    }

    public void setHead(MessageHead messageHead) {
        this.messageHead = messageHead;
    }
}
