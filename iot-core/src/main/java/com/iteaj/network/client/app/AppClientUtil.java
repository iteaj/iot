package com.iteaj.network.client.app;

import com.alibaba.fastjson.JSONObject;
import com.iteaj.network.CoreConst;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.client.ClientRelationEntity;
import com.iteaj.network.consts.ExecStatus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class AppClientUtil {

    public static String buildByteMessage(AppClientMessage message) {
        try {
            String toJSONString = JSONObject.toJSONString(message) + CoreConst.DELIMITER;

            message.setMessage(toJSONString.getBytes("UTF-8"));

            return toJSONString;
        } catch (UnsupportedEncodingException e) {
            throw new ProtocolException(e.getMessage(), e);
        }
    }

    public static AppClientMessage buildClientRequestMessage(byte[] message, String equipCode) throws IOException {
        String clientMessage = new String(message, "UTF-8");
        JSONObject jsonMessage = JSONObject.parseObject(clientMessage);

        AppClientType clientType = AppClientType.getInstance(jsonMessage.getString("clientType"));
        // 返回心跳报文
        if(clientType == AppClientType.App_Client_Heart) return getHeartMessage(equipCode);

        AppClientMessage appClientMessage = new AppClientMessage(clientType);
        appClientMessage.setType(RequestType.REQ);
        appClientMessage.setJsonMessage(jsonMessage);
        appClientMessage.setDeviceSn(jsonMessage.getString("deviceSn"));
        JSONObject head = jsonMessage.getJSONObject("head");
        appClientMessage.setHead(new AppClientMessageHead(equipCode, jsonMessage
                .getString("messageId"), head.getString("tradeType"), head.getLong("timeout")));

        return appClientMessage;
    }

    public static AppClientMessage<AppClientResponseBody> buildServerResponseMessage(byte[] message) {
        AppClientMessage appClientMessage = new AppClientMessage<AppClientResponseBody>(AppClientType.App_Client_Server);

        try {
            String clientMessage = new String(message, "UTF-8");
            JSONObject jsonMessage = JSONObject.parseObject(clientMessage);
            appClientMessage.setType(RequestType.RES);
            appClientMessage.setDeviceSn(jsonMessage.getString("deviceSn"));

            JSONObject head = jsonMessage.getJSONObject("head");
            appClientMessage.setHead(new AppClientMessageHead(head.getString("equipCode")
                    , jsonMessage.getString("messageId"), head.getString("tradeType"), head.getLong("timeout")));

            JSONObject body = jsonMessage.getJSONObject("body");
            appClientMessage.setBody(new AppClientResponseBody(body.getString("reason"), ExecStatus.getInstance(body.getString("status"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return appClientMessage;
    }

    public static AppClientMessage<AppClientResponseBody> buildServerResponseMessage(ClientRelationEntity entity, AppClientResponseBody body) {
        AppClientMessageHead messageHead = new AppClientMessageHead(entity.getEquipCode(), entity.getMessageId(), null, entity.getTimeout());

        AppClientMessage<AppClientResponseBody> message = new AppClientMessage<>(AppClientType.App_Client_Server, messageHead, body);

        AppClientUtil.buildByteMessage(message);
        return message;
    }

    /**
     * 适用于服务端响应给应用程序
     * @see AppClientResponseBody
     * @param head 报文头 -> 使用应用程序请求报文的报文头
     * @param body 报文体 -> 响应报文体
     * @return
     */
    public static AppClientMessage<AppClientResponseBody> buildServerResponseMessage(AppClientMessageHead head, AppClientResponseBody body) {
        AppClientMessage<AppClientResponseBody> responseMessage = new AppClientMessage<>(AppClientType.App_Client_Server, head, body).setType(RequestType.RES);
        try {
            String messageStr = JSONObject.toJSONString(responseMessage) + CoreConst.DELIMITER;
            responseMessage.setMessage(messageStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return responseMessage;
    }

    /**
     * 返回心跳报文
     * @return
     */
    public static AppClientMessage getHeartMessage(String equipCode) {
        AppClientMessage appClientMessage = new AppClientMessage(AppClientType.App_Client_Heart);
        AppClientMessageHead appClientMessageHead = new AppClientMessageHead(equipCode, appClientMessage.getMessageId(), null);
        appClientMessage.setHead(appClientMessageHead);
        appClientMessage.setType(RequestType.REQ);
        return appClientMessage;
    }
}
