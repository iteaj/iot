package com.iteaj.network.client.app;

import com.alibaba.fastjson.JSONObject;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.client.ClientMessage;
import org.apache.commons.lang3.StringUtils;

import java.beans.Transient;

/**
 * 应用程序客户端报文
 */
public class AppClientMessage<T extends AppClientMessageBody> extends ClientMessage {

    private RequestType type;
    private JSONObject jsonMessage;
    private AppClientType clientType;

    protected AppClientMessage(AppClientType clientType) {
        super(new byte[0]);
        this.clientType = clientType;
    }

    protected AppClientMessage(AppClientType clientType, AppClientMessageHead head, T body) {
        this(clientType);
        this.messageHead = head;
        this.messageBody = body;
    }

    /**
     * 适用于应用程序向服务端发起要操作设备的请求
     * @param deviceSn 要操作设备的设备编号
     * @param tradeType 要操作的服务端的协议类型 {@link AppClientTypeMatcherHandle#isMatcher(String)}
     * @param messageBody 操作的数据
     * @return
     */
    public static AppClientMessage<AppClientMessageBody> getInstance(String deviceSn, AppTradeType tradeType, AppClientMessageBody messageBody) {
        if(null == messageBody) throw new ProtocolException("请传入正确的操作内容");
        if(null == tradeType) throw new ProtocolException("请传入正确的操作类型");
        if(StringUtils.isBlank(deviceSn)) throw new ProtocolException("请传入要操作的设备的设备编号");

        AppClientMessage appClientMessage = new AppClientMessage(AppClientType.App_Client_Server);
        AppClientMessageHead appClientMessageHead = new AppClientMessageHead(null
                , appClientMessage.getMessageId(), tradeType.getTradeType());
        appClientMessage.setDeviceSn(deviceSn);
        appClientMessage.setBody(messageBody);
        appClientMessage.setType(RequestType.REQ);
        appClientMessage.setHead(appClientMessageHead);
        return appClientMessage;
    }

    /**
     * 不操作任何设备, 单纯的和服务端数据交互
     * deviceSn 为空
     * @param tradeType
     * @param messageBody
     * @return
     */
    public static AppClientMessage<AppClientMessageBody> getInstance(AppTradeType tradeType, AppClientMessageBody messageBody) {
        if(null == messageBody) throw new ProtocolException("请传入正确的操作内容");
        if(null == tradeType) throw new ProtocolException("请传入正确的操作类型");

        AppClientMessage appClientMessage = new AppClientMessage(AppClientType.App_Client_Server);
        AppClientMessageHead appClientMessageHead = new AppClientMessageHead(null
                , appClientMessage.getMessageId(), tradeType.getTradeType());
        appClientMessage.setBody(messageBody);
        appClientMessage.setType(RequestType.REQ);
        appClientMessage.setHead(appClientMessageHead);
        return appClientMessage;
    }

    @Override
    public String getMessageId() {
        AppClientMessageHead head = getHead();
        if(null == head) {
            return super.getMessageId();
        }

        return head.getMessageId();
    }

    @Override
    @Transient
    public byte[] getMessage() {
        return super.getMessage();
    }

    @Override
    public AppClientMessageHead getHead() {
        return (AppClientMessageHead) super.getHead();
    }

    @Override
    public T getBody() {
        return (T) super.getBody();
    }

    public AppClientType getClientType() {
        return clientType;
    }

    public AppClientMessage<T> setClientType(AppClientType clientType) {
        this.clientType = clientType;
        return this;
    }

    @Transient
    public JSONObject getJsonMessage() {
        return jsonMessage;
    }

    public void setJsonMessage(JSONObject jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    public RequestType getType() {
        return type;
    }

    public AppClientMessage<T> setType(RequestType type) {
        this.type = type;
        return this;
    }
}
