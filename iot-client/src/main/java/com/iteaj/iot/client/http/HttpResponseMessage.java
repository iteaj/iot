package com.iteaj.iot.client.http;

import com.iteaj.network.Message;

import java.io.UnsupportedEncodingException;

public class HttpResponseMessage implements Message {

    /**
     * 标准http协议状态码
     */
    private int code;

    /**
     * 响应报文
     */
    private byte[] message;

    /**
     * 响应报文的 contentType
     */
    private String contentType;

    public HttpResponseMessage(int code, byte[] message, String contentType) {
        this.code = code;
        this.message = message;
        this.contentType = contentType;
    }

    public int getCode() {
        return code;
    }

    public String getMessage(String charset) {
        try {
            return new String(message, charset);
        } catch (UnsupportedEncodingException e) {
            throw new HttpProtocolException(e.getMessage(), e);
        }
    }

    public String getContentType() {
        return contentType;
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    @Override
    public int length() {
        return this.message.length;
    }

    @Override
    public byte[] getMessage() {
        return message;
    }

    @Override
    public MessageHead getHead() {
        throw new UnsupportedOperationException("不支持的操作");
    }

    @Override
    public MessageBody getBody() {
        throw new UnsupportedOperationException("不支持的操作");
    }
}
