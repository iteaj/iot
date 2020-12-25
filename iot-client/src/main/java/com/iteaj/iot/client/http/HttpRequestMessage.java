package com.iteaj.iot.client.http;

import cn.hutool.http.Method;
import com.iteaj.network.Message;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestMessage implements Message {

    private String url;

    private long timeout;

    /**
     * http使用的方法, 只支持两种
     * @see HttpMethod#Get
     * @see HttpMethod#Post
     */
    private HttpMethod method;

    /**
     * http请求的请求头
     */
    private Map<String, String> heads;
    /**
     * 请求参数<br>
     *     1. 如果是get请求, 将作为查询参数的一部分, 连接到url后面
     *     2. 如果是post请求, 将作为body
     */
    private Map<String, Object> param;

    public HttpRequestMessage(String url, HttpMethod method) {
        this(url, method, null);
    }

    public HttpRequestMessage(String url, HttpMethod method, Map<String, Object> param) {
        this.url = url;
        this.param = param;
        this.method = method;
    }

    public static HttpRequestMessage get(String url) {
        return get(url, new HashMap<>());
    }

    public static HttpRequestMessage get(String url, Map<String, Object> param) {
        return new HttpRequestMessage(url, HttpMethod.Get, param);
    }

    public static HttpRequestMessage post(String url) {
        return new HttpRequestMessage(url, HttpMethod.Post, new HashMap<>());
    }

    public static HttpRequestMessage post(String url, Map<String, Object> param) {
        return new HttpRequestMessage(url, HttpMethod.Post, param);
    }

    @Override
    public int length() {
        throw new UnsupportedOperationException("不支持的操作");
    }

    @Override
    public byte[] getMessage() {
        throw new UnsupportedOperationException("不支持的操作");
    }

    @Override
    public MessageHead getHead() {
        throw new UnsupportedOperationException("不支持的操作");
    }

    @Override
    public MessageBody getBody() {
        throw new UnsupportedOperationException("不支持的操作");
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public HttpRequestMessage addParam(String key, String value) {
        if(this.param == null) {
            this.param = new HashMap<>();
        }

        this.param.put(key, value);
        return this;
    }

    public Map<String, String> getHeads() {
        return heads;
    }

    public HttpRequestMessage addHeader(String key, String val) {
        if(this.heads == null) {
            this.heads = new HashMap<>();
        }

        this.heads.put(key, val);
        return this;
    }

    public HttpRequestMessage setHeads(Map<String, String> heads) {
        this.heads = heads;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpRequestMessage setUrl(String url) {
        this.url = url;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    public HttpRequestMessage setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }
}
