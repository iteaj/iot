package com.iteaj.network.device.server.pdu.protocol;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.device.server.pdu.PduMessageUtil;
import com.iteaj.network.device.server.pdu.PduTradeType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.ProtocolException;

/**
 * 控制插口的开关状态
 * @see PduTradeType#close
 * @see PduTradeType#open
 */
public class PduStatusProtocol extends PduPlatformProtocol {

    private static String open = "START open tag='%s' io='%s' check='%s' END\n";
    private static String close = "START close tag='%s' io='%s' check='%s' END\n";

    /**
     * 11111110
     * 要操作的插口， 1表示开, 0标识关
     */
    private String io;
    // 真正的交易类型
    private PduTradeType tradeType;
    public PduStatusProtocol(String equipCode, String io, PduTradeType tradeType) {
        super(equipCode);
        this.io = io;
        this.tradeType = tradeType;
    }

    @Override
    public PduTradeType protocolType() {
        return PduTradeType.status;
    }

    @Override
    protected AbstractMessage doBuildRequestMessage() throws IOException {
        String message;
        if(tradeType == PduTradeType.open) {
            message = open;
        } else if(tradeType == PduTradeType.close) {
            message = close;
        } else {
            throw new ProtocolException("错误的交易类型： " + tradeType);
        }
        if(StringUtils.isBlank(io)) throw new ProtocolException("未指定要操作的插口： io");
        message = String.format(message, getMessageId(), this.getIo(), "%s");

        int check = PduMessageUtil.getCode(message);
        message = String.format(message, check);
        return newMessage(message.getBytes("GBK"));
    }

    @Override
    protected AbstractMessage resolverResponseMessage(AbstractMessage message) {
        return message;
    }

    public PduTradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(PduTradeType tradeType) {
        this.tradeType = tradeType;
    }

    public String getIo() {
        return io;
    }

    public void setIo(String io) {
        this.io = io;
    }
}
