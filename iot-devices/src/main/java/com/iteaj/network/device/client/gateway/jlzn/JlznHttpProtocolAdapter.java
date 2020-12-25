package com.iteaj.network.device.client.gateway.jlzn;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.iteaj.iot.client.http.HttpClientProtocol;
import com.iteaj.iot.client.http.HttpResponseMessage;

/**
 * 渐朗智能网关使用Http请求协议
 */
public abstract class JlznHttpProtocolAdapter extends HttpClientProtocol {

    private String requestId = "12342";
    private String token = "@33ssfXdgdsfdsf";
    /**
     * 0	success	           成功
     * 1	unauthorized	   未授权
     * 2	提示哪个参数非法	   参数错误
     * 3	request time out   请求超时
     * 100	Internal error	   其他内部错误
     */
    private int statusCode; // 操作响应状态吗
    private String statusMsg; // 操作响应状态消息
    private static final String gatewayUrl = "http://%s/req.cgi";

    public JlznHttpProtocolAdapter(String ip) {
        this(ip, null);
    }

    public JlznHttpProtocolAdapter(String ip, String deviceSn) {
        super(String.format(gatewayUrl, ip), deviceSn);
    }

    @Override
    public abstract JlznProtocolType protocolType();

    @Override
    protected void resolverResponseMessage(HttpResponseMessage content) {
        JSONObject jsonObject = JSONUtil.parseObj(content.getMessage("UTF-8"));

        JSONObject responseParams = jsonObject.getJSONObject("response_params");
        this.statusMsg = responseParams.getStr("status_msg");
        this.statusCode = responseParams.getInt("status_code");
    }

    public boolean isOk() {
        return this.statusCode == 0;
    }

    @Override
    public String desc() {
        return "渐朗智能网关(http://www.jianlangzn.com/)";
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

}
