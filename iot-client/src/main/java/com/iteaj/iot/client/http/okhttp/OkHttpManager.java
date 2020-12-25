package com.iteaj.iot.client.http.okhttp;

import com.iteaj.iot.client.http.*;
import okhttp3.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpManager implements HttpManager, InitializingBean {

    private OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(HttpConst.DefaultTimeout, TimeUnit.SECONDS).build();

    @Override
    public HttpResponseMessage get(HttpRequestMessage requestMessage) {
        long timeout = requestMessage.getTimeout();

        String url = requestMessage.getUrl();
        if(!StringUtils.hasText(url)) {
            throw new HttpProtocolException("请求url必填");
        }
        Response response = null;
        try {
            OkHttpClient okHttpClient = client;

            if(timeout != HttpConst.DefaultTimeout) {
                okHttpClient = client.newBuilder().readTimeout(timeout, TimeUnit.SECONDS).build();
            }

            // 构建请求的url
            url = buildRequestUrl(url, requestMessage.getParam());

            Request.Builder builder = new Request.Builder().get().url(url);
            handleRequestHeader(requestMessage, builder);

            response = okHttpClient.newCall(builder.build()).execute();

            ResponseBody responseBody = response.body();
            return new HttpResponseMessage(response.code(), responseBody.bytes(), responseBody.contentType().type());
        } catch (IOException e) {
            throw new HttpProtocolException(e.getMessage(), e);
        } finally {
            if(response != null) {
                response.body().close();
            }
        }
    }

    private void handleRequestHeader(HttpRequestMessage requestMessage, Request.Builder builder) {
        Map<String, String> heads = requestMessage.getHeads();
        if(!CollectionUtils.isEmpty(heads)) {
            heads.forEach((key, val) -> {
                builder.addHeader(key, val);
            });
        }
    }

    @Override
    public void get(HttpRequestMessage requestMessage, HttpClientProtocol.HttpProtocolCall handle) {
        long timeout = requestMessage.getTimeout();

        String url = requestMessage.getUrl();
        if(!StringUtils.hasText(url)) {
            throw new HttpProtocolException("请求url必填");
        }

        if(null == handle) {
            throw new HttpProtocolException("未指定异步协议处理器: [ProtocolHandle]");
        }

        // 构建请求的Url
        url = buildRequestUrl(url, requestMessage.getParam());

        try {
            OkHttpClient okHttpClient = client;
            if(timeout != HttpConst.DefaultTimeout) {
                okHttpClient = client.newBuilder().readTimeout(timeout, TimeUnit.SECONDS).build();
            }

            Request.Builder builder = new Request.Builder().get().url(url);

            // 复制请求头
            handleRequestHeader(requestMessage, builder);

            httpAsyncCallback(handle, okHttpClient, builder);

        } catch (Exception e) {
            throw new HttpProtocolException(e.getMessage(), e);
        }
    }

    private String buildRequestUrl(String url, Map<String, Object> param) {
        if(!CollectionUtils.isEmpty(param)) {
            int indexOf = url.indexOf('?');
            StringBuilder sb = new StringBuilder(url);
            if(indexOf == -1) {
                sb.append('?');
                param.forEach((key, val) -> {
                    sb.append(key).append("=").append(val).append("&");
                });
                return sb.substring(0, sb.length() - 1);
            } else {
                param.forEach((key, val) -> {
                    sb.append("&").append(key).append("=").append(val);
                });
                return sb.toString();
            }
        }

        return url;
    }

    @Override
    public HttpResponseMessage post(HttpRequestMessage requestMessage) {
        long timeout = requestMessage.getTimeout();

        String url = requestMessage.getUrl();
        if(!StringUtils.hasText(url)) {
            throw new HttpProtocolException("请求url必填");
        }

        Response response = null;
        try {
            OkHttpClient okHttpClient = client;

            if(timeout != HttpConst.DefaultTimeout) {
                okHttpClient = client.newBuilder().readTimeout(timeout, TimeUnit.SECONDS).build();
            }

            // 构建请求body
            RequestBody requestBody = buildRequestBody(requestMessage.getParam());

            Request.Builder builder = new Request.Builder().post(requestBody).url(url);
            handleRequestHeader(requestMessage, builder);

            response = okHttpClient.newCall(builder.build()).execute();

            ResponseBody responseBody = response.body();

            return new HttpResponseMessage(response.code(), responseBody.bytes(), responseBody.contentType().type());
        } catch (IOException e) {
            throw new HttpProtocolException(e.getMessage(), e);
        } finally {
            if(response != null) {
                response.body().close();
            }
        }
    }

    private RequestBody buildRequestBody(Map<String, Object> param) {
        FormBody.Builder builder = new FormBody.Builder();
        if(!CollectionUtils.isEmpty(param)) {
            param.forEach((key, val) -> {
                builder.add(key, (String) val);
            });
        }

        return builder.build();
    }

    @Override
    public void post(HttpRequestMessage requestMessage, HttpClientProtocol.HttpProtocolCall handle) {
        long timeout = requestMessage.getTimeout();

        String url = requestMessage.getUrl();
        if(!StringUtils.hasText(url)) {
            throw new HttpProtocolException("请求url必填");
        }

        if(null == handle) {
            throw new HttpProtocolException("未指定异步协议处理器: [ProtocolHandle]");
        }

        try {
            OkHttpClient okHttpClient = client;

            if(timeout != HttpConst.DefaultTimeout) {
                okHttpClient = client.newBuilder().readTimeout(timeout, TimeUnit.SECONDS).build();
            }

            // 构建请求body
            RequestBody requestBody = buildRequestBody(requestMessage.getParam());
            Request.Builder builder = new Request.Builder().post(requestBody).url(url);

            // 处理请求头
            handleRequestHeader(requestMessage, builder);

            // http异步回调执行
            httpAsyncCallback(handle, okHttpClient, builder);

        } catch (Exception e) {
            throw new HttpProtocolException(e.getMessage(), e);
        }
    }

    protected void httpAsyncCallback(HttpClientProtocol.HttpProtocolCall handle, OkHttpClient okHttpClient, Request.Builder builder) {
        okHttpClient.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handle.call(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                handle.responseResolver(new HttpResponseMessage(response.code(), responseBody.bytes(), responseBody.contentType().type()));
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
