package com.iteaj.iot.client.http;

public interface HttpManager {

    HttpResponseMessage get(HttpRequestMessage requestMessage);

    void get(HttpRequestMessage requestMessage, HttpClientProtocol.HttpProtocolCall handle);

    HttpResponseMessage post(HttpRequestMessage requestMessage);

    void post(HttpRequestMessage requestMessage, HttpClientProtocol.HttpProtocolCall handle);

}
