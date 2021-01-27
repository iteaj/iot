package com.iteaj.iot.client.http;

import com.iteaj.iot.client.IotClientBootstrap;
import com.iteaj.network.FreeProtocolHandle;
import com.iteaj.network.Protocol;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.ProtocolHandle;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpClientProtocol implements Protocol {

    private String url;

    /**
     * 要请求的设备的设备编号
     */
    private String deviceSn;

    /**
     * 是否同步调用
     */
    private boolean sync = false;

    /**
     * 请求超时时间
     */
    private long timeout = HttpConst.DefaultTimeout;

    /**
     * 自定义协议处理
     */
    private ProtocolHandle handle;
    /**
     * 请求报文
     */
    private HttpRequestMessage requestMessage;

    /**
     * 响应报文
     */
    private HttpResponseMessage responseMessage;

    /**
     * 协议额外携带的参数
     */
    private Map<String, Object> param;

    public HttpClientProtocol(String url) {
        this.url = url;
    }

    public HttpClientProtocol(String url, String deviceSn) {
        this.url = url;
        this.deviceSn = deviceSn;
    }

    /**
     * @param <T>
     * @return 当前协议对象
     * @throws ProtocolException
     */
    public <T extends HttpClientProtocol> T request() throws HttpProtocolException {
        requestMessage = doBuildRequestMessage();
        if(requestMessage == null) {
            throw new HttpProtocolException("构建Http协议请求报文失败, 请求报文不能未空");
        }

        if(requestMessage.getUrl() == null) {
            requestMessage.setUrl(getUrl());
        }

        if(requestMessage.getUrl() == null) {
            throw new HttpProtocolException("必须指定请求的Url地址");
        }

        if(this.handle == null) {
            this.handle = IotClientBootstrap.businessFactory.getProtocolHandle(getClass());
        }

        HttpResponseMessage responseMessage = null;
        try {
            if(getTimeout() != HttpConst.DefaultTimeout) {
                requestMessage.setTimeout(getTimeout());
            }

            if(requestMessage.getMethod() == HttpMethod.Get) {
                if(isSync()) {
                    responseMessage = IotClientBootstrap.httpManager.get(requestMessage);
                } else {
                    IotClientBootstrap.httpManager.get(requestMessage, new HttpProtocolCall());
                }
            } else {
                if(isSync()) {
                    responseMessage = IotClientBootstrap.httpManager.post(requestMessage);
                } else {
                    IotClientBootstrap.httpManager.post(requestMessage, new HttpProtocolCall());
                }
            }
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if(cause instanceof SocketTimeoutException) {
                throw new HttpProtocolException("网络超时, 请检查网络", cause);
            } else if(cause instanceof ConnectException) {
                throw new HttpProtocolException("连接异常, 请检查网络和地址", cause);
            } else if(cause instanceof NoRouteToHostException) {
                throw new HttpProtocolException("找不到主机, 请检查访问地址", cause);
            }

            throw new HttpProtocolException("请求失败", cause);
        }

        /**
         * 如果是同步的話, 先解析报文, 然后再执行业务
         */
        if(this.isSync()) {
            try {
                resolverResponseMessage(responseMessage);

                if(handle != null) {
                    handle.business(this);
                }

                return (T) this;
            } catch (Exception e) {
                throw new HttpProtocolException("解析设备报文错误", e);
            }
        } else {
            return null;
        }

    }

    /**
     *
     * @param handle 自定义此协议的处理器
     * @param <T>
     * @return 当前协议对象
     * @throws ProtocolException
     */
    public  <T extends HttpClientProtocol> T request(FreeProtocolHandle<T> handle) throws ProtocolException {
        this.handle = handle;
        if(this.handle == null) {
            throw new ProtocolException("参数必填[FreeProtocolHandle]");
        }

        return this.request();
    }

    public <P extends HttpClientProtocol> P sync(long timeout) {
        this.sync = true;
        this.timeout = timeout;
        return (P) this;
    }

    public <P extends HttpClientProtocol> P sync() {
        return this.sync(HttpConst.DefaultTimeout);
    }

    public boolean isSync() {
        return sync;
    }

    public Object getParam(String key) {
        if(null == this.param) {
            return null;
        }

        return this.param.get(key);
    }

    public HttpClientProtocol addParam(String key, Object value) {
        if(this.param == null) {
            this.param = new HashMap<>();
        }
        this.param.put(key, value);
        return this;
    }

    @Override
    public HttpClientProtocol removeParam(String key) {
        if(this.param == null) {
            return this;
        } else {
            this.param.remove(key);
            return this;
        }
    }

    /**
     * 解析响应报文
     * @param responseMessage
     */
    protected abstract void resolverResponseMessage(HttpResponseMessage responseMessage);

    @Override
    public String desc() {
        return "Http客户端请求协议";
    }

    public <T extends HttpClientProtocol> T timeout(long timeout) {
        if(timeout <= 0) {
            throw new ProtocolException("超时时间必须大于0(s)");
        }
        this.timeout = timeout;

        return (T) this;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    /**
     * 构建请求报文
     * @return
     */
    protected abstract HttpRequestMessage doBuildRequestMessage();

    @Override
    public HttpRequestMessage requestMessage() {
        return this.requestMessage;
    }

    @Override
    public HttpResponseMessage responseMessage() {
        return this.responseMessage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimeout() {
        return timeout;
    }

    @Override
    public String getEquipCode() {
        return this.deviceSn;
    }

    public class HttpProtocolCall {

        public void call(Throwable throwable) {
            throwable.printStackTrace();
        }

        public void responseResolver(HttpResponseMessage message) {
            resolverResponseMessage(message);
            HttpClientProtocol.this.responseMessage = message;

            if(HttpClientProtocol.this.handle != null) {
                HttpClientProtocol.this.handle.business(HttpClientProtocol.this);
            }
        }
    }
}
