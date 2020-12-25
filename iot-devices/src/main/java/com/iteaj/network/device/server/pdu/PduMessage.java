package com.iteaj.network.device.server.pdu;

import com.iteaj.network.message.DeviceMessageHead;
import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.message.VoidMessageBody;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PduMessage extends UnParseBodyMessage {

    private String[] content;
    private Logger logger = LoggerFactory.getLogger(PduMessage.class);
    public PduMessage(byte[] message) {
        super(message);
    }

    public PduMessage(ByteBuf byteBuf) {
        super(byteBuf);
    }

    public PduMessage(PduMessageHead head, byte[] message) {
        super(message);
        this.messageHead = head;
    }

    @Override
    public PduMessage build() throws IOException {
        String message = new String(this.message, "GBK");
        if(message.equals("S")) {
            this.messageHead = new PduMessageHead(null, PduTradeType.心跳);
        } else {
            this.content = message.split("\\s");
            String type = PduMessageUtil.getId(this.content, 1);
            if(logger.isDebugEnabled()) {
                logger.debug("设备协议： {} - 设备报文: {}", type, message);
            }
            PduTradeType pduTradeType = PduTradeType.getInstance(type);

            String equipCode = null;
            if(pduTradeType == PduTradeType.登录) {
                equipCode = PduMessageUtil.getString(this.content, 3);
            }

            String messageId = null;
            if(PduMessageUtil.startWith(this.content, 2, "tag")) {
                messageId = PduMessageUtil.getString(this.content, 2);
            }

            // 开关类型的报文将转换成 {@link PduTradeType#status}
            if(pduTradeType == PduTradeType.open || pduTradeType == PduTradeType.close)
                pduTradeType = PduTradeType.status;

            PduMessageHead pduMessageHead = new PduMessageHead(messageId, equipCode, pduTradeType);
            pduMessageHead.setCheckSum(PduMessageUtil.endString(this.content, 2));
            this.messageHead = pduMessageHead;
        }

        this.messageBody = new VoidMessageBody(this.message);

        return this;
    }

    public static class PduMessageHead extends DeviceMessageHead {

        public PduMessageHead(String messageId, String equipCode, PduTradeType tradeType) {
            this(messageId, equipCode, tradeType, null);
        }

        public PduMessageHead(String equipCode, PduTradeType tradeType) {
            this(null, equipCode, tradeType);
        }

        public PduMessageHead(String messageId, String equipCode, PduTradeType tradeType, String checkSum) {
            super(null, checkSum, messageId, equipCode, tradeType);
        }
    }

    public String[] getContent() {
        return content;
    }

    @Override
    public String toString() {
        try {
            String equipCode = this.getHead() != null ? this.getHead().getEquipCode() : "";
            return "PduMessage{deviceSn=" + equipCode + ", message=" + new String(message, "GBK") + '}';
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
