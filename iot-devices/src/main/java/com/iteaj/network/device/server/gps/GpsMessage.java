package com.iteaj.network.device.server.gps;

import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.utils.ByteUtil;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * create time: 2021/1/14
 *
 * @author iteaj
 * @since 1.0
 */
public class GpsMessage extends UnParseBodyMessage {

    private static byte[] flag = new byte[]{0x7e};
    private static Logger LOGGER = LoggerFactory.getLogger(GpsMessage.class);

    public GpsMessage(ByteBuf byteBuf) {
        super(byteBuf);
    }

    public GpsMessage(MessageHead head, MessageBody body) {
        super(head, body);

        // 标识(1) + 头长度 + 报文体长度 + 校验码(1) + 标识(1)
        this.message = new byte[1 + head.getHeadLength() + body.getBodyLength() + 1 + 1];
        ByteUtil.addBytes(this.message, flag, 0);
        ByteUtil.addBytes(this.message, head.getHeadMessage(), 1);
        ByteUtil.addBytes(this.message, body.getBodyMessage(), head.getHeadLength() + 1);

        byte[] validateMessage = ByteUtil.subBytes(this.message, 1);
        byte xor = Validate.getXor(validateMessage);
        this.message[this.message.length - 2] = xor;

        ByteUtil.addBytes(this.message, flag, this.message.length - 1);


    }

    /**
     * 构建 平台通用响应报文 通过请求的报文
     * @param request 请求报文
     * @return
     */
    public static GpsMessage buildPlatformCommonRespMessageByRequest(GpsMessage request, GpsCommonRespResult result) {
        if(request == null) {
            throw new IllegalArgumentException("请求报文必传[request]");
        }
        if(result == null) {
            throw new IllegalArgumentException("响应结果必传[result]");
        }

        GpsMessageHead requestHead = (GpsMessageHead) request.getHead();

        // 构建报文头
        GpsMessageHead respHead = GpsMessageHead.resp(requestHead.getEquipCode(), requestHead.getMessageId()
                , GpsProtocolType.PResp, 5);


        // 构建报文体
        byte[] bodyMessage = new byte[5];
        ByteUtil.addBytes(bodyMessage, ByteUtil.shortToBytes(Short.valueOf(requestHead.getMessageId())), 0);
        ByteUtil.addBytes(bodyMessage, ByteUtil.hexToByte(requestHead.getTradeType().code), 2);
        ByteUtil.addBytes(bodyMessage, result.code, 4);

        GpsMessageBody body = new GpsMessageBody(bodyMessage);

        return new GpsMessage(respHead, body);
    }

    @Override
    public UnParseBodyMessage build() throws IOException {
        byte[] message = this.getMessage();
        String hex = ByteUtil.bytesToHex(message);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("十六进制报文：{}", hex);
        }

        byte[] subBytes = ByteUtil.subBytes(message, 1, message.length - 2);

        // 获取消息体属性
        byte[] bodyAttr = ByteUtil.subBytes(subBytes, 2, 4);

        byte[] headMessage = ByteUtil.subBytes(subBytes, 0, 12);
        GpsMessageHead messageHead = GpsMessageHead.build(headMessage);

        byte[] bodyMessage = ByteUtil.subBytes(subBytes, 12, subBytes.length);
        GpsMessageBody messageBody = new GpsMessageBody(bodyMessage);

        return new GpsMessage(messageHead, messageBody);
    }
}
