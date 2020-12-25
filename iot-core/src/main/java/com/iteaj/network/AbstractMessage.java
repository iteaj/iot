package com.iteaj.network;

import com.iteaj.network.message.VoidMessageBody;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>报文</p>
 * Create Date By 2017-09-11
 * @author iteaj
 * @since 1.7
 */
public abstract class AbstractMessage implements Message {

    /**报文数据*/
    protected byte[] message;
    protected static final byte[] emptyMessage = new byte[]{};
    /**空报文体*/
    public static final VoidMessageBody VOID_MESSAGE_BODY = new VoidMessageBody();
    /** 日志 */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected AbstractMessage() { }

    public AbstractMessage(byte[] message) {
        if(null == message)
            throw new IllegalArgumentException("参数不能为空");
        this.message = message;
    }

    public AbstractMessage(ByteBuf byteBuf) {
        try {
            if(null == byteBuf)
                throw new IllegalArgumentException("参数不能为空");

            byteBuf.retain();
            this.message = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(message);
        } finally {
            // 释放内存缓冲
            ReferenceCountUtil.release(byteBuf);
        }
    }

    @Override
    public int length() {
        return message.length;
    }

    @Override
    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }
}
